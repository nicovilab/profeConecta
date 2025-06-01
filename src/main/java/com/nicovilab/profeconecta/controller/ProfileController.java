/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Direccion;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.Valoracion;
import com.nicovilab.profeconecta.model.VistaValoracion;
import com.nicovilab.profeconecta.model.address.AutonomousCommunity;
import com.nicovilab.profeconecta.model.address.Province;
import com.nicovilab.profeconecta.model.address.Town;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.service.ProfileService;
import com.nicovilab.profeconecta.service.UserService;
import static com.nicovilab.profeconecta.utils.Utils.convertFileToBytes;
import static com.nicovilab.profeconecta.utils.Utils.getAutonomousCommunitiesData;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.ProfilePanel;
import com.nicovilab.profeconecta.view.extraSwingComponents.ImageAvatar;
import com.nicovilab.profeconecta.view.reviews.ReviewsView;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Nico
 */
public class ProfileController {

    private final MainJFrame view;
    private final ProfilePanel profilePanel;
    private final DatabaseService databaseService;
    private final ProfileService profileService;
    private final UserService userService;
    private final ReviewsView reviewsView;
    private final List<AutonomousCommunity> autonomousCommunities;

    private Usuario user;
    private final String userEmail;

    public ProfileController(MainJFrame view, ProfilePanel profilePanel, Usuario user) throws SQLException {
        this.view = view;
        this.profilePanel = profilePanel;
        this.userEmail = user.getEmail();
        this.user = user;

        databaseService = new DatabaseService();
        profileService = new ProfileService();
        userService = new UserService();
        reviewsView = new ReviewsView(userService);

        this.autonomousCommunities = getAutonomousCommunitiesData();

        populateAddressData(autonomousCommunities);

        addProvinceComboBoxListener();

        profilePanel.addExitButtonActionListener(this.getExitButtonActionListener());
        profilePanel.addEditButtonActionListener(this.getEditButtonActionListener());
        profilePanel.addSaveButtonActionListener(this.getSaveButtonActionListener());
        profilePanel.addImageAvatarButtonActionListener(this.getImageAvatarButtonActionListener());
        profilePanel.addAdButtonActionListener(this.getAdButtonActionListener());
        profilePanel.addCalendarButtonActionListener(this.getCalendarButtonActionListener());

        VistaValoracion vistaValoracion = databaseService.getAverageRating(String.valueOf(user.getIdUsuario()));

        if (vistaValoracion != null) {
            profilePanel.getRatingLabel().setText(vistaValoracion.getValoracionMedia() + " con " + vistaValoracion.getTotalValoraciones() + " valoraciones");
            setStarRating(vistaValoracion.getValoracionMedia().doubleValue());
        } else {
            setStarRating(0D);
        }

        setUserInfo(user, profileService.fetchUserAddress(user.getEmail()));
        cargarPerfilUsuario(user.getIdUsuario());
        
        profilePanel.enableFields(false);
    }


    private ActionListener getImageAvatarButtonActionListener() {
        return (ActionEvent e) -> {
            File selectedFile = selectImageFile();
            if (selectedFile != null) {
                try {
                    byte[] imageBytes = convertFileToBytes(selectedFile);
                    
                    databaseService.updateUserImage(imageBytes, userEmail);
                    
                    ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image image = icon.getImage().getScaledInstance(
                        profilePanel.getImageAvatar().getWidth(),
                        profilePanel.getImageAvatar().getHeight(),
                        Image.SCALE_SMOOTH
                );
                profilePanel.getImageAvatar().setIcon(new ImageIcon(image));

                } catch (IOException ex) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    private ActionListener getEditButtonActionListener() {
        return (ActionEvent e) -> {
            profilePanel.enableFields(true);
            profilePanel.enableEditButton(false);
            profilePanel.enableSaveButton(true);
            profilePanel.enableTownCombobox(false);
        };
    }

    private ActionListener getSaveButtonActionListener() {
        return (ActionEvent e) -> {

            String selectedTown = profilePanel.getTownComboBox().getItemAt(profilePanel.getTownComboBox().getSelectedIndex());

            Town town = autonomousCommunities.stream()
                    .flatMap(x -> x.getProvinces().stream())
                    .flatMap(x -> x.getTowns().stream())
                    .filter(y -> y.getLabel().equals(selectedTown))
                    .findFirst()
                    .orElse(null);

            databaseService.updateUserInfo(
                    profilePanel.getNameTextField().getText(),
                    profilePanel.getSurnameTextField().getText(),
                    profilePanel.getNumberTextField().getText(),
                    profilePanel.getProvinceComboBox().getItemAt(profilePanel.getProvinceComboBox().getSelectedIndex()),
                    town,
                    profilePanel.getAddressTextField().getText(),
                    profilePanel.getDescriptionTextArea().getText(),
                    userEmail);
            profilePanel.enableFields(false);
            profilePanel.enableEditButton(true);
            profilePanel.enableSaveButton(false);

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

    private void setUserInfo(Usuario user, Direccion address) {
        profilePanel.setNameTextField(user.getNombre());
        profilePanel.setSurnameTextField(user.getApellidos());
        profilePanel.setNumberTextField(user.getTelefono() == null ? null : user.getTelefono());
        profilePanel.setDescriptionTextField(user.getDescripcion() == null ? null : user.getDescripcion());
        if (address != null) {
            profilePanel.setProvinceTextField(address.getProvincia() == null ? null : address.getProvincia());
            profilePanel.setTownTextField(address.getMunicipio() == null ? null : address.getMunicipio());
            profilePanel.setAdressTextField(address.getDireccion() == null ? null : address.getDireccion());
        }
        loadUserImage(user, profilePanel.getImageAvatar());
    }

    private void setStarRating(Double valoracionMedia) {
        int visibleStars = (int) Math.round(roundRating(valoracionMedia) * 2d);

        for (int i = 1; i < 11; i++) {
            boolean shouldBeVisible = i <= visibleStars;
            try { //todo catchear correctamente la excepcion
                Method getter = profilePanel.getClass().getMethod("getStar" + i);
                Object starLabel = getter.invoke(profilePanel);
                Method setVisible = starLabel.getClass().getMethod("setVisible", boolean.class);
                setVisible.invoke(starLabel, shouldBeVisible);
            } catch (Exception ex) {
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        profilePanel.repaint();
    }

    private Double roundRating(Double valoracionMedia) {
        double floor = Math.floor(valoracionMedia);
        int decimal = (int) ((valoracionMedia - floor) * 10);

        if (decimal >= 0 && decimal < 5) {
            return floor;
        } else if (decimal >= 6 && decimal < 9) {
            return floor + 1;
        } else if (decimal == 5) {
            return valoracionMedia;
        }
        return null;
    }

    private File selectImageFile() {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Archivos de imagen", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }

        return null;
    }

    private void loadUserImage(Usuario user, ImageAvatar imageAvatar) {
        if (user != null && user.getFotoPerfil() != null) {
            ImageIcon icon = new ImageIcon(user.getFotoPerfil());

            Image image = icon.getImage();
            Image resizedImage = image.getScaledInstance(
                    imageAvatar.getWidth(),
                    imageAvatar.getHeight(),
                    Image.SCALE_SMOOTH
            );

            imageAvatar.setIcon(new ImageIcon(resizedImage));
        }
    }

    private void populateAddressData(List<AutonomousCommunity> autonomousCommunitiesData) {
        DefaultComboBoxModel<String> provinceModel = new DefaultComboBoxModel<>();
        List<String> provinceNames = autonomousCommunitiesData.stream()
                .flatMap(community -> community.getProvinces().stream())
                .map(Province::getLabel)
                .sorted()
                .toList();
        provinceModel.addAll(provinceNames);
        profilePanel.getProvinceComboBox().setModel(provinceModel);

    }

    private void addProvinceComboBoxListener() {  //todo añadir las coordenadas a los ayuntamiento que faltan
        profilePanel.getProvinceComboBox().addActionListener(e -> {
            String selectedProvince = (String) profilePanel.getProvinceComboBox().getSelectedItem();

            if (selectedProvince != null) {
                profilePanel.enableTownCombobox(true);
                List<String> towns = autonomousCommunities.stream()
                        .flatMap(c -> c.getProvinces().stream())
                        .filter(p -> p.getLabel().equals(selectedProvince))
                        .flatMap(p -> p.getTowns().stream())
                        .map(Town::getLabel)
                        .sorted()
                        .toList();

                DefaultComboBoxModel<String> townModel = new DefaultComboBoxModel<>();
                townModel.addAll(towns);
                profilePanel.getTownComboBox().setModel(townModel);
            }
        });
    }

    public void cargarPerfilUsuario(int idUsuario) throws SQLException {
        List<Valoracion> valoraciones = databaseService.getUserReviews(idUsuario);

        JPanel panelReseñas = reviewsView.createReviewsPanel(valoraciones);

        if (valoraciones != null && !valoraciones.isEmpty()) {
            profilePanel.getReviewsScrollPane().setViewportView(panelReseñas);
        }

    }

}
