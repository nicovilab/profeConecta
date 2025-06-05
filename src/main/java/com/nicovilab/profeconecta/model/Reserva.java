/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.model;

import java.sql.Time;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Nico
 */
@Data
public class Reserva {
  private Integer idReserva;
  private Integer usuarioProfesor;
  private Integer usuarioEstudiante;
  private String estado;
  private LocalDate fechaSolicitud;
  private LocalDate fecha;
  private Time horaInicio;
  private Time horaFin;
  private boolean disponible;
}
