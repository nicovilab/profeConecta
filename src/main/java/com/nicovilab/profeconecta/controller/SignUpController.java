/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.service.LoginService;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.SignUpPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.Timer;

/**
 *
 * @author Nico
 */
public class SignUpController {

    private final MainJFrame view;
    private final SignUpPanel signUpPanel;
    private final LoginService loginService;

    public SignUpController(MainJFrame view, SignUpPanel signUpPanel) {
        this.view = view;
        this.signUpPanel = signUpPanel;
        loginService = new LoginService();
        signUpPanel.addSignInButtonActionListener(this.getSignInButtonActionListener());
        signUpPanel.addRegisterButtonActionListener(this.getRegisterButtonActionListener());
    }

    // Listener del botón
    private ActionListener getSignInButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("login");
            signUpPanel.clearAllFields();
        };
    }

    /* 
     Valida los campos del formulario de registro 
     Si todo está correcto, intenta registrar al usuario 
     Muestra mensaje de éxito o error según el resultado
     */
    private ActionListener getRegisterButtonActionListener() {
        return (ActionEvent e) -> {
            String name = signUpPanel.getNameTextField().getText();
            String surname = signUpPanel.getSurnameTextField().getText();
            String email = signUpPanel.getEmailTextField().getText();
            char[] password = signUpPanel.getPasswordField().getPassword();
            char[] confirmPassword = signUpPanel.getConfirmPasswordField().getPassword();

            if (validateRegister(name, surname, email, password, confirmPassword)) {
                if (loginService.registerSuccessful(name, surname, email, password)) {
                    signUpPanel.setInformationTextField("Se ha registrado el usuario correctamente. Redirigiendo al login", Color.GREEN);
                    delayedViewChanged();
                    signUpPanel.clearAllFields();
                } else {
                    signUpPanel.setInformationTextField("Se ha producido un error al registrar", Color.RED);
                }
            }
        };
    }

    // Validar en el orden original de prioridades
    public boolean validateRegister(String name, String surname, String email, char[] password, char[] confirmPassword) {
        if (name.isEmpty()) {
            signUpPanel.setInformationTextField("El nombre es obligatorio", Color.RED);
            return false;
        }

        if (surname.isEmpty()) {
            signUpPanel.setInformationTextField("El apellido es obligatorio", Color.RED);
            return false;
        }

        if (!email.contains("@")) {
            signUpPanel.setInformationTextField("Email inválido", Color.RED);
            return false;
        }

        if (password.length == 0) {
            signUpPanel.setInformationTextField("La contraseña es obligatoria", Color.RED);
            return false;
        }

        if (!Arrays.equals(password, confirmPassword)) {
            signUpPanel.setInformationTextField("Las contraseñas no coinciden", Color.RED);
            return false;
        }
        return true;
    }

    // Cambia automáticamente al panel de login después de 3 segundos
    private void delayedViewChanged() {
        Timer t = new Timer(3000, (ActionEvent e) -> {
            view.showPanel("login");
        });
        t.setRepeats(false);
        t.start();
    }
}
