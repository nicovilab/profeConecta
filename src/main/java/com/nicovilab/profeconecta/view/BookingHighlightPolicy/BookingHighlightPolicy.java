/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view.BookingHighlightPolicy;

/**
 *
 * @author Nico
 */
import com.github.lgooddatepicker.optionalusertools.DateHighlightPolicy;
import com.github.lgooddatepicker.zinternaltools.HighlightInformation;
import java.time.LocalDate;
import java.awt.Color;
import java.util.Set;

public class BookingHighlightPolicy implements DateHighlightPolicy {

    private Set<LocalDate> noBookingDates;      
    private Set<LocalDate> availableBookingDates;  
    private Set<LocalDate> fullyBookedDates;     

    public BookingHighlightPolicy(Set<LocalDate> noBookingDates, Set<LocalDate> availableBookingDates, Set<LocalDate> fullyBookedDates) {
        this.noBookingDates = noBookingDates;
        this.availableBookingDates = availableBookingDates;
        this.fullyBookedDates = fullyBookedDates;
    }

    @Override
    public HighlightInformation getHighlightInformationOrNull(LocalDate date) {
        if (noBookingDates.contains(date)) {
            return new HighlightInformation(Color.GRAY, null, null);
        } else if (fullyBookedDates.contains(date)) {
            return new HighlightInformation(Color.RED, null, null);
        } else if (availableBookingDates.contains(date)) {
            return new HighlightInformation(Color.GREEN, null, null);
        }
        return null;
    }
}
