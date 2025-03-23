/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Dimension;


import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JButton;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject; // Make sure to include a JSON library
/**
 *
 * @author nguyenp
 */
// Import your Professor class. Adjust the package if needed.

public class Dashboard extends javax.swing.JFrame {

    private Professor professor;  // Store professor-specific info

    /**
     * Creates new form Dashboard with a Professor object.
     */
    public Dashboard(Professor professor) {
    this.professor = professor;
    initComponents(); 
    initializeUI();
    
    // Explicitly set the size of the JFrame
    setSize(1200, 1000); 
    // (Optional) Center the window on the screen
    setLocationRelativeTo(null);
}



    /**
     * This method is used to add custom components to the Dashboard.
     */
   private void initializeUI() {
    // Create the fancy logo panel (adjust the image path as needed)
    FancyLogoPanel fancyLogo = new FancyLogoPanel(
        "https://raw.githubusercontent.com/hioihia123/CheckMates_Application/refs/heads/master/strategy.png"
    );

    // --- Existing header labels ---
    JLabel greetingLabel = new JLabel("Hello, " + professor.getProfessorName() + "!");
    greetingLabel.setFont(new Font("Roboto", Font.BOLD, 24));
    greetingLabel.setForeground(new Color(50, 50, 50));
    greetingLabel.setHorizontalAlignment(SwingConstants.LEFT);
    
    JLabel professorIDLabel = new JLabel("Professor ID: " + professor.getProfessorID());
    professorIDLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
    professorIDLabel.setForeground(new Color(80, 80, 80));
    professorIDLabel.setHorizontalAlignment(SwingConstants.LEFT);
    
    // Create a header panel for the greeting and professor ID, stacked vertically
    JPanel headerPanel = new JPanel(new GridLayout(2, 1));
    headerPanel.setOpaque(false);
    headerPanel.add(greetingLabel);
    headerPanel.add(professorIDLabel);

    // --- Top bar: left side (logo & header) and right side (log off button) ---
    JPanel topBar = new JPanel(new BorderLayout());
    topBar.setOpaque(false);

    // Left side: logo and header panel
    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    leftPanel.setOpaque(false);
    leftPanel.add(fancyLogo);
    leftPanel.add(Box.createHorizontalStrut(20)); // spacing
    leftPanel.add(headerPanel);

    // Right side: modern log off button
    ModernButton logOffButton = new ModernButton("Log Off");
    logOffButton.addActionListener(e -> {
        Dashboard.this.dispose();
        new Login().setVisible(true);
    });
    JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    rightPanel.setOpaque(false);
    rightPanel.add(logOffButton);

    topBar.add(leftPanel, BorderLayout.WEST);
    topBar.add(rightPanel, BorderLayout.EAST);

    // "What's on your mind?" label
    JLabel questionHeader = new JLabel("What's on your mind?");
    questionHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 36));
    questionHeader.setForeground(new Color(50, 50, 50));
     // Remove the forced LEFT alignment so we can center it
     // questionHeader.setHorizontalAlignment(SwingConstants.LEFT);

     // Wrap questionHeader in a center-aligned panel
     JPanel questionHeaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
     questionHeaderPanel.setOpaque(false);
     questionHeaderPanel.add(questionHeader);

     // Create the fancy hover button
     FancyHoverButton fancyButton = new FancyHoverButton("Create Class");
     fancyButton.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
     fancyButton.setPreferredSize(new Dimension(180, 50)); // size as needed

      // We'll place this button in the same topSection panel, below the question header
     JPanel topSection = new JPanel();
     topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
     topSection.setOpaque(false);

     topSection.add(topBar);

      // Add the questionHeaderPanel (which centers the text)
     topSection.add(questionHeaderPanel);

     // Add some vertical spacing so the button doesn't overlap the label
     topSection.add(Box.createVerticalStrut(40));

     // Put the button in a small panel (FlowLayout) if you want it left-aligned
     JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
     buttonPanel.setOpaque(false);
     buttonPanel.add(fancyButton);
     topSection.add(buttonPanel);
    // --- Main panel setup ---
    GradientPanel panel = new GradientPanel();
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // Place the top section (top bar + question header) at the top
    panel.add(topSection, BorderLayout.NORTH);
    
    // --- Bottom: Create the text version label ---
    JLabel textLogoLabel = new JLabel("CheckMates Version 1.0");
    textLogoLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
    textLogoLabel.setForeground(new Color(50, 50, 50));
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottomPanel.setOpaque(false);
    bottomPanel.add(textLogoLabel);
    
    panel.add(bottomPanel, BorderLayout.SOUTH);
    panel.setPreferredSize(new Dimension(1200, 1000));
    
    getContentPane().removeAll();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(panel, BorderLayout.CENTER);
    getContentPane().setBackground(new Color(240, 240, 240));
    validate();
    repaint();
}





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        greetingLabel = new javax.swing.JLabel();
        questionHeader = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        greetingLabel.setFont(new java.awt.Font("Hiragino Maru Gothic ProN", 3, 36)); // NOI18N
        greetingLabel.setText("jLabel1");

        questionHeader.setFont(new java.awt.Font("Helvetica Neue", 3, 36)); // NOI18N
        questionHeader.setText("What's on your mind?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(365, 365, 365)
                        .addComponent(greetingLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(questionHeader)))
                .addContainerGap(386, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(135, 135, 135)
                .addComponent(questionHeader)
                .addGap(134, 134, 134)
                .addComponent(greetingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(286, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        // Create a Professors object
    Professor prof = new Professor("Professor Name", "test@gmail.com", "it's not a game. I am not a robot AI challenging you");
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            new Dashboard(prof).setVisible(true);
        }
    });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel greetingLabel;
    private javax.swing.JLabel questionHeader;
    // End of variables declaration//GEN-END:variables
}
