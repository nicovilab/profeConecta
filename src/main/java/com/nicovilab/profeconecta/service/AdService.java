/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import com.nicovilab.profeconecta.model.Anuncio;
import java.util.List;

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
    
    public List<Anuncio> fetchUserAdsById(int userId) {
        databaseService = new DatabaseService();
        return databaseService.fetchUserAdsById(userId);
    }

    public boolean updateAd(Anuncio anuncio) {
        databaseService = new DatabaseService();
        return databaseService.updateAd(anuncio);
    }

    public boolean deleteAd(int adId) {
        databaseService = new DatabaseService();
        return databaseService.deleteAd(adId);
    }
}
