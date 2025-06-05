/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

import com.nicovilab.profeconecta.model.Reserva;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nico
 */
public class BookingService {
    
    DatabaseService databaseService;
    
    public BookingService() {
        databaseService = new DatabaseService();
    }
    
    public List<Reserva> getUserBookings(int userId) {
        return databaseService.getAllBookingsByUser(userId);
    }
    
    public Map<LocalDate, List<Reserva>> getBookingsGroupedByDate(int userId){
        return databaseService.getBookingsGroupedByDate(userId);
    }
    
    public boolean bookSlot(int bookingId, int studentUserId, LocalDate date){
        return databaseService.bookSlot(bookingId, studentUserId, date);
    }

    public boolean addBooking(int userId, LocalDate date, String startTime, String endTime) {
        return databaseService.insertOpenBooking(userId, date, startTime, endTime);
    }

    public boolean removeBooking(int userId, int bookingId) {
        return databaseService.deleteOpenBooking(userId, bookingId);
    }
}
