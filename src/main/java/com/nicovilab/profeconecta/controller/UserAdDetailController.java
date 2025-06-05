/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.optionalusertools.CalendarListener;
import com.github.lgooddatepicker.zinternaltools.CalendarSelectionEvent;
import com.nicovilab.profeconecta.model.AnuncioDetail;
import com.nicovilab.profeconecta.model.Reserva;
import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.model.Valoracion;
import com.nicovilab.profeconecta.model.VistaValoracion;
import com.nicovilab.profeconecta.service.BookingService;
import com.nicovilab.profeconecta.service.DatabaseService;
import com.nicovilab.profeconecta.service.UserService;
import static com.nicovilab.profeconecta.utils.Utils.roundRating;
import com.nicovilab.profeconecta.view.BookingHighlightPolicy.AvailableHoursDialog;
import com.nicovilab.profeconecta.view.UserAdDetailAddReviewDialog;
import com.nicovilab.profeconecta.view.UserAdDetailDialog;
import com.nicovilab.profeconecta.view.extraSwingComponents.ImageAvatar;
import com.nicovilab.profeconecta.view.reviews.ReviewsView;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Nico
 */
public class UserAdDetailController {

    private final UserAdDetailDialog userAdDetailDialog;
    private final Usuario user;
    private final AnuncioDetail anuncioDTO;
    private final ReviewsView reviewsView;
    private final UserService userService;
    private final DatabaseService databaseService;
    private final BookingService bookingService;

    public UserAdDetailController(JFrame parent, Usuario user, AnuncioDetail anuncioDTO) {
        this.user = user;
        this.anuncioDTO = anuncioDTO;
        userService = new UserService();
        reviewsView = new ReviewsView(userService);
        databaseService = new DatabaseService();
        bookingService = new BookingService();

        this.userAdDetailDialog = new UserAdDetailDialog(parent, true);

        ImageAvatar avatarComponent = userAdDetailDialog.getImageAvatar();

        userAdDetailDialog.setNameTextField(anuncioDTO.getUsuarioNombre());
        userAdDetailDialog.setSurnameTextField(anuncioDTO.getUsuarioApellidos());
        userAdDetailDialog.setDescriptionTextField(anuncioDTO.getDescripcion());
        loadUserImage(avatarComponent, anuncioDTO.getUsuarioFotoPerfil());
        userAdDetailDialog.setProvinceTextField(anuncioDTO.getUsuarioProvincia());
        userAdDetailDialog.setTownTextField(anuncioDTO.getUsuarioMunicipio());
        userAdDetailDialog.enableFields(false);

        userAdDetailDialog.addRateUserButtonActionListener(this.getAddReviewActionListener());
        userAdDetailDialog.addBookingButtonActionListener(this.getAddBookingActionListener());

        if (anuncioDTO.getValoracionMedia() != null) {
            DecimalFormat df = new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.US));
            String mediaFormateada = df.format(anuncioDTO.getValoracionMedia());
            userAdDetailDialog.getRatingLabel().setText(mediaFormateada + " con " + anuncioDTO.getTotalValoraciones() + " valoraciones");
            setStarRating(anuncioDTO.getValoracionMedia());
        } else {
            setStarRating(BigDecimal.ZERO);
        }
        DisableAddReviewOnOwnProfile();
        DisableAddBookingOnOwnProfile();
        loadUserCalendar();
        try {
            cargarReviewsDelAnunciante(anuncioDTO.getIdUsuario());
        } catch (SQLException ex) {
            Logger.getLogger(UserAdDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }

        userAdDetailDialog.setVisible(true);

    }

    private ActionListener getAddReviewActionListener() {
        return (ActionEvent e) -> {
            addReviewDialog();
        };
    }

    private ActionListener getAddBookingActionListener() {
        return (ActionEvent e) -> {
            LocalDate selectedDate = userAdDetailDialog.getCalendarPanel().getSelectedDate();

            if (selectedDate == null) {
                userAdDetailDialog.setInformationTextField("Seleccione una fecha", Color.RED);
                return;
            }

            Map<LocalDate, List<Reserva>> bookingsByDate = bookingService.getBookingsGroupedByDate(anuncioDTO.getIdUsuario());
            List<Reserva> reservas = bookingsByDate.getOrDefault(selectedDate, Collections.emptyList());

            boolean hayDisponibles = reservas.stream().anyMatch(Reserva::isDisponible);

            if (!hayDisponibles) {
                userAdDetailDialog.setInformationTextField("No hay horarios disponibles para ese día", Color.RED);
                return;
            }

            showAvailableHoursDialog(selectedDate);
        };
    }

    private void loadUserImage(ImageAvatar imageAvatar, byte[] fotoPerfilBytes) {
        if (fotoPerfilBytes != null && fotoPerfilBytes.length > 0) {
            ImageIcon icon = new ImageIcon(fotoPerfilBytes);

            Image image = icon.getImage();
            Image resizedImage = image.getScaledInstance(
                    imageAvatar.getWidth(),
                    imageAvatar.getHeight(),
                    Image.SCALE_SMOOTH
            );

            imageAvatar.setIcon(new ImageIcon(resizedImage));
        }
    }

    private void cargarReviewsDelAnunciante(int idUsuarioAnunciante) throws SQLException {
        List<Valoracion> valoraciones = userService.getUserReviews(idUsuarioAnunciante);

        JPanel panelResenas = reviewsView.createReviewsPanel(valoraciones);

        if (valoraciones != null && !valoraciones.isEmpty()) {
            userAdDetailDialog.getReviewsScrollPane().setViewportView(panelResenas);
        } else {
            // Opcional: mostrar mensaje "Sin valoraciones"
            userAdDetailDialog.getReviewsScrollPane().setViewportView(new JLabel("Sin valoraciones. Sé el primero en valorar al usuario"));
        }
    }

    private void setStarRating(BigDecimal valoracionMedia) {
        if (valoracionMedia == null) {
            return;
        }
        // Adaptar el método de roundRating si esperas Double, convertir BigDecimal a double
        double media = valoracionMedia.doubleValue();

        int visibleStars = (int) Math.round(roundRating(media) * 2d);

        for (int i = 1; i < 11; i++) {
            try {
                Method getter = userAdDetailDialog.getClass().getMethod("getStar" + i);
                Object starLabel = getter.invoke(userAdDetailDialog);
                Method setVisible = starLabel.getClass().getMethod("setVisible", boolean.class);
                setVisible.invoke(starLabel, i <= visibleStars);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        userAdDetailDialog.repaint();
    }

    public void addReviewDialog() {
        UserAdDetailAddReviewDialog addReviewDialog = new UserAdDetailAddReviewDialog(userAdDetailDialog);

        addReviewDialog.getSendButton().addActionListener(e -> {
            Integer selectedRating = addReviewDialog.getSelectedRating();

            if (selectedRating == -1) {
                addReviewDialog.setInformationTextField("Seleccione una puntuación", Color.RED);
            } else {
                userService.insertReview(
                        anuncioDTO.getIdUsuario(),
                        user.getIdUsuario(),
                        selectedRating,
                        addReviewDialog.getCommmentTextArea(),
                        LocalDate.now()
                );

                VistaValoracion valoracionActualizada = databaseService.getAverageRating(String.valueOf(anuncioDTO.getIdUsuario()));

                if (valoracionActualizada != null) {
                    anuncioDTO.setValoracionMedia(valoracionActualizada.getValoracionMedia());
                    anuncioDTO.setTotalValoraciones(valoracionActualizada.getTotalValoraciones());
                } else {
                    anuncioDTO.setValoracionMedia(BigDecimal.ZERO);
                }

                addReviewDialog.setInformationTextField("Valoración enviada correctamente.", new Color(0, 128, 0));

                addReviewDialog.dispose();

                try {
                    cargarReviewsDelAnunciante(anuncioDTO.getIdUsuario());

                    DecimalFormat df = new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.US));
                    String mediaFormateada = df.format(anuncioDTO.getValoracionMedia());
                    userAdDetailDialog.getRatingLabel().setText(mediaFormateada + " con " + anuncioDTO.getTotalValoraciones() + " valoraciones");
                    setStarRating(anuncioDTO.getValoracionMedia());
                } catch (SQLException ex) {
                    Logger.getLogger(UserAdDetailController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        addReviewDialog.setVisible(true);
    }

    private void DisableAddReviewOnOwnProfile() {
        if (user.getIdUsuario() == anuncioDTO.getIdUsuario()) {
            userAdDetailDialog.getRateUserButton().setEnabled(false);
            userAdDetailDialog.getRateUserButton().setToolTipText("No te puedes valorar a ti mismo");
        }
    }
    private void DisableAddBookingOnOwnProfile() {
        if (user.getIdUsuario() == anuncioDTO.getIdUsuario()) {
            userAdDetailDialog.getBookingButton().setEnabled(false);
            userAdDetailDialog.getBookingButton().setToolTipText("No te puedes reservar a ti mismo");
        }
    }
    

    private void refreshCalendarHighlighting() {
        Map<LocalDate, List<Reserva>> bookingsByDate = bookingService.getBookingsGroupedByDate(anuncioDTO.getIdUsuario());

        Set<LocalDate> noBookingDates = new HashSet<>();
        Set<LocalDate> fullyBookedDates = new HashSet<>();
        Set<LocalDate> availableBookingDates = new HashSet<>();

        for (LocalDate date : bookingsByDate.keySet()) {
            List<Reserva> bookings = bookingsByDate.get(date);

            boolean allBooked = bookings.stream().allMatch(b -> !b.isDisponible());
            boolean anyAvailable = bookings.stream().anyMatch(b -> b.isDisponible());

            if (allBooked) {
                fullyBookedDates.add(date);
            } else if (anyAvailable) {
                availableBookingDates.add(date);
            } else {
                noBookingDates.add(date);
            }
        }

        userAdDetailDialog.updateCalendarHighlighting(noBookingDates, availableBookingDates, fullyBookedDates);
    }

    public void loadUserCalendar() {
        refreshCalendarHighlighting();
    }

    public void showAvailableHoursDialog(LocalDate selectedDate) {
        Map<LocalDate, List<Reserva>> bookingsByDate = bookingService.getBookingsGroupedByDate(anuncioDTO.getIdUsuario());
        List<Reserva> bookings = bookingsByDate.getOrDefault(selectedDate, Collections.emptyList());

        List<Reserva> availableSlots = bookings.stream()
                .filter(Reserva::isDisponible)
                .collect(Collectors.toList());

        if (availableSlots.isEmpty()) {
            return;
        }

        AvailableHoursDialog dialog = new AvailableHoursDialog(userAdDetailDialog, availableSlots);

        dialog.setBookingActionListener(e -> {
            Reserva selectedBooking = dialog.getSelectedBooking();
            if (bookingService.bookSlot(selectedBooking.getIdReserva(), user.getIdUsuario(), selectedBooking.getFecha())) {
                dialog.dispose();
                refreshCalendarHighlighting();
                userAdDetailDialog.setInformationTextField("Fecha y hora reservada", Color.GREEN);
            }
        });

        dialog.setVisible(true);
    }
}
