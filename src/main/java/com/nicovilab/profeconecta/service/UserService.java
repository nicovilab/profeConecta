/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.Valoracion;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Nico
 */
public class UserService {
    DatabaseService databaseService;
    
    public UserService() {
        databaseService = new DatabaseService();
    }
    
    public Usuario getUserById(int userId) throws SQLException {
        return databaseService.getUserById(userId);
    }
    
    public List<Valoracion> getUserReviews(int idUsuario) throws SQLException{
        return databaseService.getUserReviews(idUsuario);
    }
    
    
    public boolean insertReview(int reviewedUserId, int reviewerUserId, int rating, String comment, LocalDate date){
        return databaseService.insertReview(reviewedUserId, reviewerUserId, rating, comment, date);
    }
}
