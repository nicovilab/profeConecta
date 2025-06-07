/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import com.nicovilab.profeconecta.model.Chat;
import java.io.File;
import java.util.List;

/**
 *
 * @author Nico
 */
public class ChatService {

    DatabaseService databaseService;

    public ChatService() {
        databaseService = new DatabaseService();
    }

    public List<Chat> fetchChatMessages(int bookingId) {
        return databaseService.fetchChatMessages(bookingId);
    }

    public boolean sendMessage(int idReserva, int idEmisor, int idReceptor, String contenido) {
        return databaseService.sendMessage(idReserva, idEmisor, idReceptor, contenido);
    }

    public boolean sendFileMessage(int idReserva, int idEmisor, int idReceptor, File file) {
        return databaseService.sendFileMessage(idReserva, idEmisor, idReceptor, file);
    }
}
