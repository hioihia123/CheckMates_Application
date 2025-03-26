/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
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
import org.json.JSONObject; // Make sure to include a JSON library
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
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Call a method that sets up the UI components
        initComponents();
    }

    private void initComponents() {
        // For styling

        // Top panel with greeting
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel greetingLabel = new JLabel("Classes for Professor " + professor.getProfessorName());
        greetingLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        topPanel.add(greetingLabel);

        // Table to display classes
        classesTable = new JTable(); // set the model after fetching data
        JScrollPane scrollPane = new JScrollPane(classesTable);

        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        // Finally, fetch data and update the table
        loadClassesForProfessor();
    }

    private void loadClassesForProfessor() {
    // getClasses.php endpoint
    String urlString = "http://cm8tes.com/getClasses.php?professor_id=" + 
                   URLEncoder.encode(professor.getProfessorID(), StandardCharsets.UTF_8);

    java.util.List<String[]> rowData = new ArrayList<>();
    String[] columnNames = {"Class ID", "Class Name", "Section", "Passcode", "Created At", "Expires At"};
    
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
            // Get the classes array
            org.json.JSONArray classesArray = json.getJSONArray("classes");
            for (int i = 0; i < classesArray.length(); i++) {
                JSONObject obj = classesArray.getJSONObject(i);
                String classId = String.valueOf(obj.optInt("class_id"));
                String className = obj.optString("className");
                String section = obj.optString("section");
                String passcode = String.valueOf(obj.optInt("passcode"));
                String createdAt = obj.optString("created_at");
                String expiresAt = obj.optString("passcode_expires");
                rowData.add(new String[] {classId, className, section, passcode, createdAt, expiresAt});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + json.optString("message"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Create table model and set it on your JTable
    String[][] rowArray = rowData.toArray(new String[0][]);
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(rowArray, columnNames);
    classesTable.setModel(model);
}


    // For testing
    public static void main(String[] args) {
        // For local test, pass a dummy professor
        Professor dummyProf = new Professor("Dr. Smith", "drsmith@example.com", "DR12345");
        new ClassDashboard(dummyProf).setVisible(true);
    }
}

