/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.model;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Nico
 */
@Data
public class Chat {
    private int idChat;
    private int usuarioEmisor;
    private int usuarioReceptor;
    private Date fechaCreacion;
    private String contenido;
    private LocalDateTime fechaHora;
    private boolean leido;
    private byte[] contenidoArchivo;
    private int idReserva;
}
