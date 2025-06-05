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
public class AnuncioDetail {

    //Datos del anuncio
    private int idAnuncio;
    private int idUsuario;
    private int idMateria;
    private String titulo;
    private String descripcion;
    private BigDecimal precioHora;
    private Date fechaPublicacion;
    private boolean activo;

    //Datos del anunciante
    private String usuarioNombre;
    private String usuarioApellidos;
    private byte[] usuarioFotoPerfil;
    private String usuarioDescripcion;
    private BigDecimal valoracionMedia;
    private Long totalValoraciones;

    //Localizacion del anunciante
    private String usuarioProvincia;
    private String usuarioMunicipio;

    //Datos de la materia
    private String materiaNombre;
}
