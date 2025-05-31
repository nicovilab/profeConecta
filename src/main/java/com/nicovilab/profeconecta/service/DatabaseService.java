/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nicovilab.profeconecta.model.Anuncio;
import com.nicovilab.profeconecta.model.AnuncioDTO;
import com.nicovilab.profeconecta.model.Direccion;
import com.nicovilab.profeconecta.model.Materia;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.Valoracion;
import com.nicovilab.profeconecta.model.VistaValoracion;
import com.nicovilab.profeconecta.model.address.Town;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
        } catch (SQLException ex) {
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

    public Usuario getUserById(int userId) throws SQLException {
        PreparedStatement preparedStatement = createQuery("SELECT * FROM USUARIO WHERE id_usuario = ?", userId);

        ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            return resultSetMapper.map(resultSet, Usuario.class).getFirst();
        }
        return null;
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

    public List<Valoracion> getUserReviews(int idUsuario) throws SQLException {
        PreparedStatement preparedStatement = createQuery("SELECT id_valoracion, usuario_valorador, comentario, puntuacion, fecha FROM VALORACION WHERE usuario_valorado = ?", idUsuario);

        ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            return resultSetMapper.map(resultSet, Valoracion.class);
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

    public boolean adSuccessful(int userId, int subjectId, String title, double price, String description) {
        PreparedStatement preparedStatement = createQuery("INSERT INTO ANUNCIO (id_usuario, id_materia, titulo, descripcion, precio_hora, fecha_publicacion, activo) VALUES (?,?, ?, ?, ?, ? , 1)",
                userId, subjectId, title, description, price, new java.sql.Timestamp(System.currentTimeMillis()));

        try {
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public List<Materia> getSubjects() {
        PreparedStatement preparedStatement = createQuery("SELECT id_materia, nombre FROM MATERIA");

        ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            List<Materia> materias = resultSetMapper.map(resultSet, Materia.class);

            if (materias != null) {
                return materias;
            }
        }
        return null;
    }

    public List<Anuncio> fetchUserAdsById(int userId) {
        PreparedStatement preparedStatement = createQuery("SELECT * FROM ANUNCIO WHERE id_usuario = ?", userId);

        ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            List<Anuncio> anuncios = resultSetMapper.map(resultSet, Anuncio.class);

            if (anuncios != null) {
                return anuncios;
            }
        }
        return null;
    }

    public List<Anuncio> fetchAllAds() {
        PreparedStatement preparedStatement = createQuery("SELECT * FROM ANUNCIO");

        ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            List<Anuncio> anuncios = resultSetMapper.map(resultSet, Anuncio.class);

            if (anuncios != null) {
                return anuncios;
            }
        }
        return null;
    }


    public List<AnuncioDTO> fetchAdsFilteredDTO(Map<String, Object> filtros) throws SQLException {
    StringBuilder query = new StringBuilder("""
        SELECT DISTINCT a.id_anuncio, a.id_usuario, a.id_materia, a.titulo, a.descripcion, a.precio_hora, a.fecha_publicacion, a.activo,
                        u.nombre AS usuario_nombre, u.foto_perfil AS usuario_foto_perfil, m.nombre AS materia_nombre
        FROM ANUNCIO a
        JOIN USUARIO u ON a.id_usuario = u.id_usuario
        JOIN MATERIA m ON a.id_materia = m.id_materia
        LEFT JOIN DIRECCION d ON u.id_usuario = d.id_usuario
        LEFT JOIN (
            SELECT usuario_valorado, AVG(puntuacion) AS media
            FROM VALORACION
            GROUP BY usuario_valorado
        ) v ON u.id_usuario = v.usuario_valorado
    """);

    List<String> condiciones = new ArrayList<>();
    List<Object> parametros = new ArrayList<>();

    condiciones.add("a.activo = 1");

    if (filtros.containsKey("userIdReferencia")) {
        parametros.add(filtros.get("userIdReferencia"));
        query.append("""
            JOIN DIRECCION ref_d ON ref_d.id_usuario = ?
        """);
        condiciones.add("""
            (
                6371 * ACOS(
                    COS(RADIANS(ref_d.latitud)) * COS(RADIANS(d.latitud)) *
                    COS(RADIANS(d.longitud) - RADIANS(ref_d.longitud)) +
                    SIN(RADIANS(ref_d.latitud)) * SIN(RADIANS(d.latitud))
                )
            ) <= 50
        """);
    }

    if (filtros.containsKey("id_materia")) {
        condiciones.add("m.id_materia = ?");
        parametros.add(filtros.get("id_materia"));
    }

    if (filtros.containsKey("usuario")) {
        condiciones.add("u.nombre LIKE ?");
        parametros.add("%" + filtros.get("usuario") + "%");
    }

    if (filtros.containsKey("ciudad")) {
        condiciones.add("d.municipio LIKE ?");
        parametros.add("%" + filtros.get("ciudad") + "%");
    }

    if (filtros.containsKey("valoracion")) {
        condiciones.add("v.media >= ?");
        parametros.add((Double) filtros.get("valoracion"));
    }

    if (!condiciones.isEmpty()) {
        query.append(" WHERE ").append(String.join(" AND ", condiciones));
    }

    query.append(" ORDER BY a.fecha_publicacion DESC");

    PreparedStatement preparedStatement = createQuery(query.toString(), parametros.toArray());
    ResultSet resultSet = executeQuery(preparedStatement);

    System.out.println("Consulta SQL: " + query);
    System.out.println("Parámetros: " + Arrays.toString(parametros.toArray()));

    // Aquí asumo que tienes un mapper configurado para AnuncioDTO
    return resultSet != null ? resultSetMapper.map(resultSet, AnuncioDTO.class) : null;
}

    public boolean updateAd(Anuncio anuncio) {
        PreparedStatement preparedStatement = createQuery("""
            UPDATE ANUNCIO 
            SET titulo = ?, descripcion = ?, precio_hora = ?, activo = ? 
            WHERE id_anuncio = ?
        """,
                anuncio.getTitulo(),
                anuncio.getDescripcion(),
                anuncio.getPrecioHora(),
                anuncio.isActivo(),
                anuncio.getIdAnuncio()
        );

        try {
            preparedStatement.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean deleteAd(int adId) {
        PreparedStatement preparedStatement = createQuery("DELETE FROM ANUNCIO WHERE id_anuncio = ?", adId);

        try {
            preparedStatement.execute();
            connection.commit();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean userHasLocation(int userId) {
        PreparedStatement ps = createQuery("""
        SELECT 1 FROM direccion WHERE id_usuario = ?
    """, userId);

        ResultSet rs = executeQuery(ps);
        try {
            return rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ResultSet executeQuery(PreparedStatement query) {
        try {
            return query.executeQuery();
        } catch (SQLException ex) {
            return null;
        }
    }

    private PreparedStatement createQuery(String query, Object... params) {
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

    private PreparedStatement createQuery(String query) {
        try {
            return connection.prepareStatement(query);
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }

}
