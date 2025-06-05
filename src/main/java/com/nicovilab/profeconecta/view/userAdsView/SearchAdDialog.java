/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view.userAdsView;

import com.nicovilab.profeconecta.model.AnuncioDetail;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.AdService;
import com.nicovilab.profeconecta.view.MainJFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 *
 * @author Nico
 */
public class SearchAdDialog {

    private static final Dimension PHOTO_SIZE = new Dimension(100, 100);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 16);
    private static final Font USER_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font DESC_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font LABEL_BOLD_FONT = new Font("SansSerif", Font.BOLD, 12);
    private static final Color TITLE_COLOR = new Color(38, 117, 191);
    private static final Color BORDER_COLOR = new Color(38, 117, 191);
    private static final Color BG_COLOR = Color.WHITE;

    public JPanel createCardsPanel(List<AnuncioDetail> anuncios) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_COLOR);

        for (AnuncioDetail anuncio : anuncios) {
            JPanel card = createAdCardPanel(anuncio);
            contentPanel.add(card);
            contentPanel.add(Box.createVerticalStrut(10));
        }

        return contentPanel;
    }

    public JPanel createAdCardPanel(AnuncioDetail anuncio) {
        JPanel card = new JPanel(new BorderLayout(15, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createLineBorder(BORDER_COLOR)
        ));
        card.setBackground(BG_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Panel izquierdo (título, nombre, foto)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BG_COLOR);
        leftPanel.setPreferredSize(new Dimension(220, 150));

        // Subpanel arriba a la izquierda: título y nombre de usuario
        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS));
        topLeftPanel.setBackground(BG_COLOR);

        JLabel titulo = new JLabel(anuncio.getTitulo());
        titulo.setFont(TITLE_FONT);
        titulo.setForeground(TITLE_COLOR);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel usuarioLabel = new JLabel(anuncio.getUsuarioNombre());
        usuarioLabel.setFont(USER_FONT);
        usuarioLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topLeftPanel.add(titulo);
        topLeftPanel.add(Box.createVerticalStrut(2));
        topLeftPanel.add(usuarioLabel);

        // Foto
        JPanel centerLeftPanel = new JPanel();
        centerLeftPanel.setBackground(BG_COLOR);
        centerLeftPanel.setLayout(new BoxLayout(centerLeftPanel, BoxLayout.Y_AXIS));
        centerLeftPanel.add(Box.createVerticalStrut(10));

        JComponent photoComponent = createUserPhotoComponent(anuncio.getUsuarioFotoPerfil());
        photoComponent.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerLeftPanel.add(photoComponent);
        centerLeftPanel.add(Box.createVerticalGlue());

        leftPanel.add(topLeftPanel, BorderLayout.NORTH);
        leftPanel.add(centerLeftPanel, BorderLayout.CENTER);

        // Panel derecho (materia + fecha alineados, descripción)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(BG_COLOR);

        // Encabezado: Materia y Fecha en columna, bien alineados con la descripción
        JPanel headerRight = new JPanel();
        headerRight.setLayout(new BoxLayout(headerRight, BoxLayout.Y_AXIS));
        headerRight.setBackground(BG_COLOR);
        headerRight.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel materiaLabel = new JLabel("Materia: " + anuncio.getMateriaNombre());
        materiaLabel.setFont(LABEL_BOLD_FONT);
        materiaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel fechaLabel = new JLabel("Fecha creación: " + new SimpleDateFormat("dd/MM/yyyy").format(anuncio.getFechaPublicacion()));
        fechaLabel.setFont(LABEL_BOLD_FONT);
        fechaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerRight.add(materiaLabel);
        headerRight.add(Box.createVerticalStrut(2));
        headerRight.add(fechaLabel);

        // Descripción
        JTextArea descripcion = new JTextArea(anuncio.getDescripcion());
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setEditable(false);
        descripcion.setBackground(BG_COLOR);
        descripcion.setFont(DESC_FONT);
        descripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        descripcion.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0)); // espacio entre header y descripción

        rightPanel.add(headerRight);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(descripcion);

        // Ensamblar la card
        card.add(leftPanel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);

        Color originalBackground = card.getBackground();
        Color hoverBackground = originalBackground.darker();

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (adCardClickListener != null) {
                    adCardClickListener.onAdCardClicked(anuncio);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverBackground);
                card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(originalBackground);
                card.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });

        return card;
    }

    private JComponent createUserPhotoComponent(byte[] fotoPerfil) {
        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(PHOTO_SIZE);
        photoLabel.setMinimumSize(PHOTO_SIZE);
        photoLabel.setMaximumSize(PHOTO_SIZE);

        if (fotoPerfil != null && fotoPerfil.length > 0) {
            try {
                ImageIcon originalIcon = new ImageIcon(fotoPerfil);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                        PHOTO_SIZE.width, PHOTO_SIZE.height, Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(scaledImage));
                photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            } catch (Exception e) {
                photoLabel.setText("Error");
                photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                photoLabel.setOpaque(true);
                photoLabel.setBackground(Color.LIGHT_GRAY);
            }
        } else {
            photoLabel.setText("Sin foto");
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoLabel.setOpaque(true);
            photoLabel.setBackground(Color.LIGHT_GRAY);
            photoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }

        return photoLabel;
    }

    public interface AdCardClickListener {

        void onAdCardClicked(AnuncioDetail anuncio);
    }

    private AdCardClickListener adCardClickListener;

    public void setAdCardClickListener(AdCardClickListener listener) {
        this.adCardClickListener = listener;
    }
}
