/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.model.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Nico
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutonomousCommunity {

    @JsonProperty("parent_code")
    private String parentCode;
    private String label;
    private String code;
    private List<Province> provinces;
}
