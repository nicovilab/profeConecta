/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.VistaValoracion;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.ProfilePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nico
 */
public class ProfileController {

    private final MainJFrame view;
    private final ProfilePanel profilePanel;

    public ProfileController(MainJFrame view, ProfilePanel profilePanel) {
        this.view = view;
        this.profilePanel = profilePanel;
        profilePanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        profilePanel.addExitButtonActionListener(this.getExitButtonActionListener());
        
        DatabaseService databaseService = new DatabaseService();
        VistaValoracion vistaValoracion = databaseService.getAverageRating("39");
        profilePanel.getRatingLabel().setText(vistaValoracion.getValoracionMedia() + " con " + vistaValoracion.getTotalValoraciones() + " valoraciones");
        setStarRating(vistaValoracion.getValoracionMedia().doubleValue());
    }


    
    

    private ActionListener getProfileButtonActionListener() {
        return (ActionEvent e) -> {

        };
    }

    private ActionListener getExitButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }
    

    private void setStarRating(Double valoracionMedia) {

        int visibleStars = (int) Math.round(roundRating(valoracionMedia)* 2d);

        for (int i = 1; i < 11; i++) {
            boolean shouldBeVisible = i <= visibleStars;
            try { //todo catchear correctamente la excepcion
                Method getter = profilePanel.getClass().getMethod("getStar" + i);
                Object starLabel = getter.invoke(profilePanel);
                Method setVisible = starLabel.getClass().getMethod("setVisible", boolean.class);
                setVisible.invoke(starLabel, shouldBeVisible);
            } catch (Exception ex) {
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        profilePanel.repaint();
    }

    private Double roundRating(Double valoracionMedia) {
        double floor = Math.floor(valoracionMedia);
        int decimal = (int)((valoracionMedia - floor) * 10);
        
        if (decimal >= 0 && decimal < 5) {
            return floor;
        } else if (decimal >= 6 && decimal < 9) {
            return floor + 1;
        } else if (decimal == 5) {
            return valoracionMedia;
        }
        return null;
    }

}
