/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

/**
 *
 * @author Nico
 */
public class AdService {

    DatabaseService databaseService;

    public boolean adSuccessful(int userId, int subjectId, String title, double price, String description) {
        databaseService = new DatabaseService();
        return databaseService.adSuccessful(userId, subjectId, title, price, description);
    }
}
