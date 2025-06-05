/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view.BookingHighlightPolicy;

import com.nicovilab.profeconecta.model.Reserva;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author Nico
 */
public class AvailableHoursDialog extends JDialog {

    private JList<String> hoursList;
    private JButton bookButton;
    private List<Reserva> availableSlots;

    public AvailableHoursDialog(JDialog view, List<Reserva> availableSlots) {
    super(view, true);
    this.availableSlots = availableSlots;

    setTitle("Horas disponibles");

    DefaultListModel<String> listModel = new DefaultListModel<>();
    for (Reserva slot : availableSlots) {
        listModel.addElement(slot.getHoraInicio() + " - " + slot.getHoraFin());
    }

    hoursList = new JList<>(listModel);
    bookButton = new JButton("Reservar");

    setLayout(new BorderLayout());
    add(new JScrollPane(hoursList), BorderLayout.CENTER);
    add(bookButton, BorderLayout.SOUTH);

    // 🔥 Aquí está el truco
    pack(); // Ajusta el tamaño a los componentes automáticamente
    setLocationRelativeTo(view); // Centra el diálogo respecto al padre
}

    public void setBookingActionListener(ActionListener listener) {
        bookButton.addActionListener(listener);
    }

    public int getSelectedIndex() {
        return hoursList.getSelectedIndex();
    }

    public Reserva getSelectedBooking() {
        int idx = getSelectedIndex();
        if (idx >= 0 && idx < availableSlots.size()) {
            return availableSlots.get(idx);
        }
        return null;
    }
}
