/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.LoginService;
import com.nicovilab.profeconecta.view.LoginPanel;
import com.nicovilab.profeconecta.view.MainJFrame;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Nico
 */
public class LoginController {

    private final MainJFrame view;
    private final LoginService loginService;
    private final LoginPanel loginPanel;

    public LoginController(MainJFrame view, LoginPanel loginPanel) {
        loginService = new LoginService();
        this.loginPanel = loginPanel;
        this.view = view;
        loginPanel.addSignInButtonActionListener(this.getSignInMenuActionListener());
        loginPanel.addSignUpButtonActionListener(this.getSignUpMenuActionListener());

    }

    private ActionListener getSignUpMenuActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("signup");
            loginPanel.clearAllTextFields();
        };
    }

    private ActionListener getSignInMenuActionListener() {
    return (ActionEvent e) -> {
        Usuario usuarioAutenticado = loginService.loginSuccessful(loginPanel.getEmailTextField().getText(), 
                                                                 loginPanel.getPasswordField().getPassword());
        if (usuarioAutenticado != null) {
            new UserPanelController(view, view.getUserPanel(), usuarioAutenticado);
            view.showPanel("userpanel");
            loginPanel.clearAllTextFields();
            System.out.println(usuarioAutenticado);
        } else {
            loginPanel.setInformationTextField("Las credenciales no son válidas", Color.red);
        }
    };
}

}
