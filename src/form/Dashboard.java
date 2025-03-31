/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

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

// Import ZXing libraries for QR code generation
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
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
        setBackground(Color.WHITE);
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
                "https://raw.githubusercontent.com/hioihia123/CheckMates_Application/refs/heads/master/inner-ground2.png"
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

        // --- Top bar: left side (logo & header) and right side (buttons) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        // Left side: logo and header panel
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(fancyLogo);
        leftPanel.add(Box.createHorizontalStrut(20)); // spacing
        leftPanel.add(headerPanel);

        // Right side: buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        // Add Old-Timey Style button that will open oldDashboard.java
        FancyHoverButton oldTimeyButton = new FancyHoverButton("Old-Timey Style");
        oldTimeyButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        oldTimeyButton.addActionListener(e -> {
            this.dispose();
            new oldDashboard(professor).setVisible(true);
        });
        rightPanel.add(oldTimeyButton);

        // Add log off button
        ModernButton logOffButton = new ModernButton("Log Off");
        logOffButton.addActionListener(e -> {
            this.dispose();
            new Login().setVisible(true);
        });
        rightPanel.add(logOffButton);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        // "What's on your mind?" label
        JLabel questionHeader = new JLabel("What's on your mind, " + professor.getProfessorName() +"?");
        questionHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 36));
        questionHeader.setForeground(new Color(50, 50, 50));

        // Wrap questionHeader in a center-aligned panel
        JPanel questionHeaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        questionHeaderPanel.setOpaque(false);
        questionHeaderPanel.add(questionHeader);

        // Create the fancy hover button
        FancyHoverButton fancyButton = new FancyHoverButton("Create Class");
        fancyButton.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        fancyButton.setPreferredSize(new Dimension(180, 50)); // size as needed
        // Add an action listener to open the create class dialog
        fancyButton.addActionListener(e -> openCreateClassDialog());

        // place this button in the same topSection panel, below the question header
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);

        topSection.add(topBar);

        // Add the questionHeaderPanel (which centers the text)
        topSection.add(questionHeaderPanel);

        // Add some vertical spacing so the button doesn't overlap the label
        topSection.add(javax.swing.Box.createVerticalStrut(60));

        // Put the button in a small panel (FlowLayout) if you want it left-aligned
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(fancyButton);
        topSection.add(buttonPanel);

        // Create a panel for additional buttons with BoxLayout along the X axis
        JPanel additionalButtonsPanel = new JPanel();
        additionalButtonsPanel.setLayout(new BoxLayout(additionalButtonsPanel, BoxLayout.X_AXIS));
        additionalButtonsPanel.setOpaque(false);

        //create a "View Class" button - explicitly set to modern style (false)
        FancyHoverButton viewClassButton = new FancyHoverButton("View Class");
        viewClassButton.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        viewClassButton.setPreferredSize(new Dimension(180, 50));
        viewClassButton.setMaximumSize(new Dimension(180,50));
        viewClassButton.addActionListener(e -> {
            // Create a new ClassDashboard window with modern style (false)
            ClassDashboard classDash = new ClassDashboard(professor, false);
            classDash.setVisible(true);
        });

        //create a "View Attendance" button
        FancyHoverButton viewAttendanceButton = new FancyHoverButton("Records");
        viewAttendanceButton.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        viewAttendanceButton.setPreferredSize(new Dimension(180, 50));
        viewAttendanceButton.setMaximumSize(new Dimension(180,50));
        viewAttendanceButton.addActionListener(e -> {
            ClassSelectionDashboard csd = new ClassSelectionDashboard(professor);
            csd.setVisible(true);
        });

        //create a "Saki" button for Saki AI Agent
        FancyHoverButton AIbutton = new FancyHoverButton("Saki");
        AIbutton.setFont(new Font ("Helvetica Neueu", Font.BOLD, 24));
        AIbutton.setPreferredSize(new Dimension(180,50));
        AIbutton.setMaximumSize(new Dimension(180,50));
        AIbutton.addActionListener(e -> {
            ChatDialog chatDialog = new ChatDialog(Dashboard.this, professor);
            chatDialog.setVisible(true);
        });

        // Add spacing before adding the additional button
        additionalButtonsPanel.add(Box.createHorizontalStrut(20)); // 20-pixel space
        additionalButtonsPanel.add(viewClassButton);
        additionalButtonsPanel.add(Box.createHorizontalStrut(20));
        additionalButtonsPanel.add(viewAttendanceButton);
        additionalButtonsPanel.add(Box.createHorizontalStrut(20));
        additionalButtonsPanel.add(AIbutton);

        // Then, add the additionalButtonsPanel to the main button panel that already contains the Create Class button.
        buttonPanel.add(additionalButtonsPanel);

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

    private void openCreateClassDialog() {
        JDialog dialog = new JDialog(this, "Create New Class", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        // Use GridBagLayout for more control
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);  // spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Class Name
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // label takes minimal width
        contentPanel.add(nameLabel, gbc);

        JTextField classNameField = new JTextField();
        classNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classNameField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // text field expands
        contentPanel.add(classNameField, gbc);

        // Row 1: Section
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        contentPanel.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField();
        sectionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sectionField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        contentPanel.add(sectionField, gbc);

        // Row 2: Expiration (minutes)
        JLabel expirationLabel = new JLabel("Expiration (minutes):");
        expirationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        contentPanel.add(expirationLabel, gbc);

        JTextField expirationField = new JTextField();
        expirationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        expirationField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        contentPanel.add(expirationField, gbc);

        // Row 3: Create button (spanning two columns)
        FancyHoverButton createButton = new FancyHoverButton("Create");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        createButton.setBackground(new Color(0, 100, 0));  // modern blue
        createButton.setFocusPainted(false);
        createButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = classNameField.getText().trim();
                String section = sectionField.getText().trim();
                String expirationText = expirationField.getText().trim();

                if (className.isEmpty() || section.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in both Class Name and Section.", "Incomplete Form", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Default expiration minutes
                int expirationMinutes = 60;
                try {
                    if (!expirationText.isEmpty()) {
                        expirationMinutes = Integer.parseInt(expirationText);
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(dialog, "Expiration must be a number (minutes). Using default of 60 minutes.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }

                // Generate a random 4-digit passcode
                int passcode = (int) (Math.random() * 9000) + 1000;

                // Build the check-in URL
                String checkInUrl = "tinyurl.com/02221732";

                // Save the class information and generate QR code
                saveClassToDatabase(professor.getProfessorID(), className, section, passcode, expirationMinutes);
                Image qrImage = generateQRCodeImage(checkInUrl, 200, 200);
                showQRCodeDialog(qrImage, checkInUrl, passcode, expirationMinutes);

                dialog.dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(createButton, gbc);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    // Method to generate a QR code image using ZXing
    private Image generateQRCodeImage(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
            java.awt.image.BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return qrImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to show a dialog with the generated QR code
    private void showQRCodeDialog(Image qrImage, String url, int passcode, int expirationMinutes) {
        JDialog qrDialog = new JDialog(this, "Class Check-In QR Code", true);
        qrDialog.setSize(320, 480);
        qrDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Include passcode, URL, and expiration information in the info label
        JLabel infoLabel = new JLabel("<html>Passcode: " + passcode + "<br>" +
                "URL: " + url + "<br>" +
                "Expires in: " + expirationMinutes + " minute(s)</html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(50, 50, 50));

        contentPanel.add(qrLabel, BorderLayout.CENTER);
        contentPanel.add(infoLabel, BorderLayout.SOUTH);

        qrDialog.add(contentPanel);
        qrDialog.setVisible(true);
    }
    //

    private void saveClassToDatabase(String professorId, String className, String section, int passcode, int expirationMinutes) {
        new Thread(() -> {
            try {
                String urlString = "http://cm8tes.com/createClass.php"; // Student check in php file
                String urlParameters = "professor_id=" + URLEncoder.encode(professorId, "UTF-8") +
                        "&class=" + URLEncoder.encode(className, "UTF-8") +
                        "&section=" + URLEncoder.encode(section, "UTF-8") +
                        "&expiration=" + expirationMinutes +
                        "&passcode=" + passcode;
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.write(postData);
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                if ("success".equalsIgnoreCase(json.optString("status"))) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(Dashboard.this,
                                "Class created successfully!\nPasscode: " + json.optInt("passcode") +
                                        "\nExpires: " + json.optString("passcode_expires"),
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                } else {
                    String errMsg = json.optString("message", "Error creating class");
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(Dashboard.this, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Dashboard.this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
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
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
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