package form;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class FancyLogoPanel extends JPanel {
    private BufferedImage logoImage;
    
    // Desired display width for the logo (the height will be scaled proportionally)
    private static final int DESIRED_WIDTH = 160;
    // Drop shadow offset (adjust or remove if you don't want a shadow)
    private static final int SHADOW_OFFSET = 1;

    public FancyLogoPanel(String imagePath) {
        try {
            if (imagePath.startsWith("http")) {
                // Load the image from an external URL
                logoImage = ImageIO.read(new URL(imagePath));
            } else {
                // Load the image from local resources
                logoImage = ImageIO.read(getClass().getResource(imagePath));
            }
        } catch (Exception e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (logoImage == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate scaled dimensions
        double aspectRatio = (double) logoImage.getHeight() / logoImage.getWidth();
        int scaledHeight = (int) (DESIRED_WIDTH * aspectRatio);

        // Scale the logo smoothly
        Image scaledLogo = logoImage.getScaledInstance(DESIRED_WIDTH, scaledHeight, Image.SCALE_SMOOTH);

        // --- Drop Shadow ---
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2.drawImage(scaledLogo, SHADOW_OFFSET, SHADOW_OFFSET, null);

        // --- Draw the actual logo ---
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.drawImage(scaledLogo, 0, 0, null);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        if (logoImage != null) {
            // Calculate scaled dimensions
            double aspectRatio = (double) logoImage.getHeight() / logoImage.getWidth();
            int scaledHeight = (int) (DESIRED_WIDTH * aspectRatio);

            // Provide enough room for the image + shadow offset
            int width = DESIRED_WIDTH + SHADOW_OFFSET;
            int height = scaledHeight + SHADOW_OFFSET;
            return new Dimension(width, height);
        } else {
            // Fallback if image not loaded
            return new Dimension(200, 150);
        }
    }
}
