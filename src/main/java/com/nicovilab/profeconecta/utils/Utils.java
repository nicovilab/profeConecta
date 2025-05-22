/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicovilab.profeconecta.model.address.AutonomousCommunity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * @author Nico
 */
public class Utils {

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
}
