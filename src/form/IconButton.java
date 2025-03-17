/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
import java.awt.Button;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Dimension;

public class IconButton extends Button {
    private Image icon;

    public IconButton(Image icon) {
        // Optional: set a blank label so the button doesn’t show text
        super("");
        this.icon = icon;
    }

    public void setIcon(Image newIcon) {
        this.icon = newIcon;
        repaint();  // Force a redraw
    }

    @Override
    public void paint(Graphics g) {
        // Let the AWT Button do its normal painting (background, focus, etc.)
        super.paint(g);

        if (icon != null) {
            // Center the icon in the button
            int iconWidth = icon.getWidth(this);
            int iconHeight = icon.getHeight(this);
            int x = (getWidth() - iconWidth) / 2;
            int y = (getHeight() - iconHeight) / 2;

            g.drawImage(icon, x, y, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (icon != null) {
            // Ensure the button is big enough for the icon plus some padding
            return new Dimension(icon.getWidth(this) + 10, icon.getHeight(this) + 10);
        }
        // Fallback if no icon
        return super.getPreferredSize();
    }
}

