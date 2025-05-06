/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.SignUpPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Nico
 */
public class SignUpController {
    
    private final MainJFrame view;
    
    
    public SignUpController(MainJFrame view, SignUpPanel signUpPanel){
        this.view = view;
        signUpPanel.addSignInButtonActionListener(this.getSignInButtonActionListener());
        signUpPanel.addRegisterButtonActionListener(this.getRegisterButtonActionListener());
    }
    
    private ActionListener getSignInButtonActionListener(){
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }
    
    private ActionListener getRegisterButtonActionListener(){
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }
}
