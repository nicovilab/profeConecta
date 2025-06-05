/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Chat;
import com.nicovilab.profeconecta.model.ReservaDetail;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.BookingService;
import com.nicovilab.profeconecta.service.ChatService;
import com.nicovilab.profeconecta.view.ChatDialog;
import com.nicovilab.profeconecta.view.ShowBookingsPanel;
import com.nicovilab.profeconecta.view.ChatPanel;
import com.nicovilab.profeconecta.view.MainJFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 *
 * @author Nico
 */
public class ChatController {

    private final MainJFrame view;
    private final ChatPanel chatPanel;
    private final Usuario user;
    private final BookingService bookingService;
    private final ChatService chatService;

    public ChatController(MainJFrame view, ChatPanel chatPanel, Usuario user) {
        this.view = view;
        this.chatPanel = chatPanel;
        this.user = user;
        bookingService = new BookingService();
        chatService = new ChatService();

        chatPanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        chatPanel.addAdButtonActionListener(this.getAdButtonActionListener());
        chatPanel.addCalendarButtonActionListener(this.getCalendarButtonActionListener());
        chatPanel.addExitButtonActionListener(this.getExitButtonActionListener());

        List<ReservaDetail> bookings = bookingService.fetchBookingDetail(user.getIdUsuario());
        loadBookingsOnPanel(bookings);

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

    private ActionListener getCalendarButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("calendar");
            new BookingController(view, view.getCalendarPanel(), user);
        };
    }

    private ActionListener getExitButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("login");
        };
    }

    private void loadBookingsOnPanel(List<ReservaDetail> reservas) {
        JPanel contentPanel = chatPanel.getBookingsContentPanel();
        contentPanel.removeAll();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        for (ReservaDetail reserva : reservas) {
            ShowBookingsPanel panel = new ShowBookingsPanel(
                    reserva,
                    e -> openChat(Integer.parseInt(e.getActionCommand())),
                    e -> cancelBooking(Integer.parseInt(e.getActionCommand()))
            );
            contentPanel.add(panel);
            contentPanel.add(Box.createVerticalStrut(10));
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void cancelBooking(int bookingId) {
        bookingService.removeBookSlot(bookingId);
        List<ReservaDetail> updatedBookings = bookingService.fetchBookingDetail(user.getIdUsuario());
        loadBookingsOnPanel(updatedBookings);
    }

    private void openChat(int bookingId) {
            List<Chat> messages = chatService.fetchChatMessages(bookingId);
            ReservaDetail booking = bookingService.fetchBookingDetailById(bookingId);

            int otherUserId;
            String otherName;

            if (booking.getIdAlumno() == user.getIdUsuario()) {
                otherUserId = booking.getIdProfesor();
                otherName = booking.getNombreProfesor() + " " + booking.getApellidosProfesor();
            } else {
                otherUserId = booking.getIdAlumno();
                otherName = booking.getNombreAlumno() + " " + booking.getApellidosAlumno();
            }

            ChatDialog dialog = new ChatDialog(view, "Chat con " + otherName);

            for (Chat msg : messages) {
                boolean esMio = msg.getUsuarioEmisor() == user.getIdUsuario();
                String nombreEmisor = esMio ? "Tú" : otherName;
                dialog.addMessage(msg.getContenido(), esMio, nombreEmisor, msg.getFechaHora(), msg.getContenidoArchivo());
            }

            dialog.setChatSendListener(new ChatDialog.ChatSendListener() {
                @Override
                public void onSendText(String text) {
                    try {
                        if (chatService.sendMessage(bookingId, user.getIdUsuario(), otherUserId, text)) {
                            dialog.addMessage(text, true, "Tú", LocalDateTime.now(), null);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void onSendFile(File file) {
                    try {
                        if (chatService.sendFileMessage(bookingId, user.getIdUsuario(), otherUserId, file)) {
                            byte[] contenido = Files.readAllBytes(file.toPath());
                            dialog.addMessage("[Archivo enviado: " + file.getName() + "]", true, "Tú", LocalDateTime.now(), contenido);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            dialog.setVisible(true);
    }

}
