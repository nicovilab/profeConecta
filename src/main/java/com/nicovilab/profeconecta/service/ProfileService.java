/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import com.nicovilab.profeconecta.model.Direccion;

/**
 *
 * @author Nico
 */
public class ProfileService {
     DatabaseService databaseService;

    public Direccion profileInfoSuccessful(String username) {
        databaseService = new DatabaseService();

        return databaseService.profileInfoSuccessful(username);
    }
}
