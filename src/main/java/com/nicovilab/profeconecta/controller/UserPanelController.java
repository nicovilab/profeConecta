/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Anuncio;
import com.nicovilab.profeconecta.model.Materia;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.AdService;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.service.ProfileService;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.UserPanel;
import com.nicovilab.profeconecta.view.userAdsView.EditAdDialog;
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

    public UserPanelController(MainJFrame view, UserPanel userPanel, Usuario user){
        profileService = new ProfileService();
        adService = new AdService();
        databaseService = new DatabaseService();
        userAdsView = new UserAdsView();
        this.view = view;
        this.userPanel = userPanel;
        this.user = user;
        this.materias = databaseService.getSubjects();

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

            int selectedSubjectId = materiaMap.get(selectedSubject).getIdMateria();

            if (validateAdCreation(selectedSubject, title, price, description)) {
                if (adService.adSuccessful(user.getIdUsuario(), selectedSubjectId, title, price, description)) {
                    userPanel.setInformationLabelCreatePanel("El anuncio se ha creado correctamente", Color.GREEN);
                    userPanel.clearAllFields();
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

    private Map<String, Materia> materiaMap = new HashMap<>();

    public void populateSubjectComboBox(List<Materia> materias) {
        DefaultComboBoxModel<String> subjectModel = new DefaultComboBoxModel<>();

        materias.forEach(materia -> {
            subjectModel.addElement(materia.getNombre());
            materiaMap.put(materia.getNombre(), materia);
        });

        userPanel.getSubjectComboBoxCreatePanel().setModel(subjectModel);
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

    // Acción: Editar anuncio
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
                        loadUserAdsById(idUsuario); // Recarga la vista
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            dialog.setVisible(true);
        }
    };

    // Acción: Eliminar anuncio
    ActionListener onDelete = e -> {
        int adId = Integer.parseInt(e.getActionCommand());
        int confirm = JOptionPane.showConfirmDialog(null, "¿Eliminar anuncio?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (adService.deleteAd(adId)) {
                try {
                    loadUserAdsById(idUsuario); // Refresca la lista
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    // Acción: Activar / Desactivar anuncio
    ActionListener onToggleState = e -> {
        JButton source = (JButton) e.getSource();
        int adId = Integer.parseInt(source.getActionCommand());
        int newState = (int) source.getClientProperty("newState");

        Anuncio anuncio = anuncios.stream()
            .filter(a -> a.getIdAnuncio() == adId)
            .findFirst()
            .orElse(null);

        if (anuncio != null) {
            anuncio.setActivo(newState == 1); // asumiendo que ahora es boolean
            if (adService.updateAd(anuncio)) {
                try {
                    loadUserAdsById(idUsuario); // Actualiza vista
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    JPanel panelAnuncios = userAdsView.createUserAdsPanel(anuncios, onEdit, onDelete, onToggleState);

    userPanel.getEditScrollPane().setViewportView(panelAnuncios != null && !anuncios.isEmpty()
        ? panelAnuncios
        : new JLabel("No hay anuncios disponibles"));
}
    

}
