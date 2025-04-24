/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.controller.signUp.SignUpController;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.signUp.SignUpDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Nico
 */
public class FrontController {
    
    private MainJFrame view;
    
    
    public FrontController(MainJFrame view){
        this.view = view;
        this.view.addSignUpButtonActionListener(this.getSignUpMenuActionListener());
    }
    
    
    private ActionListener getSignUpMenuActionListener(){
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUpDialog sud = new SignUpDialog(view, true);
                SignUpController rc = new SignUpController(sud);
                sud.setVisible(true);
               
            }
        };
        return al;
    }
    
}
