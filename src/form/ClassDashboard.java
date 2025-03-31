package form;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class ClassDashboard extends JFrame {

    private Professor professor;
    private JTable classesTable;
    private boolean oldTimeyMode;

    // Old-timey style properties
    private Color typewriterInk = new Color(50, 40, 30);
    private Font typewriterFont = new Font("Courier New", Font.BOLD, 14);
    private Font titleFont = new Font("Courier New", Font.BOLD, 24);
    private Image backgroundImage;
    private Color parchmentColor = new Color(253, 245, 230, 200);

    // Modern style properties
    private Color modernTextColor = new Color(60, 60, 60);
    private Font modernFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font modernTitleFont = new Font("Segoe UI", Font.BOLD, 24);
    private Color modernBackground = Color.WHITE;
    private Color modernPanelColor = new Color(240, 240, 240);
    private Color modernAccentColor = new Color(100, 149, 237); // Cornflower blue
    private Color modernHighlightColor = new Color(200, 220, 255);

    public ClassDashboard(Professor professor) {
        this(professor, false); // Default to modern style
    }

    public ClassDashboard(Professor professor, boolean oldTimeyMode) {
        this.professor = professor;
        this.oldTimeyMode = oldTimeyMode;
        setTitle("Class Dashboard for " + professor.getProfessorName());
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Load background image for old-timey mode
        if (oldTimeyMode) {
            try {
                backgroundImage = ImageIO.read(getClass().getResource("parchmentColor.jpg"));
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Error loading image: " + e.getMessage());
                backgroundImage = null;
                JOptionPane.showMessageDialog(this,
                        "Could not load background image\nUsing fallback color",
                        "Image Error", JOptionPane.WARNING_MESSAGE);
            }
        }

        setupLookAndFeel();
        initComponents();
    }

    private void setupLookAndFeel() {
        try {
            if (oldTimeyMode) {
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
                // Modern look and feel settings
                UIManager.put("OptionPane.background", modernBackground);
                UIManager.put("OptionPane.messageFont", modernFont);
                UIManager.put("OptionPane.messageForeground", modernTextColor);
                UIManager.put("TextField.background", modernBackground);
                UIManager.put("TextField.font", modernFont);
                UIManager.put("TextField.foreground", modernTextColor);
                UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(200, 200, 200)));
                UIManager.put("Label.font", modernFont);
                UIManager.put("Label.foreground", modernTextColor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        if (oldTimeyMode) {
            initOldTimeyComponents();
        } else {
            initModernComponents();
        }
    }

    private void initOldTimeyComponents() {
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(parchmentColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, typewriterInk));

        JLabel greetingLabel = new JLabel("Classes for Professor " + professor.getProfessorName());
        greetingLabel.setFont(titleFont);
        greetingLabel.setForeground(typewriterInk);
        topPanel.add(greetingLabel);

        // Table setup
        classesTable = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(parchmentColor);
                c.setForeground(typewriterInk);
                c.setFont(typewriterFont);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(200, 180, 150, 200));
                }
                return c;
            }
        };
        customizeTable(oldTimeyMode);

        JScrollPane scrollPane = new JScrollPane(classesTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(typewriterInk));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(parchmentColor);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, typewriterInk));

        addOldTimeyButton(buttonPanel, "Add Class", e -> addNewClass());
        addOldTimeyButton(buttonPanel, "Edit Selected", e -> editSelectedClass());
        addOldTimeyButton(buttonPanel, "Delete Selected", e -> deleteSelectedClass());
        addOldTimeyButton(buttonPanel, "Refresh", e -> loadClassesForProfessor());
        addOldTimeyButton(buttonPanel, "Get Out", e -> dispose()); // Added exit button

        // Assemble components
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        loadClassesForProfessor();
    }

    private void initModernComponents() {
        // Main panel with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(modernBackground);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel with title
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(modernBackground);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JLabel greetingLabel = new JLabel("Classes for Professor " + professor.getProfessorName());
        greetingLabel.setFont(modernTitleFont);
        greetingLabel.setForeground(modernTextColor);
        topPanel.add(greetingLabel);

        // Table setup with modern styling
        classesTable = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(row % 2 == 0 ? modernBackground : new Color(248, 248, 248));
                c.setForeground(modernTextColor);
                c.setFont(modernFont);
                if (isRowSelected(row)) {
                    c.setBackground(modernHighlightColor);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        customizeTable(oldTimeyMode);

        JScrollPane scrollPane = new JScrollPane(classesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Button panel with modern buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(modernBackground);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        addModernButton(buttonPanel, "Add Class", e -> addNewClass());
        addModernButton(buttonPanel, "Edit Selected", e -> editSelectedClass());
        addModernButton(buttonPanel, "Delete Selected", e -> deleteSelectedClass());
        addModernButton(buttonPanel, "Refresh", e -> loadClassesForProfessor());
        addModernButton(buttonPanel, "Exit", e -> dispose()); // Added exit button

        // Assemble components
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        loadClassesForProfessor();
    }

    private void customizeTable(boolean oldTimey) {
        if (oldTimey) {
            classesTable.setFont(typewriterFont);
            classesTable.setForeground(typewriterInk);
            classesTable.setOpaque(false);
            classesTable.setGridColor(typewriterInk);
            classesTable.setRowHeight(30);
            classesTable.setSelectionBackground(new Color(200, 180, 150, 200));
            classesTable.setSelectionForeground(typewriterInk);
            classesTable.setBorder(BorderFactory.createLineBorder(typewriterInk));

            JTableHeader header = classesTable.getTableHeader();
            header.setFont(typewriterFont);
            header.setForeground(typewriterInk);
            header.setBackground(new Color(253, 245, 230, 220));
            header.setOpaque(false);
        } else {
            classesTable.setFont(modernFont);
            classesTable.setForeground(modernTextColor);
            classesTable.setRowHeight(30);
            classesTable.setSelectionBackground(modernHighlightColor);
            classesTable.setSelectionForeground(modernTextColor);
            classesTable.setGridColor(new Color(220, 220, 220));

            JTableHeader header = classesTable.getTableHeader();
            header.setFont(modernFont.deriveFont(Font.BOLD));
            header.setForeground(modernTextColor);
            header.setBackground(modernPanelColor);
            header.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }
    }

    private void addOldTimeyButton(JPanel panel, String text, ActionListener action) {
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
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener(action);
        panel.add(button);
    }

    private void addModernButton(JPanel panel, String text, ActionListener action) {
        FancyHoverButton button = new FancyHoverButton(text);
        button.setFont(modernFont.deriveFont(Font.BOLD));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.addActionListener(action);
        panel.add(button);
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
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    private JButton createModernButton(String text) {
        FancyHoverButton button = new FancyHoverButton(text);
        button.setFont(modernFont);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
    }

    private void editSelectedClass() {
        int selectedRow = classesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a class to edit",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) classesTable.getModel();
        int classId = Integer.parseInt(model.getValueAt(selectedRow, 1).toString());
        String className = model.getValueAt(selectedRow, 2).toString();
        String section = model.getValueAt(selectedRow, 3).toString();
        String expiresAt = model.getValueAt(selectedRow, 6).toString();

        showEditDialog(classId, className, section, expiresAt);
    }

    private void showEditDialog(int classId, String currentClassName, String currentSection, String expiresAt) {
        JDialog dialog = new JDialog(this, "Edit Class", true);
        dialog.setSize(600, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(oldTimeyMode ? parchmentColor : modernBackground);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPanel.setBackground(oldTimeyMode ? parchmentColor : modernBackground);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Class Name
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(oldTimeyMode ? typewriterFont : modernFont);
        nameLabel.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(nameLabel, gbc);

        JTextField classNameField = new JTextField(currentClassName, 40);
        classNameField.setFont(oldTimeyMode ? typewriterFont : modernFont);
        classNameField.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        classNameField.setBackground(oldTimeyMode ? parchmentColor : modernBackground);
        classNameField.setBorder(BorderFactory.createLineBorder(oldTimeyMode ? typewriterInk : new Color(200, 200, 200)));
        classNameField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(classNameField, gbc);

        // Section
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(oldTimeyMode ? typewriterFont : modernFont);
        sectionLabel.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        contentPanel.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField(currentSection, 40);
        sectionField.setFont(oldTimeyMode ? typewriterFont : modernFont);
        sectionField.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        sectionField.setBackground(oldTimeyMode ? parchmentColor : modernBackground);
        sectionField.setBorder(BorderFactory.createLineBorder(oldTimeyMode ? typewriterInk : new Color(200, 200, 200)));
        sectionField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        contentPanel.add(sectionField, gbc);

        // Expiration (display only)
        JLabel expirationLabel = new JLabel("Current Expiration:");
        expirationLabel.setFont(oldTimeyMode ? typewriterFont : modernFont);
        expirationLabel.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        contentPanel.add(expirationLabel, gbc);

        JLabel expirationValueLabel = new JLabel(expiresAt);
        expirationValueLabel.setFont(oldTimeyMode ? typewriterFont : modernFont);
        expirationValueLabel.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        contentPanel.add(expirationValueLabel, gbc);

        // Update button
        JButton updateButton = oldTimeyMode ? createTypewriterButton("Update") : createModernButton("Update");
        updateButton.setPreferredSize(new Dimension(150, 40));
        updateButton.addActionListener(e -> {
            String newClassName = classNameField.getText().trim();
            String newSection = sectionField.getText().trim();

            if (newClassName.isEmpty() || newSection.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Please fill in both Class Name and Section.",
                        "Incomplete Form", JOptionPane.ERROR_MESSAGE);
                return;
            }

            updateClassInDatabase(classId, newClassName, newSection);
            dialog.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(updateButton, gbc);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void updateClassInDatabase(int classId, String newClassName, String newSection) {
        new Thread(() -> {
            try {
                String urlString = "http://cm8tes.com/updateClass.php";
                String urlParameters = "class_id=" + classId +
                        "&professor_id=" + URLEncoder.encode(professor.getProfessorID(), "UTF-8") +
                        "&class=" + URLEncoder.encode(newClassName, "UTF-8") +
                        "&section=" + URLEncoder.encode(newSection, "UTF-8");

                URI uri = new URI(urlString);
                URL url = uri.toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlParameters);
                }

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                SwingUtilities.invokeLater(() -> {
                    if ("success".equalsIgnoreCase(json.optString("status"))) {
                        JOptionPane.showMessageDialog(this,
                                "Class updated successfully",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadClassesForProfessor();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error: " + json.optString("message"),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Error updating class: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void addNewClass() {
        JDialog dialog = new JDialog(this, "Create New Class", true);
        dialog.setSize(600, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(oldTimeyMode ? parchmentColor : modernBackground);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPanel.setBackground(oldTimeyMode ? parchmentColor : modernBackground);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Class Name Field
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(oldTimeyMode ? typewriterFont : modernFont);
        nameLabel.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(nameLabel, gbc);

        JTextField classNameField = new JTextField(40);
        classNameField.setFont(oldTimeyMode ? typewriterFont : modernFont);
        classNameField.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        classNameField.setBackground(oldTimeyMode ? parchmentColor : modernBackground);
        classNameField.setBorder(BorderFactory.createLineBorder(oldTimeyMode ? typewriterInk : new Color(200, 200, 200)));
        classNameField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(classNameField, gbc);

        // Section Field
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(oldTimeyMode ? typewriterFont : modernFont);
        sectionLabel.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        contentPanel.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField(40);
        sectionField.setFont(oldTimeyMode ? typewriterFont : modernFont);
        sectionField.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        sectionField.setBackground(oldTimeyMode ? parchmentColor : modernBackground);
        sectionField.setBorder(BorderFactory.createLineBorder(oldTimeyMode ? typewriterInk : new Color(200, 200, 200)));
        sectionField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        contentPanel.add(sectionField, gbc);

        // Expiration Field
        JLabel expirationLabel = new JLabel("Expiration (mins):");
        expirationLabel.setFont(oldTimeyMode ? typewriterFont : modernFont);
        expirationLabel.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        contentPanel.add(expirationLabel, gbc);

        JTextField expirationField = new JTextField("60", 40);
        expirationField.setFont(oldTimeyMode ? typewriterFont : modernFont);
        expirationField.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        expirationField.setBackground(oldTimeyMode ? parchmentColor : modernBackground);
        expirationField.setBorder(BorderFactory.createLineBorder(oldTimeyMode ? typewriterInk : new Color(200, 200, 200)));
        expirationField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        contentPanel.add(expirationField, gbc);

        // Create Button
        JButton createButton = oldTimeyMode ? createTypewriterButton("Create") : createModernButton("Create");
        createButton.setPreferredSize(new Dimension(150, 40));
        createButton.addActionListener(e -> {
            String className = classNameField.getText().trim();
            String section = sectionField.getText().trim();
            String expirationText = expirationField.getText().trim();

            if (className.isEmpty() || section.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Please fill in both Class Name and Section.",
                        "Incomplete Form",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int expirationMinutes = 60;
            try {
                if (!expirationText.isEmpty()) {
                    expirationMinutes = Integer.parseInt(expirationText);
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(dialog,
                        "Expiration must be a number. Using default of 60 minutes.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE);
            }

            // Generate a random 4-digit passcode
            int passcode = (int) (Math.random() * 9000) + 1000;
            String checkInUrl = "tinyurl.com/02221732";

            // Save the class and show QR code
            createNewClass(className, section, expirationMinutes, passcode, checkInUrl);
            dialog.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(createButton, gbc);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void createNewClass(String className, String section, int expirationMinutes, int passcode, String checkInUrl) {
        new Thread(() -> {
            try {
                String urlString = "http://cm8tes.com/createClass.php";
                String urlParameters = "professor_id=" + URLEncoder.encode(professor.getProfessorID(), "UTF-8") +
                        "&class=" + URLEncoder.encode(className, "UTF-8") +
                        "&section=" + URLEncoder.encode(section, "UTF-8") +
                        "&expiration=" + expirationMinutes +
                        "&passcode=" + passcode;

                URI uri = new URI(urlString);
                URL url = uri.toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlParameters);
                }

                // Read response
                StringBuilder responseBuilder = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }
                }
                final String response = responseBuilder.toString();
                final JSONObject json = new JSONObject(response);

                SwingUtilities.invokeLater(() -> {
                    if ("success".equalsIgnoreCase(json.optString("status"))) {
                        // Generate and show QR code
                        Image qrImage = generateQRCodeImage(checkInUrl, 200, 200);
                        showQRCodeDialog(qrImage, checkInUrl, passcode, expirationMinutes);

                        // Create custom notification panel
                        JPanel panel = new JPanel(new BorderLayout());
                        panel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
                        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                        // Create message label
                        JLabel message = new JLabel("<html><center>Class created successfully!<br>Passcode: " + passcode + "</center></html>");
                        message.setFont(oldTimeyMode ? typewriterFont : modernFont);
                        message.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
                        message.setHorizontalAlignment(SwingConstants.CENTER);
                        panel.add(message, BorderLayout.CENTER);

                        // Create OK button
                        JButton okButton = oldTimeyMode ?
                                createTypewriterButton("OK") :
                                createModernButton("OK");
                        okButton.addActionListener(e -> {
                            Window window = SwingUtilities.getWindowAncestor(panel);
                            if (window != null) {
                                window.dispose();
                            }
                        });

                        JPanel buttonPanel = new JPanel();
                        buttonPanel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
                        buttonPanel.add(okButton);
                        panel.add(buttonPanel, BorderLayout.SOUTH);

                        // Create and show custom dialog
                        JDialog dialog = new JDialog(this, "Success", true);
                        dialog.setContentPane(panel);
                        dialog.pack();
                        dialog.setLocationRelativeTo(this);
                        dialog.setVisible(true);

                        loadClassesForProfessor();
                    } else {
                        // Error handling
                        JOptionPane.showMessageDialog(this,
                                "Error: " + json.optString("message"),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Error creating class: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private Image generateQRCodeImage(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showQRCodeDialog(Image qrImage, String url, int passcode, int expirationMinutes) {
        JDialog qrDialog = new JDialog(this, "Class Check-In QR Code", true);
        qrDialog.setSize(320, 480);
        qrDialog.setLocationRelativeTo(this);
        qrDialog.getContentPane().setBackground(oldTimeyMode ? parchmentColor : modernBackground);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(oldTimeyMode ? parchmentColor : modernBackground);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Include passcode, URL, and expiration information
        JLabel infoLabel = new JLabel("<html>Passcode: " + passcode + "<br>" +
                "URL: " + url + "<br>" +
                "Expires in: " + expirationMinutes + " minute(s)</html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(oldTimeyMode ? typewriterFont : modernFont);
        infoLabel.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);

        contentPanel.add(qrLabel, BorderLayout.CENTER);
        contentPanel.add(infoLabel, BorderLayout.SOUTH);

        qrDialog.add(contentPanel);
        qrDialog.setVisible(true);
    }

    private void deleteSelectedClass() {
        int selectedRow = classesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a class to delete",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int classId = Integer.parseInt(
                ((javax.swing.table.DefaultTableModel)classesTable.getModel())
                        .getValueAt(selectedRow, 1).toString());

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this class?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            deleteClassFromDatabase(classId);
        }
    }

    private void deleteClassFromDatabase(int classId) {
        new Thread(() -> {
            try {
                String urlString = "http://cm8tes.com/deleteClass.php";
                String urlParameters = "class_id=" + classId +
                        "&professor_id=" + URLEncoder.encode(professor.getProfessorID(), "UTF-8");

                URI uri = new URI(urlString);
                URL url = uri.toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.writeBytes(urlParameters);
                }

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                SwingUtilities.invokeLater(() -> {
                    if ("success".equalsIgnoreCase(json.optString("status"))) {
                        JOptionPane.showMessageDialog(this,
                                "Class deleted successfully",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadClassesForProfessor();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Error: " + json.optString("message"),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting class: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void loadClassesForProfessor() {
        try {
            String urlString = "http://cm8tes.com/getClasses.php?professor_id=" +
                    URLEncoder.encode(professor.getProfessorID(), StandardCharsets.UTF_8);

            URI uri = new URI(urlString);
            URL url = uri.toURL();

            ArrayList<String[]> rowData = new ArrayList<>();
            String[] columnNames = {"No.", "Class ID", "Class Name", "Section", "Passcode", "Created At", "Expires At"};

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            if ("success".equalsIgnoreCase(json.optString("status"))) {
                JSONArray classesArray = json.getJSONArray("classes");
                for (int i = 0; i < classesArray.length(); i++) {
                    String displayNo = String.valueOf(i+1);
                    JSONObject obj = classesArray.getJSONObject(i);
                    String classId = String.valueOf(obj.optInt("class_id"));
                    String className = obj.optString("className");
                    String section = obj.optString("section");
                    String passcode = String.valueOf(obj.optInt("passcode"));
                    String createdAt = obj.optString("created_at");
                    String expiresAt = obj.optString("passcode_expires");
                    rowData.add(new String[] {displayNo, classId, className, section, passcode, createdAt, expiresAt});
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error: " + json.optString("message"),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            String[][] rowArray = rowData.toArray(new String[0][]);
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(rowArray, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table cells non-editable
                }
            };

            classesTable.setModel(model);
            classesTable.removeColumn(classesTable.getColumnModel().getColumn(1)); // Hide Class ID column
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static class FancyHoverButton extends JButton {
        private Color normalColor = new Color(76, 175, 80); // Green 500
        private Color hoverColor = new Color(102, 187, 106); // Green 400
        private Color pressedColor = new Color(56, 142, 60); // Green 600
        private Color textColor = Color.WHITE;
        private int cornerRadius = 12;

        public FancyHoverButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setForeground(textColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(pressedColor);
            } else if (getModel().isRollover()) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(normalColor);
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            // No border painting
        }
    }

    public static void main(String[] args) {
        Professor dummyProf = new Professor("Dr. Smith", "drsmith@example.com", "DR12345");
        SwingUtilities.invokeLater(() -> {
            ClassDashboard dashboard = new ClassDashboard(dummyProf, true); // Test with old-timey style
            dashboard.setVisible(true);
        });
    }
}