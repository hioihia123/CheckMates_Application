package form;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

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
import org.json.JSONObject;
import org.json.JSONArray;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

// Import ZXing libraries for QR code generation
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class ClassDashboard extends JFrame {

    private Professor professor;  // The professor data passed in
    private JTable classesTable;  // A table to display classes

    public ClassDashboard(Professor professor) {
        this.professor = professor;
        setTitle("Class Dashboard for " + professor.getProfessorName());
        setSize(1000, 800);  // Increased size to accommodate buttons
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Call a method that sets up the UI components
        initComponents();
    }

    private void initComponents() {
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with greeting
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel greetingLabel = new JLabel("Classes for Professor " + professor.getProfessorName());
        greetingLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        topPanel.add(greetingLabel);

        // Table to display classes
        classesTable = new JTable();
        classesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classesTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(classesTable);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Add Class button
        FancyHoverButton addButton = new FancyHoverButton("Add");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.addActionListener(e -> addNewClass());
        buttonPanel.add(addButton);

        // Edit Class button
        FancyHoverButton editButton = new FancyHoverButton("Edit");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        editButton.addActionListener(e -> editSelectedClass());
        buttonPanel.add(editButton);

        // Delete Class button
        FancyHoverButton deleteButton = new FancyHoverButton("Delete ");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        deleteButton.addActionListener(e -> deleteSelectedClass());
        buttonPanel.add(deleteButton);

        // Refresh button
        FancyHoverButton refreshButton = new FancyHoverButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refreshButton.addActionListener(e -> loadClassesForProfessor());
        buttonPanel.add(refreshButton);

        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);

        // Finally, fetch data and update the table
        loadClassesForProfessor();
    }

    private void editSelectedClass() {
        int selectedRow = classesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a class to edit",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get class data from the selected row
        DefaultTableModel model = (DefaultTableModel) classesTable.getModel();
        int classId = Integer.parseInt(model.getValueAt(selectedRow, 1).toString());
        String className = model.getValueAt(selectedRow, 2).toString();
        String section = model.getValueAt(selectedRow, 3).toString();
        String expiresAt = model.getValueAt(selectedRow, 6).toString();

        // Create and show edit dialog
        showEditDialog(classId, className, section, expiresAt);
    }

    private void showEditDialog(int classId, String currentClassName, String currentSection, String expiresAt) {
        JDialog dialog = new JDialog(this, "Edit Class", true);
        dialog.setSize(600, 350);  // Increased size
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Class Name
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(nameLabel, gbc);

        JTextField classNameField = new JTextField(currentClassName, 40);
        classNameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        classNameField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(classNameField, gbc);

        // Section
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        contentPanel.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField(currentSection, 40);
        sectionField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sectionField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        contentPanel.add(sectionField, gbc);

        // Expiration (display only)
        JLabel expirationLabel = new JLabel("Current Expiration:");
        expirationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        contentPanel.add(expirationLabel, gbc);

        JLabel expirationValueLabel = new JLabel(expiresAt);
        expirationValueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        contentPanel.add(expirationValueLabel, gbc);

        // Update button
        FancyHoverButton updateButton = new FancyHoverButton("Update");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
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

                URL url = new URL(urlString);
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
        dialog.setSize(600, 350);  // Increased size for better visibility
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Class Name Field
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        contentPanel.add(nameLabel, gbc);

        JTextField classNameField = new JTextField(40);
        classNameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        classNameField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        contentPanel.add(classNameField, gbc);

        // Section Field
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        contentPanel.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField(40);
        sectionField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sectionField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        contentPanel.add(sectionField, gbc);

        // Expiration Field
        JLabel expirationLabel = new JLabel("Expiration (mins):");
        expirationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        contentPanel.add(expirationLabel, gbc);

        JTextField expirationField = new JTextField(40);
        expirationField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        expirationField.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        contentPanel.add(expirationField, gbc);

        // Create Button
        FancyHoverButton createButton = new FancyHoverButton("Create");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
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

                URL url = new URL(urlString);
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
                        // Generate and show QR code
                        Image qrImage = generateQRCodeImage(checkInUrl, 200, 200);
                        showQRCodeDialog(qrImage, checkInUrl, passcode, expirationMinutes);

                        JOptionPane.showMessageDialog(this,
                                 "Class created successfully!\nPasscode: " + json.optInt("passcode") +
                        "\nExpires: " + json.optString("passcode_expires"),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
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
                            "Error creating class: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    // Method to generate a QR code image using ZXing
    private Image generateQRCodeImage(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
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

    private void deleteSelectedClass() {
        int selectedRow = classesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a class to delete",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the actual class ID from the hidden column (index 1)
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

                URL url = new URL(urlString);
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
        String urlString = "http://cm8tes.com/getClasses.php?professor_id=" +
                URLEncoder.encode(professor.getProfessorID(), StandardCharsets.UTF_8);

        ArrayList<String[]> rowData = new ArrayList<>();
        String[] columnNames = {"No.", "Class ID", "Class Name", "Section", "Passcode", "Created At", "Expires At"};

        try {
            URL url = new URL(urlString);
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
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
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
    }

    public static void main(String[] args) {
        // For testing
        Professor dummyProf = new Professor("Dr. Smith", "drsmith@example.com", "DR12345");
        SwingUtilities.invokeLater(() -> {
            ClassDashboard dashboard = new ClassDashboard(dummyProf);
            dashboard.setVisible(true);
        });
    }
}