/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.service.LoginService;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.SignUpPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Nico
 */
public class SignUpController {
    
    private final MainJFrame view;
    private final SignUpPanel signUpPanel;
    private final LoginService loginService;

    
    public SignUpController(MainJFrame view, SignUpPanel signUpPanel){
        this.view = view;
        this.signUpPanel = signUpPanel;
        loginService = new LoginService();
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
            // todo comprobar todos los campos. El email con comprobar que tiene un @ basta.
            String name = signUpPanel.getNameTextField().getText();
            String surname = signUpPanel.getSurnameTextField().getText();
            String email = signUpPanel.getEmailTextField().getText();
            char[] password = signUpPanel.getPasswordField().getPassword();
            char[] confirmPassword = signUpPanel.getConfirmPasswordField().getPassword();
            
            if(!name.isEmpty() 
                    && !surname.isEmpty()
                    && email.contains("@")
                    && password.length > 0 
                    && Arrays.equals(password, confirmPassword)){
                
            loginService.registerSuccessful(name, surname, email, password);
            view.showPanel("login");
            }
            
            
        };
    }

}