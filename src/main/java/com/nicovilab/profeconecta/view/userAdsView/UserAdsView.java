/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view.userAdsView;

import com.nicovilab.profeconecta.model.Anuncio;
import com.nicovilab.profeconecta.model.Materia;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nico
 */
public class UserAdsView {

    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color PRIMARY_COLOR = new Color(38, 117, 191);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 12);

    public JPanel createUserAdsPanel(List<Anuncio> anuncios, Map<Integer, Materia> materiaMap, ActionListener onEdit, ActionListener onDelete, ActionListener onToggleState) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);

        for (Anuncio anuncio : anuncios) {
            JPanel adPanel = createSingleAdPanel(anuncio, materiaMap, onEdit, onDelete, onToggleState);
            adPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, adPanel.getPreferredSize().height));
            contentPanel.add(adPanel);
        }

        return contentPanel;
    }

    private JPanel createSingleAdPanel(Anuncio anuncio, Map<Integer, Materia> materiaMap, ActionListener onEdit, ActionListener onDelete, ActionListener onToggleState) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel(anuncio.getTitulo());
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);

        String nombreMateria = materiaMap.containsKey(anuncio.getIdMateria())
                ? materiaMap.get(anuncio.getIdMateria()).getNombre()
                : "Desconocida";

        JLabel subjectLabel = new JLabel("Materia: " + nombreMateria);
        subjectLabel.setFont(TEXT_FONT);
        subjectLabel.setForeground(Color.DARK_GRAY);

        JLabel priceLabel = new JLabel("Precio: " + anuncio.getPrecioHora() + "€");
        priceLabel.setFont(TEXT_FONT);
        priceLabel.setForeground(Color.GRAY);

        JLabel descriptionLabel = new JLabel("<html><p style='width:300px'>" + anuncio.getDescripcion() + "</p></html>");
        descriptionLabel.setFont(TEXT_FONT);
        descriptionLabel.setForeground(Color.BLACK);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.add(titleLabel);
        infoPanel.add(subjectLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(descriptionLabel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton editBtn = createStyledButton("Editar", PRIMARY_COLOR);
        editBtn.setActionCommand(String.valueOf(anuncio.getIdAnuncio()));
        editBtn.addActionListener(onEdit);

        JButton deleteBtn = createStyledButton("Eliminar", Color.RED.darker());
        deleteBtn.setActionCommand(String.valueOf(anuncio.getIdAnuncio()));
        deleteBtn.addActionListener(onDelete);

        JButton toggleStateBtn = createStyledButton(anuncio.isActivo() ? "Desactivar" : "Activar", Color.GRAY);
        toggleStateBtn.setActionCommand(String.valueOf(anuncio.getIdAnuncio()));
        toggleStateBtn.putClientProperty("newState", anuncio.isActivo() ? 0 : 1);
        toggleStateBtn.addActionListener(onToggleState);

        buttonsPanel.add(editBtn);
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(toggleStateBtn);

        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(TEXT_FONT);
        return button;
    }
}
