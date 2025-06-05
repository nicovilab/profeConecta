/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Reserva;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.BookingService;
import com.nicovilab.profeconecta.view.BookingPanel;
import com.nicovilab.profeconecta.view.MainJFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nico
 */
public class BookingController {

    private final MainJFrame view;
    private final BookingPanel bookingPanel;
    private final Usuario user;
    private final BookingService bookingService;

    public BookingController(MainJFrame view, BookingPanel bookingPanel, Usuario user) {

        this.view = view;
        this.bookingPanel = bookingPanel;
        this.user = user;
        bookingService = new BookingService();

        bookingPanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        bookingPanel.addAdButtonActionListener(this.getAdButtonActionListener());
        bookingPanel.addChatButtonActionListener(this.getChatButtonActionListener());
        bookingPanel.addExitButtonActionListener(this.getExitButtonActionListener());
        bookingPanel.addBookingButtonActionListener(this.getAddBookingButtonActionListener());
        bookingPanel.addDeleteBookingButtonActionListener(this.getDeleteBookingButtonActionListener());

        loadBookings();
    }

    private ActionListener getProfileButtonActionListener() {
        return (ActionEvent e) -> {
            try {
                new ProfileController(view, view.getProfilePanel(), user);
            } catch (SQLException ex) {
                Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
            }
            view.showPanel("profile");
        };
    }

    private ActionListener getAdButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("userpanel");
        };
    }

    private ActionListener getChatButtonActionListener() {
        return (ActionEvent e) -> {

        };
    }

    private ActionListener getExitButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }

    private ActionListener getAddBookingButtonActionListener() {
        return (ActionEvent e) -> {
            addBooking();
            loadBookings();
        };
    }

    private ActionListener getDeleteBookingButtonActionListener() {
        return (ActionEvent e) -> {
            deleteBooking();
            loadBookings();
        };
    }

    private void loadBookings() {
        showBookings(bookingService.getUserBookings(user.getIdUsuario()));
    }

    private void addBooking() {
    var dateTimePicker = bookingPanel.getDateTimePicker();
    var date = dateTimePicker.getDatePicker().getDate();
    var startTime = dateTimePicker.getTimePicker().getTime();

    if (date == null || startTime == null) {
        bookingPanel.setInformationTextField("Selecciona fecha y hora para la reserva.", Color.RED);
        return;
    }

    var endTime = startTime.plusHours(1);

    boolean success = bookingService.addBooking(user.getIdUsuario(), date, startTime.toString(), endTime.toString());
    if (success) {
        bookingPanel.setInformationTextField("Reserva añadida correctamente.", Color.GREEN.darker());
        loadBookings();
    } else {
        bookingPanel.setInformationTextField("Error al añadir la reserva.", Color.RED);
    }
}

private void deleteBooking() {
    int selectedRow = bookingPanel.getBookingTable().getSelectedRow();
    if (selectedRow == -1) {
        bookingPanel.setInformationTextField("Selecciona una reserva para eliminar.", Color.RED);
        return;
    }

    int bookingId = (int) bookingPanel.getBookingTable().getValueAt(selectedRow, 0);
    String disponible = (String) bookingPanel.getBookingTable().getValueAt(selectedRow, 4);

    if (!"Sí".equals(disponible)) {
        bookingPanel.setInformationTextField("Solo se pueden eliminar reservas disponibles.", Color.RED);
        return;
    }

    boolean success = bookingService.removeBooking(user.getIdUsuario(), bookingId);
    if (success) {
        bookingPanel.setInformationTextField("Reserva eliminada correctamente.", Color.GREEN.darker());
        loadBookings();
    } else {
        bookingPanel.setInformationTextField("Error al eliminar la reserva.", Color.RED);
    }
}

    public void showBookings(List<Reserva> reservas) {
        JTable table = bookingPanel.getBookingTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        for (Reserva r : reservas) {
            model.addRow(new Object[]{
                r.getIdReserva(),
                r.getFecha(),
                r.getHoraInicio(),
                r.getHoraFin(),
                r.isDisponible() ? "Sí" : "No"
            });
        }
    }

}
