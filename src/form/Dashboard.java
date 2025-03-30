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
    private Color typewriterInk = new Color(50, 40, 30);
    private Font typewriterFont = new Font("Courier New", Font.BOLD, 14);
    private Font titleFont = new Font("Courier New", Font.BOLD, 24);
    private Image backgroundImage;
    private Color parchmentColor = new Color(253, 245, 230, 200);
    private boolean oldTimeyMode = false; // Track current style mode
    private JButton styleToggleButton; // Button to toggle styles

    /**
     * Creates new form Dashboard with a Professor object.
     */
    public Dashboard(Professor professor) {
        this.professor = professor;
        initComponents();
        initializeUI();
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        // Create the style toggle button
        styleToggleButton = new JButton("Old-Timey Style");
        styleToggleButton.setFont(new Font("Arial", Font.PLAIN, 12));
        styleToggleButton.addActionListener(e -> toggleStyle());

        // Load background image for old-timey mode
        try {
            backgroundImage = ImageIO.read(getClass().getResource("parchmentColor.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            backgroundImage = null;
        }

        // Initial UI setup
        updateUI();
    }

    private void toggleStyle() {
        oldTimeyMode = !oldTimeyMode;
        styleToggleButton.setText(oldTimeyMode ? "Modern Style" : "Old-Timey Style");
        updateUI();
    }

    private void updateUI() {
        if (oldTimeyMode) {
            setupOldTimeyUI();
        } else {
            setupModernUI();
        }
    }

    private void setupOldTimeyUI() {
        setupLookAndFeel(true);

        // Main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(parchmentColor);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top bar with greeting and buttons
        JPanel topBar = createOldTimeyTopBar();
        JPanel contentPanel = createOldTimeyContent();
        JPanel footerPanel = createOldTimeyFooter();

        // Assemble main panel
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        updateContentPane(mainPanel);
    }

    private JPanel createOldTimeyTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, typewriterInk));

        // Greeting labels
        JLabel greetingLabel = new JLabel("Hello, " + professor.getProfessorName() + "!");
        greetingLabel.setFont(titleFont);
        greetingLabel.setForeground(typewriterInk);

        JLabel professorIDLabel = new JLabel("Professor ID: " + professor.getProfessorID());
        professorIDLabel.setFont(typewriterFont);
        professorIDLabel.setForeground(typewriterInk);

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(greetingLabel);
        headerPanel.add(professorIDLabel);

        // Left side: header panel
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(headerPanel);

        // Right side: buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        // Add style toggle button
        JButton toggleBtn = createTypewriterButton(oldTimeyMode ? "Modern Style" : "Old-Timey Style");
        toggleBtn.addActionListener(e -> toggleStyle());
        rightPanel.add(toggleBtn);

        // Add log off button
        JButton logOffButton = createTypewriterButton("Log Off");
        logOffButton.addActionListener(e -> {
            this.dispose();
            new Login().setVisible(true);
        });
        rightPanel.add(logOffButton);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createOldTimeyContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Question header
        JLabel questionHeader = new JLabel("What's on your mind, " + professor.getProfessorName() + "?");
        questionHeader.setFont(titleFont);
        questionHeader.setForeground(typewriterInk);
        questionHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create buttons
        JButton createClassButton = createTypewriterButton("Create Class");
        createClassButton.setFont(new Font("Courier New", Font.BOLD, 18));
        createClassButton.addActionListener(e -> openCreateClassDialog());

        JButton viewClassButton = createTypewriterButton("View Class");
        viewClassButton.setFont(new Font("Courier New", Font.BOLD, 18));
        viewClassButton.addActionListener(e -> {
            ClassDashboard classDash = new ClassDashboard(professor, oldTimeyMode); // Pass oldTimeyMode
            classDash.setVisible(true);
        });

        JButton viewAttendanceButton = createTypewriterButton("Records");
        viewAttendanceButton.setFont(new Font("Courier New", Font.BOLD, 18));
        viewAttendanceButton.addActionListener(e -> {
            ClassSelectionDashboard csd = new ClassSelectionDashboard(professor);
            csd.setVisible(true);
        });

        JButton AIbutton = createTypewriterButton("Saki");
        AIbutton.setFont(new Font("Courier New", Font.BOLD, 18));
        AIbutton.addActionListener(e -> {
            ChatDialog chatDialog = new ChatDialog(Dashboard.this, professor);
            chatDialog.setVisible(true);
        });

        // Add buttons with spacing
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(createClassButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(viewClassButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(viewAttendanceButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(AIbutton);

        // Add components to content panel
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(questionHeader);
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(buttonPanel);

        return contentPanel;
    }

    private JPanel createOldTimeyFooter() {
        JLabel versionLabel = new JLabel("CheckMates Version 1.0");
        versionLabel.setFont(typewriterFont);
        versionLabel.setForeground(typewriterInk);
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        footerPanel.add(versionLabel);
        return footerPanel;
    }

    private void setupModernUI() {
        setupLookAndFeel(false);

        // Create modern UI components
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top bar with greeting and buttons
        JPanel topBar = createModernTopBar();
        JPanel contentPanel = createModernContent();
        JPanel footerPanel = createModernFooter();

        // Assemble main panel
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        updateContentPane(mainPanel);
    }

    private JPanel createModernTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(240, 240, 240));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        // Greeting labels
        JLabel greetingLabel = new JLabel("Hello, " + professor.getProfessorName() + "!");
        greetingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        greetingLabel.setForeground(Color.BLACK);

        JLabel professorIDLabel = new JLabel("Professor ID: " + professor.getProfessorID());
        professorIDLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        professorIDLabel.setForeground(Color.DARK_GRAY);

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(greetingLabel);
        headerPanel.add(professorIDLabel);

        // Left side: header panel
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(headerPanel);

        // Right side: buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        // Add style toggle button
        JButton toggleBtn = new JButton("Old-Timey Style");
        toggleBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        toggleBtn.addActionListener(e -> toggleStyle());
        rightPanel.add(toggleBtn);

        // Add log off button
        JButton logOffButton = new JButton("Log Off");
        logOffButton.addActionListener(e -> {
            this.dispose();
            new Login().setVisible(true);
        });
        rightPanel.add(logOffButton);

        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createModernContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Question header
        JLabel questionHeader = new JLabel("What's on your mind, " + professor.getProfessorName() + "?");
        questionHeader.setFont(new Font("Arial", Font.BOLD, 36));
        questionHeader.setForeground(Color.BLACK);
        questionHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create buttons
        JButton createClassButton = new JButton("Create Class");
        createClassButton.setFont(new Font("Arial", Font.BOLD, 18));
        createClassButton.addActionListener(e -> openCreateClassDialog());

        JButton viewClassButton = new JButton("View Class");
        viewClassButton.setFont(new Font("Arial", Font.BOLD, 18));
        viewClassButton.addActionListener(e -> {
            ClassDashboard classDash = new ClassDashboard(professor, oldTimeyMode); // Pass oldTimeyMode
            classDash.setVisible(true);
        });

        JButton viewAttendanceButton = new JButton("Records");
        viewAttendanceButton.setFont(new Font("Arial", Font.BOLD, 18));
        viewAttendanceButton.addActionListener(e -> {
            ClassSelectionDashboard csd = new ClassSelectionDashboard(professor);
            csd.setVisible(true);
        });

        JButton AIbutton = new JButton("Saki");
        AIbutton.setFont(new Font("Arial", Font.BOLD, 18));
        AIbutton.addActionListener(e -> {
            ChatDialog chatDialog = new ChatDialog(Dashboard.this, professor);
            chatDialog.setVisible(true);
        });

        // Add buttons with spacing
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(createClassButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(viewClassButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(viewAttendanceButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(AIbutton);

        // Add components to content panel
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(questionHeader);
        contentPanel.add(Box.createVerticalStrut(40));
        contentPanel.add(buttonPanel);

        return contentPanel;
    }

    private JPanel createModernFooter() {
        JLabel versionLabel = new JLabel("CheckMates Version 1.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(Color.DARK_GRAY);
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        footerPanel.add(versionLabel);
        return footerPanel;
    }

    private void setupLookAndFeel(boolean oldTimey) {
        try {
            if (oldTimey) {
                UIManager.put("OptionPane.background", new Color(253, 245, 230, 220));
                UIManager.put("OptionPane.messageFont", typewriterFont);
                UIManager.put("OptionPane.messageForeground", typewriterInk);
                UIManager.put("TextField.background", parchmentColor);
                UIManager.put("TextField.font", typewriterFont);
                UIManager.put("TextField.foreground", typewriterInk);
                UIManager.put("TextField.border", BorderFactory.createLineBorder(typewriterInk));
                UIManager.put("Label.font", typewriterFont);
                UIManager.put("Label.foreground", typewriterInk);
            } else {
                // Reset to default look and feel
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateContentPane(JPanel newPanel) {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(newPanel, BorderLayout.CENTER);
        validate();
        repaint();
    }

    private JButton createTypewriterButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(new Color(220, 200, 180));
                } else if (getModel().isRollover()) {
                    g.setColor(new Color(240, 230, 210));
                } else {
                    g.setColor(parchmentColor);
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        button.setFont(typewriterFont);
        button.setForeground(typewriterInk);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(typewriterInk),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    private void openCreateClassDialog() {
        JDialog dialog = new JDialog(this, "Create New Class", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);

        // Use GridBagLayout for more control
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);  // spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Class Name
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(oldTimeyMode ? typewriterFont : new Font("Arial", Font.PLAIN, 14));
        nameLabel.setForeground(oldTimeyMode ? typewriterInk : Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // label takes minimal width
        contentPanel.add(nameLabel, gbc);

        JTextField classNameField = new JTextField();
        classNameField.setFont(oldTimeyMode ? typewriterFont : new Font("Arial", Font.PLAIN, 14));
        classNameField.setForeground(oldTimeyMode ? typewriterInk : Color.BLACK);
        classNameField.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
        classNameField.setBorder(BorderFactory.createLineBorder(oldTimeyMode ? typewriterInk : Color.GRAY));
        classNameField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // text field expands
        contentPanel.add(classNameField, gbc);

        // Row 1: Section
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(oldTimeyMode ? typewriterFont : new Font("Arial", Font.PLAIN, 14));
        sectionLabel.setForeground(oldTimeyMode ? typewriterInk : Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        contentPanel.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField();
        sectionField.setFont(oldTimeyMode ? typewriterFont : new Font("Arial", Font.PLAIN, 14));
        sectionField.setForeground(oldTimeyMode ? typewriterInk : Color.BLACK);
        sectionField.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
        sectionField.setBorder(BorderFactory.createLineBorder(oldTimeyMode ? typewriterInk : Color.GRAY));
        sectionField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        contentPanel.add(sectionField, gbc);

        // Row 2: Expiration (minutes)
        JLabel expirationLabel = new JLabel("Expiration (minutes):");
        expirationLabel.setFont(oldTimeyMode ? typewriterFont : new Font("Arial", Font.PLAIN, 14));
        expirationLabel.setForeground(oldTimeyMode ? typewriterInk : Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        contentPanel.add(expirationLabel, gbc);

        JTextField expirationField = new JTextField();
        expirationField.setFont(oldTimeyMode ? typewriterFont : new Font("Arial", Font.PLAIN, 14));
        expirationField.setForeground(oldTimeyMode ? typewriterInk : Color.BLACK);
        expirationField.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
        expirationField.setBorder(BorderFactory.createLineBorder(oldTimeyMode ? typewriterInk : Color.GRAY));
        expirationField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        contentPanel.add(expirationField, gbc);

        // Row 3: Create button (spanning two columns)
        JButton createButton = oldTimeyMode ? createTypewriterButton("Create") : new JButton("Create");
        createButton.setFont(new Font(oldTimeyMode ? "Courier New" : "Arial", Font.BOLD, 16));
        createButton.setFocusPainted(false);

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
        qrDialog.getContentPane().setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Include passcode, URL, and expiration information in the info label
        JLabel infoLabel = new JLabel("<html>Passcode: " + passcode + "<br>" +
                "URL: " + url + "<br>" +
                "Expires in: " + expirationMinutes + " minute(s)</html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(oldTimeyMode ? typewriterFont : new Font("Arial", Font.PLAIN, 14));
        infoLabel.setForeground(oldTimeyMode ? typewriterInk : Color.BLACK);

        contentPanel.add(qrLabel, BorderLayout.CENTER);
        contentPanel.add(infoLabel, BorderLayout.SOUTH);

        qrDialog.add(contentPanel);
        qrDialog.setVisible(true);
    }

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