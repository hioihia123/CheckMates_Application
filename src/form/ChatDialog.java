package form;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatDialog extends JDialog {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private int classId; // Current selected class ID
    private Professor professor;
    private JComboBox<ClassItem> classComboBox; // To let professor choose a class

    // Constructor now accepts the Professor object
    public ChatDialog(JFrame parent, Professor professor) {
        super(parent, "Saki Chat", true);
        this.professor = professor;
        setLayout(new BorderLayout(10, 10));
        setSize(600, 500);
        setLocationRelativeTo(parent);
        
        // Apply padding around the dialog content
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel for class selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JLabel classLabel = new JLabel("Select Class:");
        classLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(classLabel);
        
        classComboBox = new JComboBox<>();
        classComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        topPanel.add(classComboBox);
        add(topPanel, BorderLayout.NORTH);
        loadProfessorClasses();

        // Chat area with modern styling
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(new Color(245, 245, 245));
        chatArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Input panel with text field and send button
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setBackground(new Color(66, 133, 244));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Listeners for sending messages
        sendButton.addActionListener(e -> processInput());
        inputField.addActionListener(e -> processInput());

        // Update classId when selection changes
        classComboBox.addActionListener(e -> {
            ClassItem selected = (ClassItem) classComboBox.getSelectedItem();
            if (selected != null) {
                classId = selected.id;
            }
        });
    }

    // Load professor classes from backend 
    private void loadProfessorClasses() {
        try {
            String urlString = "http://cm8tes.com/getClasses.php?professor_id=" +
                    URLEncoder.encode(professor.getProfessorID(), StandardCharsets.UTF_8.toString());
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            JSONObject json = new JSONObject(response.toString());
            if ("success".equalsIgnoreCase(json.optString("status"))) {
                JSONArray classesArray = json.getJSONArray("classes");
                for (int i = 0; i < classesArray.length(); i++) {
                    JSONObject obj = classesArray.getJSONObject(i);
                    int id = obj.optInt("class_id");
                    String name = obj.optString("className");
                    String section = obj.optString("section");
                    // Create a display text, e.g., "Math 101 - A"
                    String display = name + " - " + section;
                    classComboBox.addItem(new ClassItem(id, display));
                }
                if (classComboBox.getItemCount() > 0) {
                    ClassItem selected = (ClassItem) classComboBox.getItemAt(0);
                    classId = selected.id;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load classes.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Process input text and send to ChatProcess along with the selected class_id and its context string
    private void processInput() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;
        appendChat("You: " + userText);
        inputField.setText("");

        // Retrieve the selected class to get both ID and display text
        ClassItem selected = (ClassItem) classComboBox.getSelectedItem();
        int selectedClassId = selected != null ? selected.id : classId;
        String classContext = selected != null ? selected.toString() : "Unknown Class";

        new Thread(() -> {
            String response = ChatProcess.processUserMessage(userText, selectedClassId, classContext);
            SwingUtilities.invokeLater(() -> appendChat("Saki: " + response));
        }).start();
    }

    public void appendChat(String message) {
        chatArea.append(message + "\n\n");
        // Auto-scroll to the bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    // Inner class to hold class details for the combo box
    private class ClassItem {
        int id;
        String display;

        public ClassItem(int id, String display) {
            this.id = id;
            this.display = display;
        }

        @Override
        public String toString() {
            return display;
        }
    }
}
//April 3: 6-19pm