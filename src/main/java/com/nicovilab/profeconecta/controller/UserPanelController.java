/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Materia;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.AdService;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.service.ProfileService;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.UserPanel;
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

    public UserPanelController(MainJFrame view, UserPanel userPanel, Usuario user) {
        profileService = new ProfileService();
        adService = new AdService();
        databaseService = new DatabaseService();
        this.view = view;
        this.userPanel = userPanel;
        this.user = user;
        this.materias = databaseService.getSubjects();

        userPanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        userPanel.addExitButtonActionListener(this.getExitButtonActionListener());
        userPanel.addCreateButtonCreatePanelActionListener(this.getCreateButtonCreatePanelActionListener());
        userPanel.addDeleteButtonCreatePanelActionListener(this.getDeleteButtonCreatePanelActionListener());

        populateSubjectComboBox(materias);
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

}
