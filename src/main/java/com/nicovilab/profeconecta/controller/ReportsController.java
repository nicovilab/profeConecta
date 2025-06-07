/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.controller;

import com.nicovilab.profeconecta.model.Usuario;
import com.nicovilab.profeconecta.service.ReportService;
import static com.nicovilab.profeconecta.utils.Utils.getProvinceNamesModel;
import com.nicovilab.profeconecta.view.MainJFrame;
import com.nicovilab.profeconecta.view.ReportsPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nico
 */
public class ReportsController {

    private final MainJFrame view;
    private final ReportsPanel reportsPanel;
    private final Usuario user;
    private final ReportService reportService;

    public ReportsController(MainJFrame view, ReportsPanel reportsPanel, Usuario user) {
        this.view = view;
        this.reportsPanel = reportsPanel;
        this.user = user;
        this.reportService = new ReportService();

        reportsPanel.addProfileButtonActionListener(this.getProfileButtonActionListener());
        reportsPanel.addExitButtonActionListener(this.getExitButtonActionListener());
        reportsPanel.addCalendarButtonActionListener(this.getCalendarButtonActionListener());
        reportsPanel.addChatButtonActionListener(this.getChatButtonActionListener());
        reportsPanel.addAdButtonActionListener(this.getAdButtonActionListener());
        reportsPanel.addCreateReportButtonActionListener(this.getCreateReportButtonActionListener());

        reportsPanel.getProvinceComboBox().setModel(getProvinceNamesModel(true));
    }

    // Listeners de los botones
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

    private ActionListener getChatButtonActionListener() {
        return (ActionEvent e) -> {
            view.showPanel("chatpanel");
            new ChatController(view, view.getChatPanel(), user);
        };
    }

    private ActionListener getExitButtonActionListener() {
        return (ActionEvent e) -> {
            view.resetUserSession();
        };
    }

    // Genera un reporte de número de anuncios por ayuntamiento de una provincia seleccionada en el comboBox
    private ActionListener getCreateReportButtonActionListener() {
        return (ActionEvent e) -> {
            reportService.createAdsNumberByProvinceReport(reportsPanel.getProvinceComboBox().getSelectedItem().toString());
        };
    }

}
