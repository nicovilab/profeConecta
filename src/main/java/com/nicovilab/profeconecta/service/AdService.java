/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import com.nicovilab.profeconecta.model.Anuncio;
import com.nicovilab.profeconecta.model.AnuncioDetail;
import com.nicovilab.profeconecta.model.Usuario;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nico
 */
public class AdService {

    DatabaseService databaseService;
    
    public AdService() {
         databaseService = new DatabaseService();
    }

    public boolean adSuccessful(int userId, int subjectId, String title, double price, String description) {
        return databaseService.adSuccessful(userId, subjectId, title, price, description);
    }
    
    public List<Anuncio> fetchUserAdsById(int userId) {
        return databaseService.fetchUserAdsById(userId);
    }
    
    public List<Anuncio> fetchAllAds() {
        return databaseService.fetchAllAds();
    }
    
    public List<AnuncioDetail> fetchAdsFilteredDTO(Map<String, Object> filtros) {
        return databaseService.fetchAdsFilteredDTO(filtros);
    }

    public boolean userHasDirection(int userId){
        return databaseService.userHasLocation(userId);
    }
    public boolean updateAd(Anuncio anuncio) {
        return databaseService.updateAd(anuncio);
    }

    public boolean deleteAd(int adId) {
        return databaseService.deleteAd(adId);
    }
    
    public Usuario getUsuarioById(int userId) throws SQLException {
        return databaseService.getUserById(userId);
    }
}
