/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

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
    
    public UserPanelController(MainJFrame view, UserPanel userPanel){
        this.view = view;
        this.userPanel = userPanel;
        userPanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        userPanel.addExitButtonActionListener(this.getExitButtonActionListener());
    }
    
    private ActionListener getProfileButtonActionListener() {
        return (ActionEvent e) -> {
            ProfileController profileController = new ProfileController(view, view.getProfilePanel());
            view.showPanel("profile");
            
        };
    }
    
    private ActionListener getExitButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }
}
