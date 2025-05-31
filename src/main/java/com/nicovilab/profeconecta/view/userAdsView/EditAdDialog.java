/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view.userAdsView;

import com.nicovilab.profeconecta.model.Anuncio;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.function.Consumer;
import javax.swing.JDialog;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 *
 * @author Nico
 */

public class EditAdDialog extends JDialog {

    public EditAdDialog(Anuncio anuncio, Consumer<Anuncio> onSave) {
        setTitle("Editar anuncio");
        setModal(true);
        setSize(400, 400);
        setLocationRelativeTo(null);

        Color borderColor = new Color(38, 117, 191);
        Border customBorder = new LineBorder(borderColor, 2);

        JTextField titleField = new JTextField(anuncio.getTitulo());
        titleField.setBorder(customBorder);
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        titleField.setMargin(new Insets(5, 5, 5, 5));

        JTextArea descArea = new JTextArea(anuncio.getDescripcion());
        descArea.setRows(6);
        descArea.setBorder(customBorder);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        SpinnerNumberModel priceModel = new SpinnerNumberModel(anuncio.getPrecioHora().doubleValue(), 0.0, Double.MAX_VALUE, 0.1);
        JSpinner priceSpinner = new JSpinner(priceModel);
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(priceSpinner, "#0.00");
        priceSpinner.setEditor(editor);
        priceSpinner.setBorder(customBorder);
        priceSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton saveButton = new JButton("Guardar");
        saveButton.setPreferredSize(new Dimension(100, 25));
        saveButton.addActionListener(e -> {
            anuncio.setTitulo(titleField.getText());
            anuncio.setDescripcion(descArea.getText());
            anuncio.setPrecioHora(BigDecimal.valueOf((Double) priceSpinner.getValue()));
            onSave.accept(anuncio);
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(centerLabel("Título:"));
        panel.add(titleField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(centerLabel("Descripción:"));
        panel.add(descScroll);
        panel.add(Box.createVerticalStrut(10));

        panel.add(centerLabel("Precio:"));
        panel.add(priceSpinner);
        panel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        panel.add(buttonPanel);

        add(panel);
    }

    private JLabel centerLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
