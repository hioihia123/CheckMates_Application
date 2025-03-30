/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

public class ClassDashboard extends JFrame {

    private Professor professor;  // The professor data passed in
    private JTable classesTable;  // A table to display classes

    public ClassDashboard(Professor professor) {
        this.professor = professor;
        setTitle("Class Dashboard for " + professor.getProfessorName());
        setSize(900, 700);  // Increased size to accommodate buttons
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
        FancyHoverButton addButton = new FancyHoverButton("Add Class");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.addActionListener(e -> addNewClass());
        buttonPanel.add(addButton);

        // Delete Class button
        FancyHoverButton deleteButton = new FancyHoverButton("Delete Selected");
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

    private void addNewClass() {
        JDialog dialog = new JDialog(this, "Create New Class", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Class Name
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(nameLabel, gbc);

        JTextField classNameField = new JTextField(20);
        classNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPanel.add(classNameField, gbc);

        // Section
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField(20);
        sectionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(sectionField, gbc);

        // Expiration
        JLabel expirationLabel = new JLabel("Expiration (mins):");
        expirationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPanel.add(expirationLabel, gbc);

        JTextField expirationField = new JTextField("60", 20);
        expirationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPanel.add(expirationField, gbc);

        // Create button
        FancyHoverButton createButton = new FancyHoverButton("Create");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        createButton.addActionListener(e -> {
            String className = classNameField.getText().trim();
            String section = sectionField.getText().trim();
            String expirationText = expirationField.getText().trim();

            if (className.isEmpty() || section.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "Please fill in both Class Name and Section.",
                        "Incomplete Form", JOptionPane.ERROR_MESSAGE);
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
                        "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }

            createNewClass(className, section, expirationMinutes);
            dialog.dispose();
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(createButton, gbc);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void createNewClass(String className, String section, int expirationMinutes) {
        new Thread(() -> {
            try {
                String urlString = "http://cm8tes.com/createClass.php";
                String urlParameters = "professor_id=" + URLEncoder.encode(professor.getProfessorID(), "UTF-8") +
                        "&class=" + URLEncoder.encode(className, "UTF-8") +
                        "&section=" + URLEncoder.encode(section, "UTF-8") +
                        "&expiration=" + expirationMinutes;

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
                                "Class created successfully!\nPasscode: " + json.optInt("passcode"),
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
                            "Error creating class: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
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
                // Add professor_id to the parameters
                String urlParameters = "class_id=" + classId +
                        "&professor_id=" + URLEncoder.encode(professor.getProfessorID(), "UTF-8");

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
                    out.write(urlParameters.getBytes(StandardCharsets.UTF_8));
                }

                // Check response code first
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
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