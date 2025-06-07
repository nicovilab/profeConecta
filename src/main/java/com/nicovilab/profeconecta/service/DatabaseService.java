/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nicovilab.profeconecta.model.Anuncio;
import com.nicovilab.profeconecta.model.AnuncioDetail;
import com.nicovilab.profeconecta.model.Chat;
import com.nicovilab.profeconecta.model.Direccion;
import com.nicovilab.profeconecta.model.Materia;
import com.nicovilab.profeconecta.model.Reserva;
import com.nicovilab.profeconecta.model.ReservaDetail;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.Valoracion;
import com.nicovilab.profeconecta.model.VistaValoracion;
import com.nicovilab.profeconecta.model.address.Town;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import nl.jiankai.mapper.ResultSetMapper;
import nl.jiankai.mapper.ResultSetMapperFactory;

/**
 *
 * @author Nico
 */
public class DatabaseService {

    /*
    En esta clase se encuentran todos los métodos de llamadas a base de datos
    Se está utilizando la librería ResultSetMapper para evitar hacer codigo repetitivo y aumentar la legibilidad
    Se mapea en cada clase correspondiente del modelado
    Estos métodos son llamados en el Service correspondiente a cada controlador para repartir responsabilidades
    y que distintos controladores no tengan acceso a los mismos métodos
     */
    private final Connection connection;
    private final ResultSetMapper resultSetMapper;

    public DatabaseService() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/profeConecta",
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

            ps.setBytes(1, imageBytes);
            ps.setString(2, email);

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
        PreparedStatement preparedStatement = createQuery("SELECT id_valoracion, usuario_valorado, usuario_valorador, comentario, puntuacion, fecha FROM VALORACION WHERE usuario_valorado = ?", idUsuario);

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
        PreparedStatement preparedStatement = createQuery("""
                                                          INSERT INTO ANUNCIO (id_usuario, id_materia, titulo, descripcion, precio_hora, fecha_publicacion, activo) 
                                                          VALUES (?, ?, ?, ?, ?, ?, 1)""",
                userId, subjectId, title, description, price, LocalDateTime.now());

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
            return resultSetMapper.map(resultSet, Anuncio.class);
        }
        return null;
    }

    public List<Anuncio> fetchAllAds() {
        PreparedStatement preparedStatement = createQuery("SELECT * FROM ANUNCIO");

        ResultSet resultSet = executeQuery(preparedStatement);

        if (resultSet != null) {
            return resultSetMapper.map(resultSet, Anuncio.class);
        }
        return null;
    }

    public List<AnuncioDetail> fetchAdsFilteredDTO(Map<String, Object> filtros) {
        StringBuilder query = new StringBuilder("""
            SELECT DISTINCT 
                a.id_anuncio, 
                a.id_usuario, 
                a.id_materia, 
                a.titulo, 
                a.descripcion, 
                a.precio_hora, 
                a.fecha_publicacion, 
                a.activo,

                u.nombre AS usuario_nombre,
                u.apellidos AS usuario_apellidos,
                u.foto_perfil AS usuario_foto_perfil,
                u.descripcion AS usuario_descripcion,

                d.provincia AS usuario_provincia,
                d.municipio AS usuario_municipio,

                v.media AS valoracion_media,
                v.total_valoraciones AS total_valoraciones,

                m.nombre AS materia_nombre

            FROM ANUNCIO a
            JOIN USUARIO u ON a.id_usuario = u.id_usuario
            JOIN MATERIA m ON a.id_materia = m.id_materia
            LEFT JOIN DIRECCION d ON u.id_usuario = d.id_usuario
            LEFT JOIN (
                SELECT usuario_valorado, 
                       AVG(puntuacion) AS media,
                       COUNT(*) AS total_valoraciones
                FROM VALORACION
                GROUP BY usuario_valorado
            ) v ON u.id_usuario = v.usuario_valorado
        """);

        List<String> condiciones = new ArrayList<>();
        List<Object> parametros = new ArrayList<>();

        condiciones.add("a.activo = 1");

        if (Boolean.TRUE.equals(filtros.get("buscarCerca")) && filtros.containsKey("userIdReferencia")) {
            query.append("""
            JOIN DIRECCION ref_d ON ref_d.id_usuario = ?
        """);
            parametros.add(filtros.get("userIdReferencia"));

            condiciones.add("""
            (
                6371 * ACOS(
                    COS(RADIANS(ref_d.latitud)) * COS(RADIANS(d.latitud)) *
                    COS(RADIANS(d.longitud) - RADIANS(ref_d.longitud)) +
                    SIN(RADIANS(ref_d.latitud)) * SIN(RADIANS(d.latitud))
                )
            ) <= 30
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

        if (filtros.containsKey("precio_maximo")) {
            condiciones.add("a.precio_hora <= ?");
            parametros.add(filtros.get("precio_maximo"));
        }

        if (!condiciones.isEmpty()) {
            query.append(" WHERE ").append(String.join(" AND ", condiciones));
        }

        query.append(" ORDER BY a.fecha_publicacion DESC");

        PreparedStatement preparedStatement = createQuery(query.toString(), parametros.toArray());
        ResultSet resultSet = executeQuery(preparedStatement);

        return resultSetMapper.map(resultSet, AnuncioDetail.class);
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

    public boolean insertOpenBooking(int userId, LocalDate date, String startTime, String endTime) {
        PreparedStatement preparedStatement = createQuery("""
        INSERT INTO RESERVA (usuario_profesor, fecha_solicitud, fecha, hora_inicio, hora_fin, disponible)
        VALUES (?, ?, ?, ?, ?, ?)
     """, userId,
                LocalDateTime.now(),
                java.sql.Date.valueOf(date),
                startTime,
                endTime,
                1);

        try {
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean deleteOpenBooking(int userId, int bookingId) {
        PreparedStatement preparedStatement = createQuery("""
        DELETE FROM RESERVA
        WHERE id_reserva = ? AND usuario_profesor = ? AND disponible = 1
    """, bookingId, userId);

        try {
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Reserva> getAllBookingsByUser(int userId) {
        PreparedStatement preparedStatement = createQuery("""
        SELECT * FROM RESERVA
        WHERE usuario_profesor = ?
        ORDER BY fecha, hora_inicio
    """, userId);
        ResultSet resultSet = executeQuery(preparedStatement);
        return resultSetMapper.map(resultSet, Reserva.class);
    }

    public Map<LocalDate, List<Reserva>> getBookingsGroupedByDate(int userId) {
        List<Reserva> reservas = getAllBookingsByUser(userId);
        return reservas.stream()
                .filter(reserva -> reserva.getFecha() != null)
                .collect(Collectors.groupingBy(Reserva::getFecha));
    }

    public boolean bookSlot(int bookingId, int studentUserId, LocalDate date) {
        PreparedStatement preparedStatement = createQuery("""
        UPDATE RESERVA
        SET usuario_estudiante = ?, disponible = 0
        WHERE id_reserva = ? AND fecha = ?
    """, studentUserId, bookingId, java.sql.Date.valueOf(date));

        try {
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean removeBookSlot(int bookingId) {
        PreparedStatement preparedStatement = createQuery("""
        UPDATE RESERVA
        SET usuario_estudiante = NULL, disponible = 1
        WHERE id_reserva = ?
    """, bookingId);

        try {
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean insertReview(int reviewedUserId, int reviewerUserId, int rating, String comment, LocalDate date) {
        PreparedStatement preparedStatement = createQuery("""
        INSERT INTO VALORACION (usuario_valorado, usuario_valorador, puntuacion, comentario, fecha)
        VALUES (?, ?, ?, ?, ?)
    """,
                reviewedUserId,
                reviewerUserId,
                rating,
                comment,
                LocalDateTime.now()
        );

        try {
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public List<Chat> fetchChatMessages(int bookingId) {
        String sql = """
        SELECT 
            id_chat,
            usuario_emisor,
            usuario_receptor,
            fecha_creacion,
            contenido,
            fecha_hora,
            leido,
            contenido_archivo,
            id_reserva
        FROM CHAT
        WHERE id_reserva = ?
        ORDER BY fecha_hora ASC
    """;

        PreparedStatement ps = createQuery(sql, bookingId);
        ResultSet rs = executeQuery(ps);
        return resultSetMapper.map(rs, Chat.class);
    }

    public boolean sendMessage(int idReserva, int idEmisor, int idReceptor, String contenido) {
        PreparedStatement preparedStatement = createQuery("""
        INSERT INTO CHAT (id_reserva, usuario_emisor, usuario_receptor, contenido, fecha_creacion, fecha_hora, leido, contenido_archivo)
        VALUES (?, ?, ?, ?, ?, ?, ?, NULL)
        """,
                idReserva,
                idEmisor,
                idReceptor,
                contenido,
                java.time.LocalDate.now(),
                java.time.LocalDateTime.now(),
                0
        );

        try {
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean sendFileMessage(int idReserva, int idEmisor, int idReceptor, File file) {
        try (FileInputStream fis = new FileInputStream(file)) {

            PreparedStatement preparedStatement = createQuery("""
            INSERT INTO CHAT (id_reserva, usuario_emisor, usuario_receptor, contenido, fecha_creacion, fecha_hora, leido, contenido_archivo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """,
                    idReserva,
                    idEmisor,
                    idReceptor,
                    file.getName(),
                    java.time.LocalDate.now(),
                    java.time.LocalDateTime.now(),
                    0
            );

            preparedStatement.setBinaryStream(8, fis, (int) file.length());
            preparedStatement.execute();

        } catch (IOException | SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    public List<ReservaDetail> fetchBookingDetail(int userId) {
        String sql = """
         SELECT 
            r.id_reserva,
            r.usuario_estudiante AS id_alumno,
            r.usuario_profesor AS id_profesor,
            r.fecha_solicitud,
            r.fecha,
            r.hora_inicio,
            r.hora_fin,

            u_prof.nombre AS nombre_profesor,
            u_prof.apellidos AS apellidos_profesor,
            u_prof.descripcion AS descripcion_profesor,
            u_prof.foto_perfil AS foto_perfil_profesor,

            u_alum.nombre AS nombre_alumno,
            u_alum.apellidos AS apellidos_alumno,

            (SELECT MAX(c2.fecha_hora) FROM CHAT c2 WHERE c2.id_reserva = r.id_reserva) AS fecha_hora

            FROM RESERVA r
            JOIN USUARIO u_prof ON r.usuario_profesor = u_prof.id_usuario
            JOIN USUARIO u_alum ON r.usuario_estudiante = u_alum.id_usuario

            WHERE (r.usuario_estudiante = ? OR r.usuario_profesor = ?) 
              AND r.disponible = 0

            ORDER BY 
                fecha_hora IS NULL,
                fecha_hora DESC;
    """;

        PreparedStatement ps = createQuery(sql, userId, userId);
        ResultSet rs = executeQuery(ps);
        return resultSetMapper.map(rs, ReservaDetail.class);
    }

    public ReservaDetail fetchBookingDetailById(int bookingId) {
        String sql = """
         SELECT 
            r.id_reserva,
            r.usuario_estudiante AS id_alumno,
            r.usuario_profesor AS id_profesor,
            r.fecha_solicitud,
            r.fecha,
            r.hora_inicio,
            r.hora_fin,

            u_prof.nombre AS nombre_profesor,
            u_prof.apellidos AS apellidos_profesor,
            u_prof.descripcion AS descripcion_profesor,
            u_prof.foto_perfil AS foto_perfil_profesor,

            u_alum.nombre AS nombre_alumno,
            u_alum.apellidos AS apellidos_alumno,

            NULL AS fecha_hora

        FROM RESERVA r
        JOIN USUARIO u_prof ON r.usuario_profesor = u_prof.id_usuario
        JOIN USUARIO u_alum ON r.usuario_estudiante = u_alum.id_usuario
        WHERE r.id_reserva = ?
    """;

        PreparedStatement ps = createQuery(sql, bookingId);
        ResultSet rs = executeQuery(ps);
        List<ReservaDetail> lista = resultSetMapper.map(rs, ReservaDetail.class);
        return lista.isEmpty() ? null : lista.get(0);
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

    public void createAdsNumberByProvinceReport(String provincia) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("provincia", provincia);

            InputStream reportStream = getClass().getResourceAsStream("/reports/anuncios_por_provincia.jrxml");

            JasperReport reporte = JasperCompileManager.compileReport(reportStream);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, this.connection);

            JasperExportManager.exportReportToPdfFile(print, "informe_anuncios_por_provincia.pdf");

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Informe de anuncios por provincia");
            viewer.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
