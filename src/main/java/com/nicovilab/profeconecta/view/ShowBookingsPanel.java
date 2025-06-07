/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view;

import com.nicovilab.profeconecta.model.ReservaDetail;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Nico
 */
public class ShowBookingsPanel extends JPanel {

    private JButton btnVerChat;
    private JButton btnCancelar;
    private JLabel lblNombreProfesor;
    private JLabel lblFechaClase;
    private JLabel lblFoto;

    public ShowBookingsPanel(ReservaDetail reserva, ActionListener verChatListener, ActionListener cancelarListener) {
        Dimension fixedSize = new Dimension(596, 100);

        setPreferredSize(fixedSize);
        setMaximumSize(fixedSize);
        setMinimumSize(fixedSize);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        setBackground(Color.WHITE);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(64, 64));
        lblFoto.setIcon(escalarImagen(reserva.getFotoPerfilProfesor(), 64, 64));
        add(lblFoto, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);

        String nombreCompleto = reserva.getNombreProfesor() + " " + reserva.getApellidosProfesor();
        lblNombreProfesor = new JLabel("Profesor: " + nombreCompleto);
        lblNombreProfesor.setFont(new Font("Arial", Font.BOLD, 14));

        String fechaTexto = String.format("Clase: %s de %s a %s",
                reserva.getFecha(), reserva.getHoraInicio(), reserva.getHoraFin());
        lblFechaClase = new JLabel(fechaTexto);
        lblFechaClase.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFechaClase.setForeground(Color.GRAY);

        infoPanel.add(lblNombreProfesor);
        infoPanel.add(lblFechaClase);

        add(infoPanel, BorderLayout.CENTER);

        JPanel botonesPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        botonesPanel.setOpaque(false);

        btnVerChat = new JButton("Ver chat");
        btnCancelar = new JButton("Cancelar reserva");

        btnVerChat.setActionCommand(String.valueOf(reserva.getIdReserva()));
        btnCancelar.setActionCommand(String.valueOf(reserva.getIdReserva()));

        btnVerChat.addActionListener(verChatListener);
        btnCancelar.addActionListener(cancelarListener);

        botonesPanel.add(btnVerChat);
        botonesPanel.add(btnCancelar);

        add(botonesPanel, BorderLayout.EAST);
    }

    private ImageIcon escalarImagen(byte[] datosImagen, int ancho, int alto) {
        if (datosImagen == null) {
            return new ImageIcon(new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB));
        }
        try {
            BufferedImage imgOriginal = ImageIO.read(new ByteArrayInputStream(datosImagen));
            Image imgEscalada = imgOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(imgEscalada);
        } catch (IOException e) {
            return new ImageIcon(new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB));
        }
    }
}
