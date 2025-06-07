/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view.gradientComponents;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Nico
 */
public class JPanelGradient extends JPanel {

    private Color color1;
    private Color color2;

    public JPanelGradient(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
    }

    public JPanelGradient(int r1, int g1, int b1, int r2, int g2, int b2) {
        this.color1 = new Color(r1, g1, b1);
        this.color2 = new Color(r2, g2, b2);
    }

    public void setColor1(Color color) {
        this.color1 = color;
        repaint();
    }

    public void setColor2(Color color) {
        this.color2 = color;
        repaint();
    }

    public void setColor1RGB(int r, int g, int b) {
        this.color1 = new Color(r, g, b);
        repaint();
    }

    public void setColor2RGB(int r, int g, int b) {
        this.color2 = new Color(r, g, b);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, width, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}
