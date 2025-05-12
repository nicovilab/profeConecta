/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import at.favre.lib.crypto.bcrypt.BCrypt;


/**
 *
 * @author Nico
 */
public class LoginService {

    DatabaseService databaseService;

    public boolean loginSuccessful(String username, char[] password) {
        databaseService = new DatabaseService();

        return databaseService.loginSuccessful(username, password);
    }
    
    public boolean registerSuccessful(String name, String surname, String email, char[] password) {
        databaseService = new DatabaseService();

        String hashedPassword = BCrypt.withDefaults().hashToString(12, password);
        return databaseService.registerSuccessful(name, surname, email, hashedPassword);
    }
}
