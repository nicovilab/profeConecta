/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.model;

import lombok.Data;

/**
 *
 * @author Nico
 */
@Data
public class Direccion {
    private int idDireccion;
    private int idUsuario;
    private String provincia; 
    private String municipio;
    private String codigoPostal;
    private String direccion;
    private double latitud;
    private double longitud;
}
