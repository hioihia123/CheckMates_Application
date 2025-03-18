/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class OutlinedTextCanvas extends Canvas {
    @Override
    public void paint(Graphics g) {
        // Use Graphics2D for better rendering
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Set background (optional)
        setBackground(Color.WHITE);
        
        // Define the text and font
        String text = "CheckMates";
        Font font = new Font("Microsoft Sans Serif", Font.BOLD, 36);
        g2.setFont(font);
        
        // Get font metrics to measure the text
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int ascent = fm.getAscent();
        
        // Calculate a simple left-top coordinate 
        int padding = 10;
        int x = padding;
        int y = padding + ascent;
        
        // Draw the text
        g2.setColor(Color.BLACK);
        g2.drawString(text, x, y);
        
        // Draw a rectangle outline around the text with extra padding
        g2.drawRect(x - padding, y - ascent - padding, textWidth + 2 * padding, textHeight + 2 * padding);
    }
}

