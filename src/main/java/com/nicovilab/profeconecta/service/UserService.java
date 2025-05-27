/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import com.nicovilab.profeconecta.model.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Nico
 */
public class UserService {
    DatabaseService databaseService;
    
    public Usuario getUserById(int userId) throws SQLException {
        databaseService = new DatabaseService();

        return databaseService.getUserById(userId);
    }
}
