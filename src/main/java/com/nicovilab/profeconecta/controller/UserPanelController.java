/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.ProfileService;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.UserPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Nico
 */
public class UserPanelController {
    private final MainJFrame view;
    private final UserPanel userPanel;
    private final ProfileService profileService;
    private final Usuario user;
    
    public UserPanelController(MainJFrame view, UserPanel userPanel, Usuario user){
        profileService = new ProfileService();
        this.view = view;
        this.userPanel = userPanel;
        this.user = user;
        userPanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        userPanel.addExitButtonActionListener(this.getExitButtonActionListener());
    }
    
    private ActionListener getProfileButtonActionListener() {
        return (ActionEvent e) -> {
            new ProfileController(view, view.getProfilePanel(), user);
            view.showPanel("profile");
            
        };
    }
    
    private ActionListener getExitButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }
}
