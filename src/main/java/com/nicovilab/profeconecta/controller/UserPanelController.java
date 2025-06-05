/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Anuncio;
import com.nicovilab.profeconecta.model.AnuncioDetail;
import com.nicovilab.profeconecta.model.Materia;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.AdService;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.service.ProfileService;
import com.nicovilab.profeconecta.service.UserService;
import static com.nicovilab.profeconecta.utils.Utils.getProvinceNamesModel;
import static com.nicovilab.profeconecta.utils.Utils.getTownsByProvince;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.UserPanel;
import com.nicovilab.profeconecta.view.userAdsView.EditAdDialog;
import com.nicovilab.profeconecta.view.userAdsView.SearchAdDialog;
import com.nicovilab.profeconecta.view.userAdsView.UserAdsView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Nico
 */
public class UserPanelController {

    private final MainJFrame view;
    private final UserPanel userPanel;
    private final ProfileService profileService;
    private final Usuario user;
    private final DatabaseService databaseService;
    private final AdService adService;
    private final List<Materia> materias;
    private final UserAdsView userAdsView;
    private final UserService userService;
    private final SearchAdDialog searchAdDialog;
    private final Map<String, Materia> subjectMapByName = new HashMap<>();
    private final Map<Integer, Materia> subjectMapById = new HashMap<>();

    public UserPanelController(MainJFrame view, UserPanel userPanel, Usuario user) {
        profileService = new ProfileService();
        adService = new AdService();
        databaseService = new DatabaseService();
        userAdsView = new UserAdsView();
        userService = new UserService();
        searchAdDialog = new SearchAdDialog();

        this.view = view;
        this.userPanel = userPanel;
        this.user = user;
        this.materias = databaseService.getSubjects();

        userPanel.getProvinceComboBoxSearchPanel().setModel(getProvinceNamesModel(true));
        addProvinceComboBoxListener();

        userPanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        userPanel.addExitButtonActionListener(this.getExitButtonActionListener());
        userPanel.addCreateButtonCreatePanelActionListener(this.getCreateButtonCreatePanelActionListener());
        userPanel.addDeleteButtonCreatePanelActionListener(this.getDeleteButtonCreatePanelActionListener());
        userPanel.addCalendarButtonActionListener(this.getCalendarButtonActionListener());
        userPanel.addChatButtonActionListener(this.getChatButtonActionListener());

        populateSubjectComboBox(materias);
        loadUserAdsById(user.getIdUsuario());
        addSearchButtonListener();

        searchAdDialog.setAdCardClickListener(ad -> {
            new UserAdDetailController(view, user, ad);
        });

    }

    private ActionListener getProfileButtonActionListener() {
        return (ActionEvent e) -> {
            try {
                new ProfileController(view, view.getProfilePanel(), user);
            } catch (SQLException ex) {
                Logger.getLogger(UserPanelController.class.getName()).log(Level.SEVERE, null, ex);
            }
            view.showPanel("profile");

        };
    }

    private ActionListener getCreateButtonCreatePanelActionListener() {
    return e -> {
        String selectedSubjectName = (String) userPanel.getSubjectComboBoxCreatePanel().getSelectedItem();
        Materia selectedMateria = subjectMapByName.get(selectedSubjectName);

        if (selectedMateria == null) {
            userPanel.setInformationLabelCreatePanel("Debe seleccionar una materia válida", Color.RED);
            return;
        }

        String title = userPanel.getTitleTextFieldCreatePanel().getText();
        Double price = (Double) userPanel.getPriceSpinnerCreatePanel().getValue();
        String description = userPanel.getDescriptionTextArea().getText();
        int selectedSubjectId = selectedMateria.getIdMateria();

        if (validateAdCreation(selectedSubjectName, title, price, description)) {
            if (adService.adSuccessful(user.getIdUsuario(), selectedSubjectId, title, price, description)) {
                userPanel.setInformationLabelCreatePanel("El anuncio se ha creado correctamente", Color.GREEN);
                userPanel.clearAllFields();
                loadUserAdsById(user.getIdUsuario());
            } else {
                userPanel.setInformationLabelCreatePanel("Error al crear el anuncio", Color.RED);
            }
        }
    };
}

    private ActionListener getDeleteButtonCreatePanelActionListener() {
        return (ActionEvent e) -> {
            userPanel.clearAllFields();
        };
    }

    private ActionListener getCalendarButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("calendar");
            new BookingController(view, view.getCalendarPanel(), user);
        };
    }
    
    private ActionListener getChatButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("chatpanel");
            new ChatController(view, view.getChatPanel(), user);
        };
    }

    private ActionListener getExitButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }

    public void populateSubjectComboBox(List<Materia> materias) {
        DefaultComboBoxModel<String> subjectModel = new DefaultComboBoxModel<>();

        for (Materia materia : materias) {
            subjectModel.addElement(materia.getNombre());
            subjectMapByName.put(materia.getNombre(), materia);
            subjectMapById.put(materia.getIdMateria(), materia);
        }

        userPanel.getSubjectComboBoxCreatePanel().setModel(subjectModel);
        userPanel.getSubjectComboBoxSearchPanel().setModel(subjectModel);
    }

    public boolean validateAdCreation(String selectedSubject, String title, Double price, String description) {

        if (selectedSubject == null || selectedSubject.trim().isEmpty()) {
            userPanel.setInformationLabelCreatePanel("Debe seleccionar una materia", Color.RED);
            return false;
        }

        if (title == null || title.trim().isEmpty()) {
            userPanel.setInformationLabelCreatePanel("El título no puede estar vacío", Color.RED);
            return false;
        }

        if (price == null || price <= 0) {
            userPanel.setInformationLabelCreatePanel("El precio debe ser mayor que 0", Color.RED);
            return false;
        }

        if (description == null || description.trim().isEmpty()) {
            userPanel.setInformationLabelCreatePanel("La descripción no puede estar vacía", Color.RED);
            return false;
        }
        return true;
    }

    public void loadUserAdsById(int idUsuario) {
        List<Anuncio> anuncios = adService.fetchUserAdsById(idUsuario);

        ActionListener onEdit = e -> {
            int adId = Integer.parseInt(e.getActionCommand());
            Anuncio anuncio = anuncios.stream()
                    .filter(a -> a.getIdAnuncio() == adId)
                    .findFirst()
                    .orElse(null);

            if (anuncio != null) {
                EditAdDialog dialog = new EditAdDialog(anuncio, updatedAd -> {
                    if (adService.updateAd(updatedAd)) {
                        loadUserAdsById(idUsuario);
                    }
                });
                dialog.setVisible(true);
            }
        };

        ActionListener onDelete = e -> {
            int adId = Integer.parseInt(e.getActionCommand());
            int confirm = JOptionPane.showConfirmDialog(null, "¿Estas seguro de querer eliminar el anuncio?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (adService.deleteAd(adId)) {
                    loadUserAdsById(idUsuario);
                }
            }
        };

        ActionListener onToggleState = e -> {
            JButton source = (JButton) e.getSource();
            int adId = Integer.parseInt(source.getActionCommand());
            int newState = (int) source.getClientProperty("newState");

            Anuncio anuncio = anuncios.stream()
                    .filter(a -> a.getIdAnuncio() == adId)
                    .findFirst()
                    .orElse(null);

            if (anuncio != null) {
                anuncio.setActivo(newState == 1);
                if (adService.updateAd(anuncio)) {
                    loadUserAdsById(idUsuario);
                }
            }
        };

        JPanel panelAnuncios = userAdsView.createUserAdsPanel(anuncios, subjectMapById, onEdit, onDelete, onToggleState);

        userPanel.getEditScrollPane().setViewportView(panelAnuncios != null && !anuncios.isEmpty()
                ? panelAnuncios
                : new JLabel("No hay anuncios disponibles"));
    }

    public void addSearchButtonListener() {
        userPanel.getSearchButtonSearchPanel().addActionListener(e -> {
            Map<String, Object> filtros = new HashMap<>();
            String materiaSeleccionada = (String) userPanel.getSubjectComboBoxCreatePanel().getSelectedItem();
            Materia materiaSeleccionadaObj = subjectMapByName.get(materiaSeleccionada);

            if (materiaSeleccionadaObj != null) {
                filtros.put("id_materia", materiaSeleccionadaObj.getIdMateria());
            }

            String nombreUsuario = userPanel.getNameTextFieldSearchPanel().getText();
            if (nombreUsuario != null && !nombreUsuario.isBlank()) {
                filtros.put("usuario", nombreUsuario);
            }

            String municipio = (String) userPanel.getTownComboBoxSearchPanel().getSelectedItem();
            if (municipio != null && !municipio.isBlank()) {
                filtros.put("ciudad", municipio);
            }

            Double valoracionMin = (Double) userPanel.getRatingSpinnerSearchPanel().getValue();
            if (valoracionMin != null && valoracionMin > 0) {
                filtros.put("valoracion", valoracionMin);
            }

            if (userPanel.getProximityCheckBox().isSelected()) {
                filtros.put("userIdReferencia", user.getIdUsuario());
            }

            List<AnuncioDetail> anuncios = adService.fetchAdsFilteredDTO(filtros);
            JPanel resultsPanel = searchAdDialog.createCardsPanel(anuncios);
            userPanel.getSearchScrollPane().setViewportView(resultsPanel);

            resultsPanel.revalidate();
            resultsPanel.repaint();
        });
    }

    private void addProvinceComboBoxListener() {  //todo añadir las coordenadas a los ayuntamiento que faltan
        userPanel.getProvinceComboBoxSearchPanel().addActionListener(e -> {
            String selectedProvince = (String) userPanel.getProvinceComboBoxSearchPanel().getSelectedItem();

            if (selectedProvince != null) {
                userPanel.enableTownComboboxSearchPanel(true);
                List<String> towns = getTownsByProvince(selectedProvince, true);

                DefaultComboBoxModel<String> townModel = new DefaultComboBoxModel<>();
                townModel.addAll(towns);
                userPanel.getTownComboBoxSearchPanel().setModel(townModel);
            }
        });
    }
}
