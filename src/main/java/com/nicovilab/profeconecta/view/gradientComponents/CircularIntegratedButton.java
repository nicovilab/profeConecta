/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nicovilab.profeconecta.view.gradientComponents;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Botón circular transparente que se integra con fondos con gradiente y
 * componentes como ImageAvatar
 *
 * @author Nico
 */
public class CircularIntegratedButton extends JButton {

    private Color hoverColor = new Color(255, 255, 255, 70);
    private Color pressedColor = new Color(0, 0, 0, 40);
    private Color selectedColor = new Color(255, 255, 255, 60);
    private Color textColor = Color.WHITE;
    private Color selectedTextColor = Color.WHITE;
    private Color borderColor = new Color(255, 255, 255, 0);
    private Color selectedBorderColor = new Color(255, 255, 255, 0);
    private boolean isHover = false;
    private boolean isPressed = false;
    private boolean isSelected = false;
    private boolean drawBorder = true;
    private Icon icon = null;

    public CircularIntegratedButton() {
        this("");
    }

    public CircularIntegratedButton(String text) {
        super(text);
        setupButton();

    }

    public CircularIntegratedButton(Icon icon) {
        this.icon = icon;
        setIcon(icon);
    }

    private void setupButton() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(textColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));

        int size = 40;
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));

        setBorder(new EmptyBorder(5, 5, 5, 5));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    isHover = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected) {
                    isHover = false;
                    isPressed = false;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!isSelected) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isSelected) {
                    isPressed = false;
                    repaint();
                }
            }
        });
    }

    /**
     * Establece el tamaño del botón circular
     *
     * @param size tamaño en píxeles (ancho y alto serán iguales)
     */
    public void setCircleSize(int size) {
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setSize(new Dimension(size, size));
    }

    /**
     * Establece si el botón está en estado seleccionado (activo)
     *
     * @param selected true si el botón debe mostrarse como seleccionado
     */
    public void setSelected(boolean selected) {
        if (this.isSelected != selected) {
            this.isSelected = selected;

            // Actualizar el color del texto según el estado
            if (isSelected) {
                setForeground(selectedTextColor);
            } else {
                setForeground(textColor);
            }

            repaint();
        }
    }

    /**
     * Verifica si el botón está actualmente seleccionado
     *
     * @return true si el botón está seleccionado
     */
    public boolean isSelected() {
        return isSelected;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
        if (!isSelected) {
            setForeground(color);
        }
    }

    public void setSelectedTextColor(Color color) {
        this.selectedTextColor = color;
        if (isSelected) {
            setForeground(color);
        }
    }

    public void setHoverColor(Color color) {
        this.hoverColor = color;
    }

    public void setPressedColor(Color color) {
        this.pressedColor = color;
    }

    public void setSelectedColor(Color color) {
        this.selectedColor = color;
        if (isSelected) {
            repaint();
        }
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        if (!isSelected) {
            repaint();
        }
    }

    public void setSelectedBorderColor(Color color) {
        this.selectedBorderColor = color;
        if (isSelected) {
            repaint();
        }
    }

    public void setDrawBorder(boolean draw) {
        this.drawBorder = draw;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight());
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        if (isSelected) {
            g2d.setColor(selectedColor);
            g2d.fillOval(x, y, size, size);
        } else if (isPressed) {
            g2d.setColor(pressedColor);
            g2d.fillOval(x, y, size, size);
        } else if (isHover) {
            g2d.setColor(hoverColor);
            g2d.fillOval(x, y, size, size);
        }

        if (drawBorder) {
            if (isSelected) {
                g2d.setColor(selectedBorderColor);
                g2d.setStroke(new BasicStroke(1.5f));
            } else {
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
            }
            g2d.drawOval(x, y, size - 1, size - 1);
        }

        g2d.dispose();

        if (icon != null && getText().isEmpty()) {
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int iconX = (getWidth() - iconWidth) / 2;
            int iconY = (getHeight() - iconHeight) / 2;
            icon.paintIcon(this, g, iconX, iconY);
        } else {
            super.paintComponent(g);
        }
    }

    public void setSpecialSelectionStyle() {
        setSelectedColor(new Color(0, 0, 0, 40));
        setSelectedTextColor(Color.WHITE);
    }

    /**
     * Sobreescribe la forma del botón para detectar clicks solamente en el área
     * circular
     */
    @Override
    public boolean contains(int x, int y) {
        int size = Math.min(getWidth(), getHeight());
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return distance <= size / 2;
    }
}
