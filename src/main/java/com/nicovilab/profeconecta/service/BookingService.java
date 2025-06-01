/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import com.nicovilab.profeconecta.model.Reserva;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Nico
 */
public class BookingService {
    
    DatabaseService databaseService;
    public List<Reserva> getUserBookings(int userId) {
        databaseService = new DatabaseService();
        return databaseService.getAllBookingsByUser(userId);
    }

    public boolean addBooking(int userId, LocalDate date, String startTime, String endTime) {
        databaseService = new DatabaseService();
        return databaseService.insertOpenBooking(userId, date, startTime, endTime);
    }

    public boolean removeBooking(int userId, int bookingId) {
        databaseService = new DatabaseService();
        return databaseService.deleteOpenBooking(userId, bookingId);
    }
}
