/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nicovilab.profeconecta.model.Direccion;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.VistaValoracion;
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
    
    public Usuario loginSuccessful(String email, char[] password) {
        PreparedStatement preparedStatement = createQuery("SELECT * FROM USUARIO WHERE email = ?",
                email);

        ResultSet resultSet = executeQuery(preparedStatement);

        //todo si el resultado de la verificaicon es correcta devuelvo el objeto entero de usuario
        if (resultSet != null) {
            List<Usuario> result = resultSetMapper.map(resultSet, Usuario.class);

            if (result.size() == 1) {
                BCrypt.Result verification = BCrypt.verifyer().verify(password,
                        result.get(0).getContrasena().toCharArray());
                if (verification.verified) {
                    return result.get(0);
                }
            }
        }

        return null;
    }

    public Direccion profileInfoSuccessful(String email) {
        PreparedStatement preparedStatement = createQuery("SELECT d.* FROM DIRECCION d "
                + "JOIN USUARIO u ON d.id_usuario = u.id_usuario "
                + "WHERE u.email = ?",
                email);

        ResultSet resultSet = executeQuery(preparedStatement);

        //todo si el resultado de la verificaicon es correcta devuelvo el objeto entero de usuario
        if (resultSet != null) {
            List<Direccion> result = resultSetMapper.map(resultSet, Direccion.class);

            if (result.size() == 1) {
                    return result.get(0);
                }
            }

        return null;
    }
    
    
    public boolean registerSuccessful(String name, String surname, String email, String password) {
        PreparedStatement preparedStatement = createQuery("INSERT INTO USUARIO (nombre, apellidos, email, contrasena) VALUES (?, ?, ?, ?)",
                name, surname, email, password);

        try {
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean updateUserInfo(String name, String surname, String number, String province, String town, String address, String description, String email) {
        PreparedStatement preparedStatementUsuario = createQuery("UPDATE USUARIO SET nombre = ?, apellidos = ?, telefono = ?, descripcion = ? WHERE email = ?",
                name, surname, number, description, email);

        PreparedStatement preparedStatementDireccion = createQuery(
                "INSERT INTO DIRECCION (id_usuario, provincia, municipio, direccion) "
                + "SELECT u.id_usuario, ?, ?, ? FROM USUARIO u WHERE u.email = ? "
                + "ON DUPLICATE KEY UPDATE provincia = ?, municipio = ?, direccion = ?",
                province, town, address, email, province, town, address
        );

        try {

            preparedStatementUsuario.execute();
            preparedStatementDireccion.execute();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    

    public VistaValoracion getAverageRating(String userId) {
        PreparedStatement preparedStatement = createQuery("SELECT valoracion_media, total_valoraciones FROM vista_valoracion_media_usuario WHERE usuario_valorado = ?", userId);

        ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            VistaValoracion result = resultSetMapper.map(resultSet, VistaValoracion.class).getFirst();
            return result;
        }
        return new VistaValoracion();
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
