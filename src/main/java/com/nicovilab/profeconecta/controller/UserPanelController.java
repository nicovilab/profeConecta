/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Anuncio;
import com.nicovilab.profeconecta.model.AnuncioDTO;
import com.nicovilab.profeconecta.model.Materia;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.address.AutonomousCommunity;
import com.nicovilab.profeconecta.model.address.Province;
import com.nicovilab.profeconecta.model.address.Town;
import com.nicovilab.profeconecta.service.AdService;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.service.ProfileService;
import com.nicovilab.profeconecta.service.UserService;
import static com.nicovilab.profeconecta.utils.Utils.getAutonomousCommunitiesData;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.UserPanel;
import com.nicovilab.profeconecta.view.userAdsView.EditAdDialog;
import com.nicovilab.profeconecta.view.userAdsView.SearchAdDialog;
import com.nicovilab.profeconecta.view.userAdsView.UserAdsView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
    private final List<AutonomousCommunity> autonomousCommunities;
    private Map<Integer, Materia> subjectMap = new HashMap<>();

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

        this.autonomousCommunities = getAutonomousCommunitiesData();

        populateAddressData(autonomousCommunities);

        addProvinceComboBoxListener();

        userPanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        userPanel.addExitButtonActionListener(this.getExitButtonActionListener());
        userPanel.addCreateButtonCreatePanelActionListener(this.getCreateButtonCreatePanelActionListener());
        userPanel.addDeleteButtonCreatePanelActionListener(this.getDeleteButtonCreatePanelActionListener());

        populateSubjectComboBox(materias);
        try {
            loadUserAdsById(user.getIdUsuario());
        } catch (SQLException ex) {
            Logger.getLogger(UserPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
        addSearchButtonListener();

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
        return (ActionEvent e) -> {
            String selectedSubject = (String) userPanel.getSubjectComboBoxCreatePanel().getSelectedItem();
            String title = userPanel.getTitleTextFieldCreatePanel().getText();
            Double price = (Double) userPanel.getPriceSpinnerCreatePanel().getValue();
            String description = userPanel.getDescriptionTextArea().getText();

            int selectedSubjectId = subjectMap.get(selectedSubject).getIdMateria();

            if (validateAdCreation(selectedSubject, title, price, description)) {
                if (adService.adSuccessful(user.getIdUsuario(), selectedSubjectId, title, price, description)) {
                    userPanel.setInformationLabelCreatePanel("El anuncio se ha creado correctamente", Color.GREEN);
                    userPanel.clearAllFields();
                    try {
                        loadUserAdsById(user.getIdUsuario());
                    } catch (SQLException ex) {
                        Logger.getLogger(UserPanelController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    userPanel.setInformationLabelCreatePanel("Se ha producido un error al crear el anuncio", Color.RED);
                }
            }
        };
    }

    private ActionListener getDeleteButtonCreatePanelActionListener() {
        return (ActionEvent e) -> {
            userPanel.clearAllFields();
        };
    }

    private ActionListener getExitButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }

    public void populateSubjectComboBox(List<Materia> materias) {
    DefaultComboBoxModel<String> subjectModel = new DefaultComboBoxModel<>();

    materias.forEach(materia -> {
        subjectModel.addElement(materia.getNombre());
        subjectMap.put(materia.getIdMateria(), materia); // mapear por ID
    });

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

    public void loadUserAdsById(int idUsuario) throws SQLException {
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
                        try {
                            loadUserAdsById(idUsuario);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
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
                    try {
                        loadUserAdsById(idUsuario);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
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
                    try {
                        loadUserAdsById(idUsuario);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        JPanel panelAnuncios = userAdsView.createUserAdsPanel(anuncios, subjectMap, onEdit, onDelete, onToggleState);

        userPanel.getEditScrollPane().setViewportView(panelAnuncios != null && !anuncios.isEmpty()
                ? panelAnuncios
                : new JLabel("No hay anuncios disponibles"));
    }

    public void addSearchButtonListener() {
        userPanel.getSearchButtonSearchPanel().addActionListener(e -> {
            Map<String, Object> filtros = new HashMap<>();
            String materiaSeleccionada = (String) userPanel.getSubjectComboBoxCreatePanel().getSelectedItem();
            Materia materiaSeleccionadaObj = subjectMap.get(materiaSeleccionada);
            
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
            
            List<AnuncioDTO> anuncios = adService.fetchAdsFilteredDTO(filtros);
            JPanel resultsPanel = searchAdDialog.createCardsPanel(anuncios);
            userPanel.getSearchScrollPane().setViewportView(resultsPanel);
            
            resultsPanel.revalidate();
            resultsPanel.repaint();
        });
    }

    private void populateAddressData(List<AutonomousCommunity> autonomousCommunitiesData) {
        DefaultComboBoxModel<String> provinceModel = new DefaultComboBoxModel<>();
        List<String> provinceNames = autonomousCommunitiesData.stream()
                .flatMap(community -> community.getProvinces().stream())
                .map(Province::getLabel)
                .sorted()
                .toList();
        provinceModel.addAll(provinceNames);
        userPanel.getProvinceComboBoxSearchPanel().setModel(provinceModel);

    }

    private void addProvinceComboBoxListener() {  //todo añadir las coordenadas a los ayuntamiento que faltan
        userPanel.getProvinceComboBoxSearchPanel().addActionListener(e -> {
            String selectedProvince = (String) userPanel.getProvinceComboBoxSearchPanel().getSelectedItem();

            if (selectedProvince != null) {
                userPanel.enableTownComboboxSearchPanel(true);
                List<String> towns = autonomousCommunities.stream()
                        .flatMap(c -> c.getProvinces().stream())
                        .filter(p -> p.getLabel().equals(selectedProvince))
                        .flatMap(p -> p.getTowns().stream())
                        .map(Town::getLabel)
                        .sorted()
                        .toList();

                DefaultComboBoxModel<String> townModel = new DefaultComboBoxModel<>();
                townModel.addAll(towns);
                userPanel.getTownComboBoxSearchPanel().setModel(townModel);
            }
        });
    }
}
