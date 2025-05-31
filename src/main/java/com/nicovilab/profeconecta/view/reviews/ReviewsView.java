/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view.reviews;

import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.Valoracion;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.service.UserService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
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
public class ReviewsView {

    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Dimension PHOTO_SIZE = new Dimension(50, 50);
    private static final Font RATING_FONT = new Font("Arial", Font.BOLD, 14);
    private final UserService userService;

    public ReviewsView(UserService userService) {
        this.userService = userService;
    }

    public JPanel createReviewsPanel(List<Valoracion> valoraciones) throws SQLException {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);

        for (Valoracion valoracion : valoraciones) {
            JPanel reviewPanel = createSingleReviewPanel(valoracion);
            reviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, reviewPanel.getPreferredSize().height));
            contentPanel.add(reviewPanel);
        }

        return contentPanel;
    }

    private JPanel createSingleReviewPanel(Valoracion valoracion) throws SQLException {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(BACKGROUND_COLOR);

        Usuario valorador = userService.getUserById(valoracion.getUsuarioValorador());
        String nombreCompleto = (valorador != null)
                ? valorador.getNombre() + " " + valorador.getApellidos() : "Usuario desconocido";

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(new JLabel(nombreCompleto), BorderLayout.WEST);
        headerPanel.add(createRatingPanel(valoracion.getPuntuacion()), BorderLayout.EAST);
        headerPanel.setBackground(BACKGROUND_COLOR);

        JPanel bodyPanel = new JPanel(new BorderLayout(10, 0));
        bodyPanel.add(createUserPhotoComponent(valorador), BorderLayout.WEST);
        bodyPanel.add(createCommentComponent(valoracion.getComentario()), BorderLayout.CENTER);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(bodyPanel, BorderLayout.CENTER);
        return panel;
    }

    private JComponent createUserPhotoComponent(Usuario usuario) {
        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(PHOTO_SIZE);

        try {
            if (usuario != null && usuario.getFotoPerfil() != null && usuario.getFotoPerfil().length > 0) {
                ImageIcon originalIcon = new ImageIcon(usuario.getFotoPerfil());
                Image scaledImage = originalIcon.getImage()
                        .getScaledInstance(PHOTO_SIZE.width, PHOTO_SIZE.height, Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                photoLabel.setOpaque(true);
            }
        } catch (Exception e) {
            photoLabel.setText("Foto");
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }

        return photoLabel;
    }

    private JComponent createCommentComponent(String comentario) {
        JTextArea textArea = new JTextArea(comentario);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(null);
        textArea.setBorder(null);
        textArea.setFocusable(false);
        return textArea;
    }

    private JPanel createRatingPanel(int puntuacion) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        JLabel ratingLabel = new JLabel(String.valueOf(puntuacion));
        ratingLabel.setFont(RATING_FONT);

        JLabel star = new JLabel("★");
        star.setForeground(Color.ORANGE);

        panel.add(ratingLabel);
        panel.add(star);

        return panel;
    }
}
