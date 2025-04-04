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
import javax.imageio.ImageIO;
import java.io.IOException;

// Import iText classes
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;


public class AttendanceDashboard extends JFrame {
    private int classId;
    private JTable recordsTable;
    private JTextField searchField;
    private TableRowSorter<TableModel> rowSorter;
    private boolean oldTimeyMode;

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

    public AttendanceDashboard(int classId, boolean oldTimeyMode) {
        this.classId = classId;
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
        
        FancyHoverButton2 exportButton = new FancyHoverButton2("Export to PDF");
        exportButton.addActionListener(new ActionListener() {
            @Override
           public void actionPerformed(ActionEvent e) {
             exportSelectedRowsToPDF();
           }
        });
        // Then add the button to an appropriate panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(exportButton);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        recordsTable = new JTable();
        recordsTable.setFont(modernFont);
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
                // If your table has been sorted, convert the view index to the model index:
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AttendanceDashboard(1, true).setVisible(true));
    }
}