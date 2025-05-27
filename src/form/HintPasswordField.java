/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;


import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class HintPasswordField extends JPasswordField {
    private String hint;
    private Color hintColor = Color.GRAY;

    public HintPasswordField(String hint) {
        this.hint = hint;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the hint only if password is empty and not focused
        if (getPassword().length == 0 && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(hintColor);
            int padding = getInsets().left + 5;
            g2.drawString(hint, padding, g2.getFontMetrics().getMaxAscent() + getInsets().top + 2);
            g2.dispose();
        }
    }

    public void setHint(String hint) {
        this.hint = hint;
        repaint();
    }

    public void setHintColor(Color hintColor) {
        this.hintColor = hintColor;
        repaint();
    }
}

