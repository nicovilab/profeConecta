/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.model;

import java.sql.Time;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Nico
 */
@Data
public class ReservaDetail {

    private int idReserva;
    private int idAlumno;
    private int idProfesor;

    // Datos del profesor
    private String nombreProfesor;
    private String apellidosProfesor;
    private String descripcionProfesor;
    private byte[] fotoPerfilProfesor;

    // Datos del alumno
    private String nombreAlumno;
    private String apellidosAlumno;
    
    private Date fechaHora;
    
    // Info de la reserva
    private Date fechaSolicitud;
    private Date fecha;           // Fecha de la clase reservada
    private Time horaInicio;
    private Time horaFin;
}
