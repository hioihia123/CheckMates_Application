package form;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class AttendanceDashboard extends JFrame {
    private int classId;
    private JTable recordsTable;
    private JTextField searchField;
    private TableRowSorter<TableModel> rowSorter;

    // Constructor that uses class_id to retrieve attendance records for that specific class
    public AttendanceDashboard(int classId) {
        this.classId = classId;
        setTitle("Attendance Records for Class " + classId);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        loadAttendanceRecordsByClass();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Attendance Records", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        headerLabel.setForeground(new Color(50, 50, 50));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Create a search panel with a label and a text field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search: "));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        recordsTable = new JTable();
        recordsTable.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        recordsTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(recordsTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

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

    private void loadAttendanceRecordsByClass() {
        try {
            String urlString = "http://cm8tes.com/getAttendanceRecordsByClass.php?class_id=" + 
                                URLEncoder.encode(String.valueOf(classId), StandardCharsets.UTF_8.toString());
            System.out.println("Fetching: " + urlString); // Debugging output

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
                JSONArray recordsArray = json.getJSONArray("records");
                String[] columnNames = {"Student ID", "Name", "Date", "Time"};
                ArrayList<String[]> rowData = new ArrayList<>();
                for (int i = 0; i < recordsArray.length(); i++) {
                    JSONObject record = recordsArray.getJSONObject(i);
                    String studentId = record.optString("studentId");
                    String name = record.optString("name");
                    String date = record.optString("date");
                    String time = record.optString("time");
                    rowData.add(new String[]{studentId, name, date, time});
                }
                String[][] data = rowData.toArray(new String[0][]);
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                recordsTable.setModel(model);
                
                // Initialize the row sorter and associate it with the table model
                rowSorter = new TableRowSorter<>(model);
                recordsTable.setRowSorter(rowSorter);
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + json.optString("message"),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // For testing purposes
    public static void main(String[] args) {
        // For testing, we use a hard-coded class ID.
        // In a real scenario, you would retrieve this from a selection.
        SwingUtilities.invokeLater(() -> new AttendanceDashboard(1).setVisible(true));
    }
}

