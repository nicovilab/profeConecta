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
        System.out.println("Hello World!");
        new DatabaseService();
        MainJFrame mainView = new MainJFrame();

        LoginController loginController = new LoginController(mainView, mainView.getLoginPanel());
        SignUpController signUpController = new SignUpController(mainView, mainView.getSignUpPanel());
    }
}
