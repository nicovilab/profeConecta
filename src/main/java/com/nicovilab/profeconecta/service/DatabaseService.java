/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nicovilab.profeconecta.model.Direccion;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.VistaValoracion;
import com.nicovilab.profeconecta.model.address.Town;
import java.sql.*;
import java.util.List;
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
        PreparedStatement preparedStatement = createQuery("""
                SELECT d.* FROM DIRECCION d
                JOIN USUARIO u ON d.id_usuario = u.id_usuario
                WHERE u.email = ?
                """, email);

        ResultSet resultSet = executeQuery(preparedStatement);

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

    public boolean updateUserInfo(String name, String surname, String number, String province, Town town, String address, String description, String email) {
        PreparedStatement preparedStatementUsuario = createQuery("UPDATE USUARIO SET nombre = ?, apellidos = ?, telefono = ?, descripcion = ? WHERE email = ?",
                name, surname, number, description, email);
        
        PreparedStatement preparedStatementDireccion = createQuery("""
                INSERT INTO DIRECCION (id_usuario, provincia, municipio, direccion, latitud, longitud)
                SELECT u.id_usuario, ?, ?, ?, ?, ? FROM USUARIO u WHERE u.email = ?
                ON DUPLICATE KEY UPDATE provincia = ?, municipio = ?, direccion = ?, latitud = ?, longitud = ?
                """,
                province, town.getLabel(), address, town.getLatitude(), town.getLongitude(), 
                email, province, town.getLabel(), address, town.getLatitude(), town.getLongitude()
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

    public boolean updateUserImage(byte[] imageBytes, String email) {
        String sql = "UPDATE USUARIO SET foto_perfil = ? WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            // Establecer parámetros
            ps.setBytes(1, imageBytes);
            ps.setString(2, email);

            // Ejecutar la actualización
            int rowsAffected = ps.executeUpdate();

            ps.execute();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public List<Usuario> findNearestUsers(int userId) {
        PreparedStatement preparedStatement = createQuery("""
                     SELECT 
                       u.id_usuario,
                       u.nombre,
                       u.apellidos,
                       d.latitud,
                       d.longitud,
                       (
                         6371 * ACOS(
                           COS(RADIANS(ref_d.latitud)) *
                           COS(RADIANS(d.latitud)) *
                           COS(RADIANS(d.longitud) - RADIANS(ref_d.longitud)) +
                           SIN(RADIANS(ref_d.latitud)) * SIN(RADIANS(d.latitud))
                         )
                       ) AS distancia_km
                     FROM USUARIO u
                     JOIN DIRECCION d ON u.id_usuario = d.id_usuario
                     JOIN DIRECCION ref_d ON ref_d.id_usuario = ?
                     WHERE u.id_usuario != ?
                     HAVING distancia_km <= 50
                     ORDER BY distancia_km ASC;
                     """, userId);
        
         ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            return resultSetMapper.map(resultSet, Usuario.class);
        }

        return null;
    }

    public VistaValoracion getAverageRating(String userId) {
        PreparedStatement preparedStatement = createQuery("SELECT valoracion_media, total_valoraciones FROM vista_valoracion_media_usuario WHERE usuario_valorado = ?", userId);

        ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            List<VistaValoracion> result = resultSetMapper.map(resultSet, VistaValoracion.class);
            return result.isEmpty() ? null : result.get(0);
        }
        return null;
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
    
    private PreparedStatement createQuery(String query, int... params) {
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        for (int i = 0; i < params.length; i++) {
            preparedStatement.setInt(i + 1, params[i]);
        }

        return preparedStatement;
    } catch (SQLException ex) {
        throw new RuntimeException("Error preparando la consulta", ex);
    }
}

    private PreparedStatement createQuery(String query) {
        try {
            return connection.prepareStatement(query);
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }
    
}
