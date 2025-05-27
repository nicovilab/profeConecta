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
public class Valoracion {
    private Integer idValoracion;
    private Integer usuarioValorado;
    private Integer usuarioValorador;
    private Integer puntuacion;
    private String comentario;
    private Date fecha;
}
