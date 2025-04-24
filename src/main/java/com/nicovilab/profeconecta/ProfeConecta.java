/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.nicovilab.profeconecta;

import com.nicovilab.profeconecta.controller.FrontController;
import com.nicovilab.profeconecta.view.MainJFrame;

/**
 *
 * @author Nico
 */
public class ProfeConecta {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        MainJFrame mainView = new MainJFrame();
        FrontController fc = new FrontController(mainView);
        mainView.setVisible(true);
    }
}
