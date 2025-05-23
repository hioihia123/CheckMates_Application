/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
import javax.swing.*;
import java.awt.*;

public class ModernSeparator extends JPanel {
    private final boolean horizontal;
    private final Color startColor;
    private final Color endColor;

    /**
     * @param horizontal  true for a horizontal line, false for vertical
     * @param startColor  gradient’s start color
     * @param endColor    gradient’s end color
     */
    public ModernSeparator(boolean horizontal, Color startColor, Color endColor) {
        this.horizontal = horizontal;
        this.startColor = startColor;
        this.endColor = endColor;
        // Make it non-opaque so only the line shows
        setOpaque(false);
    }
    
    @Override
    public Dimension getPreferredSize() {
      return horizontal
        ? new Dimension(750, 10)
        : new Dimension(10, 200);
    }

    @Override
    public Dimension getMaximumSize() {
        // Make max == pref so no layout manager can stretch it larger
        return getPreferredSize();
    }   

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth(), h = getHeight();

        // 1) Smooth edges
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                            RenderingHints.VALUE_ANTIALIAS_ON);

        // 2) Create a gradient paint along the line
        GradientPaint paint = horizontal
          ? new GradientPaint(0, 0, startColor, w, 0, endColor)
          : new GradientPaint(0, 0, startColor, 0, h, endColor);
        g2.setPaint(paint);

        // 3) Custom stroke: thick, rounded ends, dashed
        float thickness = 4f;
        float[] dashPattern = { 15f, 8f }; // dash length, gap length
        BasicStroke stroke = new BasicStroke(
          thickness,
          BasicStroke.CAP_ROUND,
          BasicStroke.JOIN_ROUND,
          1.0f,
          dashPattern,
          0f
        );
        g2.setStroke(stroke);

        // 4) Draw the line centered in the panel
        if (horizontal) {
            int y = h / 2;
            g2.drawLine(0, y, w, y);
        } else {
            int x = w / 2;
            g2.drawLine(x, 0, x, h);
        }

        g2.dispose();
    }
}
