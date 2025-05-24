/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nguyenp
 */
package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

public class FancyHoverButton3 extends JButton {

    // Idle (normal) colors
    private final Color idleOutlineColor = (Color.ORANGE);  // Dark Green

    private final Color idleTextColor =  new Color(0, 100, 0);

    // Hover colors
    private final Color hoverOutlineColor =  new Color(0, 100, 0);
    private final Color hoverTextColor = Color.RED;

    // Track whether the mouse is hovering
    private boolean hovering = false;

    public FancyHoverButton3(String text) {
        super(text);
        setOpaque(false);              // do our own painting
        setContentAreaFilled(false);   // No default fill
        setFocusPainted(false);        // No focus outline
        setBorderPainted(false);       // draw our own border
        setForeground(idleTextColor);

        // Hover detection: change outline and text colors on mouse enter/exit
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                setForeground(hoverTextColor);
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                setForeground(idleTextColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Create a copy for custom painting and enable anti-aliasing
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine button bounds
        int width = getWidth();
        int height = getHeight();
        int inset = 2;
        int outlineThickness = 2;

        // Create a rounded rectangle shape for the button's outline
        Shape shape = new RoundRectangle2D.Float(
                inset, 
                inset, 
                width - 2 * inset, 
                height - 2 * inset, 
                12, // arc width
                12  // arc height
        );

        // Choose the outline color based on hover state.
        Color currentOutline = hovering ? hoverOutlineColor : idleOutlineColor;
        g2.setStroke(new BasicStroke(outlineThickness));
        g2.setColor(currentOutline);
        g2.draw(shape);

        // Let Swing paint the text (the button's label) on top.
        super.paintComponent(g2);

        g2.dispose();
    }
}

