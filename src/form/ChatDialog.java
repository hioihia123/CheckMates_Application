package form;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
        setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(parent);

        // Top panel for class selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Class:"));
        classComboBox = new JComboBox<>();
        topPanel.add(classComboBox);
        add(topPanel, BorderLayout.NORTH);
        loadProfessorClasses();

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // Input panel with text field and send button
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
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

    // Load professor classes from your backend (similar to your ClassSelectionDashboard)
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
                    classId = ((ClassItem) classComboBox.getItemAt(0)).id;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load classes.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Process input text and send to ChatProcess along with the selected class_id
    private void processInput() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;
        appendChat("You: " + userText);
        inputField.setText("");

        new Thread(() -> {
            String response = ChatProcess.processUserMessage(userText, classId);
            SwingUtilities.invokeLater(() -> appendChat("Saki: " + response));
        }).start();
    }

    public void appendChat(String message) {
        chatArea.append(message + "\n");
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
