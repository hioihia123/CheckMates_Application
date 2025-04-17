package form;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.imageio.ImageIO;

// Import iText classes
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class AttendanceDashboard extends JFrame {
    private int classId;
    private JTable recordsTable;
    private JTextField searchField;
    private TableRowSorter<TableModel> rowSorter;
    private boolean oldTimeyMode;
    private String className;
    private String section;

    // Old-timey style properties
    private Color typewriterInk = new Color(50, 40, 30);
    private Font typewriterFont = new Font("Courier New", Font.BOLD, 14);
    private Font titleFont = new Font("Courier New", Font.BOLD, 24);
    private Image backgroundImage;
    private Color parchmentColor = new Color(253, 245, 230, 200);

    // Modern style properties
    private Color modernTextColor = new Color(60, 60, 60);
    private Font modernFont = new Font("Helvetica Neue", Font.PLAIN, 16);
    private Font modernTitleFont = new Font("Helvetica Neue", Font.BOLD, 24);
    private Color modernBackground = Color.WHITE;

    public AttendanceDashboard(int classId, String className,String section,boolean oldTimeyMode) {
        this.classId = classId;
        this.className = className;
        this.section = section;
        this.oldTimeyMode = oldTimeyMode;
        setTitle("Attendance Records for Class " + classId);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (oldTimeyMode) {
            try {
                backgroundImage = ImageIO.read(getClass().getResource("parchmentColor.jpg"));
            } catch (IOException | IllegalArgumentException e) {
                backgroundImage = null;
            }
        }

        setupLookAndFeel();
        initComponents();
        loadAttendanceRecordsByClass();
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

        // Header label
        JLabel headerLabel = new JLabel("Attendance Records", SwingConstants.CENTER);
        headerLabel.setFont(titleFont);
        headerLabel.setForeground(typewriterInk);
        headerLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, typewriterInk));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(typewriterFont);
        searchLabel.setForeground(typewriterInk);
        searchPanel.add(searchLabel);

        searchField = new JTextField(20);
        searchField.setFont(typewriterFont);
        searchField.setForeground(typewriterInk);
        searchField.setBackground(parchmentColor);
        searchField.setBorder(BorderFactory.createLineBorder(typewriterInk));
        searchPanel.add(searchField);

        mainPanel.add(searchPanel, BorderLayout.SOUTH);

        // Table setup
        recordsTable = new JTable() {
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

        recordsTable.setFont(typewriterFont);
        recordsTable.setForeground(typewriterInk);
        recordsTable.setOpaque(false);
        recordsTable.setGridColor(typewriterInk);
        recordsTable.setRowHeight(30);
        recordsTable.setSelectionBackground(new Color(200, 180, 150, 200));
        recordsTable.setSelectionForeground(typewriterInk);
        recordsTable.setBorder(BorderFactory.createLineBorder(typewriterInk));

    
        JTableHeader header = recordsTable.getTableHeader();
        header.setFont(typewriterFont);
        header.setForeground(typewriterInk);
        header.setBackground(new Color(253, 245, 230, 220));
        header.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(recordsTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(typewriterInk));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        // Add search listener
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

    private void initModernComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Attendance Records", SwingConstants.CENTER);
        headerLabel.setFont(modernTitleFont);
        headerLabel.setForeground(modernTextColor);
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
      

        // Create a search panel with a label and a text field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(new JLabel("Search: "));
        searchField = new JTextField(20);
        searchPanel.add(searchField);
        mainPanel.add(searchPanel, BorderLayout.SOUTH);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        // Add Student button
        form.FancyHoverButton addButton = new form.FancyHoverButton("Add");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        //addButton.addActionListener(e -> addNewStudent());
        buttonPanel.add(addButton);

        // Edit Student button
        form.FancyHoverButton editButton = new form.FancyHoverButton("Edit");
        editButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        // editButton.addActionListener(e -> editSelectedStudent());
        buttonPanel.add(editButton);

        // Delete Student button
        form.FancyHoverButton deleteButton = new form.FancyHoverButton("Delete ");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        deleteButton.addActionListener(e -> {
            deleteSelectedStudent();
        });
        buttonPanel.add(deleteButton);

        form.FancyHoverButton exportButton = new form.FancyHoverButton("Export to PDF ");
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        buttonPanel.add(exportButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        //FancyHoverButton2 exportButton = new FancyHoverButton2("Export to PDF");
        exportButton.addActionListener(new ActionListener() {
            @Override
           public void actionPerformed(ActionEvent e) {
             exportTableToPDF();
           }
        });
        // Then add the button to an appropriate panel
        /*
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(exportButton);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        */


        recordsTable = new JTable();
        recordsTable.setFont(modernFont);
        recordsTable.setRowHeight(25);
        recordsTable.setBackground(Color.WHITE);
        
        recordsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            final Color alterColor = new Color(255, 255, 255);
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                    boolean hasFocus, int row, int column){
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Zebra striping effect
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : alterColor);
                } else {
                    c.setBackground(Color.RED);
                }
                return c;
            }
        });
        
        // Modern header customization
        JTableHeader header = recordsTable.getTableHeader();
        header.setFont(modernFont.deriveFont(Font.BOLD));
        header.setForeground(modernTextColor);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200))); // Flat bottom border
 

        // customize header renderer for centered text and extra padding
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                return label;
            }
        });

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
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
    }

    private void loadAttendanceRecordsByClass() {
        try {
            String urlString = "http://cm8tes.com/getAttendanceRecordsByClass.php?class_id=" +
                    URLEncoder.encode(String.valueOf(classId), StandardCharsets.UTF_8.toString());
            System.out.println("Fetching: " + urlString);

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
                String[] columnNames = {"No.","Student ID", "Name", "Date", "Time"};
                ArrayList<String[]> rowData = new ArrayList<>();
                for (int i = 0; i < recordsArray.length(); i++) {
                    String displayNo = String.valueOf(i + 1);
                    JSONObject record = recordsArray.getJSONObject(i);
                    String studentId = record.optString("studentId");
                    String name = record.optString("name");
                    String date = record.optString("date");
                    String time = record.optString("time");
                    rowData.add(new String[]{displayNo,studentId, name, date, time});
                }
                String[][] data = rowData.toArray(new String[0][]);
                DefaultTableModel model = new DefaultTableModel(data, columnNames);
                recordsTable.setModel(model);
                
                //Move the entries in the center
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

                for (int i = 0; i < recordsTable.getColumnCount(); i++) {
                  recordsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }

                rowSorter = new TableRowSorter<>(model);
                recordsTable.setRowSorter(rowSorter);
            } else {
                showMessage("Error: " + json.optString("message"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {////comment

            ex.printStackTrace();
            showMessage("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMessage(String message, String title, int messageType) {
        if (oldTimeyMode) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(parchmentColor);
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel messageLabel = new JLabel(message);
            messageLabel.setFont(typewriterFont);
            messageLabel.setForeground(typewriterInk);
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(messageLabel, BorderLayout.CENTER);

            JButton okButton = createTypewriterButton("OK");
            okButton.addActionListener(e -> {
                Window window = SwingUtilities.getWindowAncestor(panel);
                if (window != null) window.dispose();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(parchmentColor);
            buttonPanel.add(okButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            JDialog dialog = new JDialog(this, title, true);
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, message, title, messageType);
        }
    }
    private JButton createModernButton(String text) {
        ClassDashboard.FancyHoverButton button = new ClassDashboard.FancyHoverButton(text);
        button.setFont(modernFont);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return button;
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
    
    private void exportSelectedRowsToPDF() {
    int[] selectedRows = recordsTable.getSelectedRows();
    if (selectedRows.length == 0) {
        showMessage("Please select at least one record to export.", "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    // Let the user choose where to save the PDF file
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save PDF");
    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        try {
            // Create a new document and writer
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
            document.open();
            
            // Create a table with the same number of columns as the JTable
            int numColumns = recordsTable.getColumnCount();
            PdfPTable pdfTable = new PdfPTable(numColumns);
            
            // Add table headers
            for (int i = 0; i < numColumns; i++) {
                pdfTable.addCell(new PdfPCell(new Phrase(recordsTable.getColumnName(i))));
            }
            
            // Add selected rows
            for (int rowIndex : selectedRows) {
               // convert the view index to the model index:
                int modelRow = recordsTable.convertRowIndexToModel(rowIndex);
                for (int col = 0; col < numColumns; col++) {
                    Object cellValue = recordsTable.getModel().getValueAt(modelRow, col);
                    pdfTable.addCell(new PdfPCell(new Phrase(cellValue != null ? cellValue.toString() : "")));
                }
            }
            
            document.add(pdfTable);
            document.close();
            showMessage("PDF exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | java.io.FileNotFoundException ex) {
            ex.printStackTrace();
            showMessage("Error exporting PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    private void exportTableToPDF() {
    // Let the user choose where to save the PDF file
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save PDF");
    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        try {
            // Create a new document and writer
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
            document.open();

            // Create a table with the same number of columns as the JTable
            int numColumns = recordsTable.getColumnCount();
            PdfPTable pdfTable = new PdfPTable(numColumns);

           // Create a header cell with the classes
           PdfPCell headerCell = new PdfPCell(new Phrase(className + "----" + section));

           // Set the cell to span all columns in the table
           headerCell.setColspan(pdfTable.getNumberOfColumns());

           // Add the header cell to the table
           pdfTable.addCell(headerCell);

           //Mark the header row so that it repeats on each page 
           pdfTable.setHeaderRows(1);



            // Add table headers
            for (int i = 0; i < numColumns; i++) {
                pdfTable.addCell(new PdfPCell(new Phrase(recordsTable.getColumnName(i))));
            }

            // Add all rows from the table
            for (int rowIndex = 0; rowIndex < recordsTable.getRowCount(); rowIndex++) {
                // Convert the view index to the model index 
                int modelRow = recordsTable.convertRowIndexToModel(rowIndex);
                
                for (int col = 0; col < numColumns; col++) {
                    Object cellValue = recordsTable.getModel().getValueAt(modelRow, col);
                    pdfTable.addCell(new PdfPCell(new Phrase(cellValue != null ? cellValue.toString() : "")));
                }
            }

            document.add(pdfTable);
            document.close();
            showMessage("PDF exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (DocumentException | java.io.FileNotFoundException ex) {
            ex.printStackTrace();
            showMessage("Error exporting PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    private void deleteSelectedStudent() {
        int selectedRow = recordsTable.getSelectedRow();
        if (selectedRow == -1) {
            // Custom "no selection" notification
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JLabel message = new JLabel("Please select a student to delete");
            message.setFont(oldTimeyMode ? typewriterFont : modernFont);
            message.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
            message.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(message, BorderLayout.CENTER);

            JButton okButton = oldTimeyMode ?
                    createTypewriterButton("OK") :
                    createModernButton("OK");
            okButton.addActionListener(e -> {
                Window window = SwingUtilities.getWindowAncestor(panel);
                if (window != null) window.dispose();
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
            buttonPanel.add(okButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            JDialog dialog = new JDialog(this, "No Selection", true);
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            return;
        }

        // Custom delete confirmation dialog
        JPanel confirmPanel = new JPanel(new BorderLayout());
        confirmPanel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel confirmMessage = new JLabel("Are you sure you want to delete this student?");
        confirmMessage.setFont(oldTimeyMode ? typewriterFont : modernFont);
        confirmMessage.setForeground(oldTimeyMode ? typewriterInk : modernTextColor);
        confirmMessage.setHorizontalAlignment(SwingConstants.CENTER);
        confirmPanel.add(confirmMessage, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);

        JButton yesButton = oldTimeyMode ?
                createTypewriterButton("Yes") :
                createModernButton("Yes");
        yesButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(confirmPanel);
            if (window != null) window.dispose();

            int classId = this.classId;
            //deleteClassFromDatabase(classId);
            String studentId = ((DefaultTableModel)recordsTable.getModel())
                    .getValueAt(selectedRow, 1).toString();
            deleteStudentFromDatabase(this.classId, studentId);

           // deleteStudentFromDatabase(classId,studentId);
        });

        JButton noButton = oldTimeyMode ?
                createTypewriterButton("No") :
                createModernButton("No");
        noButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(confirmPanel);
            if (window != null) window.dispose();
        });

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));

        confirmPanel.add(buttonPanel, BorderLayout.SOUTH);

        JDialog confirmDialog = new JDialog(this, "Confirm Deletion", true);
        confirmDialog.setContentPane(confirmPanel);
        confirmDialog.pack();
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setVisible(true);

    }

    private void deleteStudentFromDatabase(int classId, String studentId) {
        new Thread(() -> {
            try {
                String urlString = "http://cm8tes.com/deleteStudent.php";
                String urlParameters = "class_id=" + classId +"&studentId=" + URLEncoder.encode(studentId, StandardCharsets.UTF_8);

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
                        // Create custom notification panel
                        JPanel panel = new JPanel(new BorderLayout());
                        panel.setBackground(oldTimeyMode ? parchmentColor : Color.WHITE);
                        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                        // Create message label
                        JLabel message = new JLabel("Class deleted successfully");
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

                        loadAttendanceRecordsByClass();
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
                            "Error deleting student: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceDashboard(1,"abc", "007",true).setVisible(true));
    }
}