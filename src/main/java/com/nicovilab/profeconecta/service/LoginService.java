/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.service;

/**
 *
 * @author Nico
 */
public class LoginService {
    
    public boolean loginSuccessful(String username, String password){
       return username.equals("paco") && password.equals("123");
           
    }
    }
