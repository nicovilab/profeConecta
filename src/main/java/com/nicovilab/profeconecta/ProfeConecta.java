/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.nicovilab.profeconecta;

import com.nicovilab.profeconecta.controller.LoginController;
import com.nicovilab.profeconecta.controller.SignUpController;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.view.MainJFrame;

/**
 *
 * @author Nico
 */
public class ProfeConecta {

    public static void main(String[] args) {
        new DatabaseService();
        MainJFrame mainView = new MainJFrame();
        new LoginController(mainView, mainView.getLoginPanel());
        new SignUpController(mainView, mainView.getSignUpPanel());
    }
}
