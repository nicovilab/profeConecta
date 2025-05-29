package com.nicovilab.profeconecta.view.extraSwingComponents.customTabbedPane;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.plaf.basic.BasicTabbedPaneUI;


/**
 *
 * @author RAVEN
 */
public class TabbedPaneCustomUI extends BasicTabbedPaneUI {

    private final TabbedPaneCustom tab;

    public TabbedPaneCustomUI(TabbedPaneCustom tab) {
        this.tab = tab;
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
    }

    @Override
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        return new Insets(8, 25, 8, 25);
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int width, int height, boolean isSelected) {
        // No border
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        // No focus indicator
    }

    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Pintar primero las pestañas no seleccionadas
        for (int i = 0; i < tabPane.getTabCount(); i++) {
            if (i != selectedIndex) {
                paintTabBackground(g2, i, false);
            }
        }
        
        // Pintar la pestaña seleccionada al final para que esté en el frente
        if (selectedIndex >= 0) {
            paintTabBackground(g2, selectedIndex, true);
        }
        
        g2.dispose();
        
        // NO llamar a super.paintTabArea para evitar el renderizado por defecto
        // super.paintTabArea(g, tabPlacement, selectedIndex);
        
        // En su lugar, pintar manualmente el texto de las pestañas
        paintTabTexts(g, tabPlacement, selectedIndex);
    }

    protected void paintTabBackground(Graphics2D g2, int index, boolean selected) {
        Rectangle rec = getTabBounds(tabPane, index);
        Color color = getTabColor(selected);
        
        // Crear la forma de la pestaña
        Shape shape = createTabArea(rec, selected);
        
        // Aplicar sombra solo a pestañas seleccionadas
        if (selected) {
            g2.drawImage(new ShadowRenderer(4, 0.3f, new Color(0, 0, 0, 50)).createShadow(shape), 
                        rec.x, rec.y - 2, null);
        }
        
        // Aplicar gradiente
        g2.setPaint(new GradientPaint(rec.x, rec.y, color.brighter(), rec.x, rec.y + rec.height, color));
        g2.fill(shape);
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tabAreaHeight = calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
        
        // Color de fondo del área de contenido
        g2.setColor(new Color(250, 250, 250));

        // Crear el área de contenido con bordes redondeados
        RoundRectangle2D contentArea = new RoundRectangle2D.Double(
            0, tabAreaHeight - 1, 
            tabPane.getWidth(), 
            tabPane.getHeight() - tabAreaHeight + 1, 
            15, 15
        );
        
        Area area = new Area(contentArea);
        
        // Si hay una pestaña seleccionada, crear la conexión
        if (selectedIndex >= 0) {
            Rectangle selectedTab = getTabBounds(tabPane, selectedIndex);
            
            // Crear un rectángulo que conecte la pestaña con el contenido
            Rectangle2D connector = new Rectangle2D.Double(
                selectedTab.x + 5, 
                tabAreaHeight - 1, 
                selectedTab.width - 10, 
                15
            );
            area.add(new Area(connector));
            
            // Suavizar las esquinas de la conexión
            if (selectedTab.x > 5) {
                // Esquina izquierda redondeada
                area.add(new Area(new Rectangle2D.Double(0, tabAreaHeight - 1, 15, 15)));
            }
        }
        
        g2.fill(area);
        g2.dispose();
    }

    private Shape createTabArea(Rectangle rec, boolean selected) {
        int x = rec.x;
        int y = rec.y;
        int width = rec.width;
        int height = rec.height;
        int round = 12;
        
        Path2D path = new Path2D.Double();
        
        // Comenzar desde la esquina inferior izquierda
        path.moveTo(x, y + height);
        
        // Línea vertical izquierda hacia arriba
        path.lineTo(x, y + round);
        
        // Curva superior izquierda
        path.quadTo(x, y, x + round, y);
        
        // Línea horizontal superior
        path.lineTo(x + width - round, y);
        
        // Curva superior derecha
        path.quadTo(x + width, y, x + width, y + round);
        
        // Línea vertical derecha hacia abajo
        path.lineTo(x + width, y + height);
        
        if (selected) {
            // Para pestañas seleccionadas, conectar directamente con el contenido
            path.lineTo(x, y + height);
        } else {
            // Para pestañas no seleccionadas, cerrar normalmente
            path.closePath();
        }
        
        return path;
    }

    private Color getTabColor(boolean selected) {
        return selected ? tab.getSelectedColor() : tab.getUnselectedColor();
    }
    
    private void paintTabTexts(Graphics g, int tabPlacement, int selectedIndex) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        for (int i = 0; i < tabPane.getTabCount(); i++) {
            Rectangle tabRect = getTabBounds(tabPane, i);
            String title = tabPane.getTitleAt(i);
            
            if (title != null) {
                // Configurar color del texto
                if (i == selectedIndex) {
                    g2.setColor(Color.WHITE); // Texto blanco para pestaña seleccionada
                } else {
                    g2.setColor(Color.DARK_GRAY); // Texto gris oscuro para pestañas no seleccionadas
                }
                
                // Configurar fuente
                g2.setFont(tabPane.getFont());
                
                // Calcular posición centrada del texto
                java.awt.FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(title);
                int textHeight = fm.getHeight();
                
                int x = tabRect.x + (tabRect.width - textWidth) / 2;
                int y = tabRect.y + (tabRect.height - textHeight) / 2 + fm.getAscent();
                
                // Dibujar el texto
                g2.drawString(title, x, y);
            }
        }
        
        g2.dispose();
    }
}

