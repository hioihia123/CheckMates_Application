/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.table.DefaultTableCellRenderer;


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
        // Create a header panel with enhanced styling
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(245, 245, 245));
        JLabel headerLabel = new JLabel("Select a Class to View Attendance");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        headerLabel.setForeground(new Color(50, 50, 50));
        headerPanel.add(headerLabel);

        // Table to list classes with modern font
        classesTable = new JTable();
        classesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classesTable.setRowHeight(30);
        JScrollPane tableScrollPane = new JScrollPane(classesTable);
        
     // Set the custom header renderer for a modern flat look
    classesTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel header = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        header.setHorizontalAlignment(JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(66, 133, 244));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return header;
    }
});



        // Button to view attendance for selected class with modern styling
        viewAttendanceBtn = new JButton("View Attendance");
        viewAttendanceBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        viewAttendanceBtn.setBackground(new Color(66, 133, 244));
        viewAttendanceBtn.setForeground(Color.WHITE);
        viewAttendanceBtn.setFocusPainted(false);
        viewAttendanceBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAttendanceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAttendanceForSelectedClass();
            }
        });

        // Panel for the attendance button with spacing
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(viewAttendanceBtn);

        // Create a search panel with enhanced styling
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(245, 245, 245));
        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(searchLabel);
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchPanel.add(searchField);
        
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

        // Combine searchPanel and buttonPanel into one panel using BoxLayout (vertical)
        JPanel bottomCombinedPanel = new JPanel();
        bottomCombinedPanel.setLayout(new BoxLayout(bottomCombinedPanel, BoxLayout.Y_AXIS));
        bottomCombinedPanel.setBackground(new Color(245, 245, 245));
        bottomCombinedPanel.add(searchPanel);
        bottomCombinedPanel.add(buttonPanel);

        // Main panel layout with consistent background and spacing
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomCombinedPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }

    private void updateFilter() {
        String text = searchField.getText();
        if (rowSorter != null) {
            if (text.trim().length() == 0) {
                rowSorter.setRowFilter(null);
            } else {
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
    try {
        UIManager.setLookAndFeel(new FlatLightLaf());
    } catch (Exception ex) {
        System.err.println("Failed to initialize LaF");
    }
    // Launch your dashboard
    Professor dummyProf = new Professor("Dr. Smith", "drsmith@example.com", "DR12345");
    SwingUtilities.invokeLater(() -> new ClassSelectionDashboard(dummyProf).setVisible(true));
}

}
