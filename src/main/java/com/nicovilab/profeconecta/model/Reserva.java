/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.model;

import java.util.Date;
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
  private Date fechaSolicitud;
  private Date fecha;
  private String horaInicio;
  private String horaFin;
  private boolean disponible;
}
