/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicovilab.profeconecta.model.address.AutonomousCommunity;
import com.nicovilab.profeconecta.model.address.Province;
import com.nicovilab.profeconecta.model.address.Town;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Nico
 */
public class Utils {
    
    public static List<AutonomousCommunity> autonomousCommunities = getAutonomousCommunitiesData();

    public static byte[] convertFileToBytes(File file) throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;

        try {
            fis = new FileInputStream(file);
            baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            return baos.toByteArray();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (baos != null) {
                baos.close();
            }
        }
    }

    public static List<AutonomousCommunity> getAutonomousCommunitiesData() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();

            String s = new String(classloader.getResourceAsStream("masterdata/cities.json").readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(s,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, AutonomousCommunity.class)
            );
        } catch (IOException e) {
            return null;
        }
    }
    
    public static List<String> getTownsByProvince(String selectedProvince, Boolean firstNull) {
        List<String> towns = autonomousCommunities.stream()
            .flatMap(c -> c.getProvinces().stream())
            .filter(p -> p.getLabel().equals(selectedProvince))
            .flatMap(p -> p.getTowns().stream())
            .map(Town::getLabel)
            .sorted()
            .collect(Collectors.toCollection(ArrayList::new));
        
        if(firstNull) {
            towns.addFirst("");
        }
        
        return towns;
    }
    
    public static DefaultComboBoxModel<String> getProvinceNamesModel(Boolean firstNull) {
        DefaultComboBoxModel<String> provinceModel = new DefaultComboBoxModel<>();
        if(firstNull) {
            provinceModel.addElement("");
        }
        
        List<String> provinceNames = autonomousCommunities.stream()
                .flatMap(community -> community.getProvinces().stream())
                .map(Province::getLabel)
                .sorted()
                .toList();
        provinceModel.addAll(provinceNames);
        return provinceModel;
    }
    
    public static double roundRating(double valoracionMedia) {
        double floor = Math.floor(valoracionMedia);
        int decimal = (int) ((valoracionMedia - floor) * 10);

        if (decimal >= 0 && decimal < 5) {
            return floor;
        } else if (decimal >= 6 && decimal < 9) {
            return floor + 1;
        } else if (decimal == 5) {
            return valoracionMedia;
        }
        return floor; // fallback
    }
}
