package com.nicovilab.profeconecta.view.gradientComponents;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Botón transparente que se integra con fondos con gradiente
 * @author Nico
 */
public class IntegratedButton extends JButton {
    private Color hoverColor = new Color(255, 255, 255, 40);
    private Color pressedColor = new Color(0, 0, 0, 40);
    private Color selectedColor = new Color(255, 255, 255, 60);
    private Color textColor = Color.WHITE;
    private Color selectedTextColor = Color.WHITE;
    private Color borderColor = new Color(255, 255, 255, 0);
    private Color selectedBorderColor = new Color(255, 255, 255, 0);
    private boolean isHover = false;
    private boolean isPressed = false;
    private boolean isSelected = false;
    private int borderRadius = 10;
    private boolean drawBorder = true;
    
    public IntegratedButton(String text, boolean selected) {
    super(text);
    setupButton(); // Primero inicializa la configuración base
    setSelected(selected); // Esto actualiza el estado visual
    if(selected) {
        setSelectedColor(new Color(0, 0, 0, 40));
    }
}
    
    private void setupButton() {
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(textColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Añadir listeners para manejar los efectos visuales
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
     * Establece si el botón está en estado seleccionado (activo)
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
    
    public void setBorderRadius(int radius) {
        this.borderRadius = radius;
    }
    
    public void setDrawBorder(boolean draw) {
        this.drawBorder = draw;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Dibujar efecto según el estado
        if (isSelected) {
            // Estado seleccionado (activo)
            g2d.setColor(selectedColor);
            g2d.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);
        } else if (isPressed) {
            // Estado presionado
            g2d.setColor(pressedColor);
            g2d.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);
        } else if (isHover) {
            // Estado hover
            g2d.setColor(hoverColor);
            g2d.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);
        }
        
        // Dibujar borde sutil
        if (drawBorder) {
            if (isSelected) {
                g2d.setColor(selectedBorderColor);
                g2d.setStroke(new BasicStroke(1.5f)); // Borde más grueso cuando está seleccionado
            } else {
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(1f));
            }
            g2d.drawRoundRect(0, 0, width - 1, height - 1, borderRadius, borderRadius);
        }
        
        g2d.dispose();
        
        // Dibujar el texto
        super.paintComponent(g);
    }
    
    public void setSpecialSelectionStyle() {
    setSelectedColor(new Color(0, 0, 0, 40));
    setSelectedTextColor(Color.WHITE);
}
}