/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nicovilab.profeconecta.model.Usuario;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.jiankai.mapper.ResultSetMapper;
import nl.jiankai.mapper.ResultSetMapperFactory;

/**
 *
 * @author Nico
 */
public class DatabaseService {
    private final Connection connection;
    private final ResultSetMapper resultSetMapper;
   
    public DatabaseService() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/exmple-database",
                    "root", "my-secret-pw");
            resultSetMapper = ResultSetMapperFactory.getResultSetMapperLowerCaseUnderscore();
        } catch (SQLException ex){
            throw new RuntimeException();
        }
    }
    
    public boolean loginSuccessful(String email, String password) {
        PreparedStatement preparedStatement = createQuery("SELECT * FROM USUARIO WHERE email = ?",
                email);

        ResultSet resultSet = executeQuery(preparedStatement);
        
        if(resultSet != null) {
            List<Usuario> result = resultSetMapper.map(resultSet, Usuario.class);
            
            if(result.size() == 1) {
               BCrypt.Result verification = BCrypt.verifyer().verify(password.toCharArray(), 
                       result.get(0).getContrasena().toCharArray());
               return verification.verified;
            }
        }
            
        return false;
    }
    
    public boolean registerSuccessful(String name, String surname, String email, String password) {
        PreparedStatement preparedStatement = createQuery("INSERT INTO USUARIO (nombre, apellidos, email, contrasena) VALUES (?, ?, ?, ?)",
                name, surname, email, password);
        
        try {
            boolean resultSet = preparedStatement.execute();
            return resultSet;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private ResultSet executeQuery(PreparedStatement query) {
        try {
            return query.executeQuery();
        } catch (SQLException ex) {
            return null;
        }
    }

    private PreparedStatement createQuery(String query, String... params) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            return preparedStatement;
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }
    
}
