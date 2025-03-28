/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.json.JSONArray;
import org.json.JSONObject;

public class ClassSelectionDashboard extends JFrame {

    private Professor professor;       // Professor object passed in
    private JTable classesTable;       // Table to display classes
    private JButton viewAttendanceBtn; // Button to view attendance for selected class
    private JTextField searchField;
    private TableRowSorter<TableModel> rowSorter;


    public ClassSelectionDashboard(Professor professor) {
        this.professor = professor;
        setTitle("Class Selection Dashboard for " + professor.getProfessorName());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadClassesForProfessor();
    }

    private void initComponents() {
        // Create a header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        JLabel headerLabel = new JLabel("Select a Class to View Attendance");
        headerLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        
     

        // Table to list classes
        classesTable = new JTable();
        classesTable.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        classesTable.setRowHeight(25);
        JScrollPane tableScrollPane = new JScrollPane(classesTable);

        // Button to view attendance for selected class
        viewAttendanceBtn = new JButton("View Attendance");
        viewAttendanceBtn.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        viewAttendanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAttendanceForSelectedClass();
            }
        });

        // Bottom panel for the button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(viewAttendanceBtn);

        // Main panel layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
         // Create a search panel with a label and a text field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search: "));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        mainPanel.add(searchPanel, BorderLayout.SOUTH);
        
        // Add a listener to the search field to filter table data
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateFilter();
            }
        });

        getContentPane().add(mainPanel);
    }
    
    private void updateFilter() {
        String text = searchField.getText();
        if (rowSorter != null) {
            if (text.trim().length() == 0) {
                rowSorter.setRowFilter(null);
            } else {
                // Filter to match the text in any column 
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
    }

   private void loadClassesForProfessor() {
    // Build the URL to get classes for this professor
    String urlString = "";
    try {
        urlString = "http://cm8tes.com/getClasses.php?professor_id=" +
                    URLEncoder.encode(professor.getProfessorID(), StandardCharsets.UTF_8.toString());
    } catch (Exception e) {
        e.printStackTrace();
    }

    ArrayList<String[]> rowData = new ArrayList<>();
    // Define column names. The first column "No." is  sequential number.
    String[] columnNames = {"No.", "Class ID", "Class Name", "Section", "Passcode", "Created At", "Expires At"};
    
    try {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        JSONObject json = new JSONObject(response.toString());
        if ("success".equalsIgnoreCase(json.optString("status"))) {
            JSONArray classesArray = json.getJSONArray("classes");
            for (int i = 0; i < classesArray.length(); i++) {
                JSONObject obj = classesArray.getJSONObject(i);
                // Get the actual class ID from the DB
                String classId = String.valueOf(obj.optInt("class_id"));
                String className = obj.optString("className");
                String section = obj.optString("section");
                String passcode = String.valueOf(obj.optInt("passcode"));
                String createdAt = obj.optString("created_at");
                String expiresAt = obj.optString("passcode_expires");
                // Create a display number based on the order (i+1)
                String displayNo = String.valueOf(i + 1);
                // Add a new row where the first column is the sequential number
                rowData.add(new String[] {displayNo, classId, className, section, passcode, createdAt, expiresAt});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + json.optString("message"),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                                      "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Convert rowData to a 2D array
    String[][] data = rowData.toArray(new String[0][]);
    javax.swing.table.DefaultTableModel model =
            new javax.swing.table.DefaultTableModel(data, columnNames);
    
        classesTable.setModel(model);
        
        rowSorter = new TableRowSorter<>(classesTable.getModel());
        classesTable.setRowSorter(rowSorter);

        
        // Hide the "Class ID" column (the second column, index 1) so that only sequential numbers show.
        classesTable.removeColumn(classesTable.getColumnModel().getColumn(1));
}


    private void viewAttendanceForSelectedClass() {
    int selectedRow = classesTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a class from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    // Directly access the hidden column from the model (column index 1)
        int classId = Integer.parseInt(((javax.swing.table.DefaultTableModel)classesTable.getModel())
                            .getValueAt(selectedRow, 1).toString());
    // Open the AttendanceDashboard for the selected class
    AttendanceDashboard attDash = new AttendanceDashboard(classId);
    attDash.setVisible(true);
}


    
    // For testing purposes
    public static void main(String[] args) {
        // Dummy professor for testing
        Professor dummyProf = new Professor("Dr. Smith", "drsmith@example.com", "DR12345");
        SwingUtilities.invokeLater(() -> new ClassSelectionDashboard(dummyProf).setVisible(true));
    }
}
