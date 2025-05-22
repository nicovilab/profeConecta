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
public class Usuario {
  private Integer idUsuario;
  private String nombre;
  private String apellidos;
  private String email;
  private String contrasena;
  private String telefono;
  private byte[] fotoPerfil;
  private String descripcion;
  private Date fechaRegistro;
  private boolean esAdmin;
}
