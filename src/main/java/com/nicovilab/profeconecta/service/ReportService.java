/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

/**
 *
 * @author Nico
 */
public class ReportService {

    DatabaseService databaseService;

    public ReportService() {
        databaseService = new DatabaseService();
    }

    public void createAdsNumberByProvinceReport(String provincia) {
        databaseService.createAdsNumberByProvinceReport(provincia);
    }
}
