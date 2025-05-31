/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Nico
 */
@Data
public class Anuncio {
    private int idAnuncio;
    private int idUsuario;
    private int idMateria;
    private String titulo;
    private String descripcion;
    private BigDecimal precioHora;
    private Date fechaPublicacion;
    private boolean activo;
}
