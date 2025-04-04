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
    private final JTextArea chatArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private int classId; // Current selected class ID
    private final Professor professor;
    private final JComboBox<ClassItem> classComboBox; // To let professor choose a class

    public ChatDialog(JFrame parent, Professor professor) {
        super(parent, "Saki Chat", true);
        this.professor = professor;
        setLayout(new BorderLayout(10, 10));
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Apply padding around the dialog content
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        
        getContentPane().setBackground(Color.WHITE);

        // Top panel for class selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JLabel classLabel = new JLabel("Select Class:");
        classLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(classLabel);

        classComboBox = new JComboBox<>();
        classComboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        classComboBox.setBackground(Color.WHITE);
        topPanel.add(classComboBox);
        add(topPanel, BorderLayout.NORTH);
        loadProfessorClasses();

        // Chat area with modern styling
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(new Color(255, 255, 255));
        chatArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Input panel with text field and arrow up button
       JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
       inputField = new HintTextField("Chat With Saki Here");
       inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
       inputField.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLineBorder(new Color(255, 255, 255)),
         new EmptyBorder(5, 10, 5, 10)
         ));
       
       // Create arrow up button instead of a send button
       sendButton = new form.FancyHoverButton("\u2191"); // Unicode for up arrow
       sendButton.setFont(new Font("SansSerif", Font.BOLD, 18));
       sendButton.setBackground(new Color(255, 255, 255));
       //sendButton.setForeground(Color.WHITE);
       sendButton.setFocusPainted(false);
       sendButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
       sendButton.setToolTipText("Send message");
       inputPanel.add(inputField, BorderLayout.CENTER);
       inputPanel.setBackground(new Color(255, 255, 255));
       inputPanel.add(sendButton, BorderLayout.EAST);
       add(inputPanel, BorderLayout.SOUTH);
       
       


      // Listeners for sending messages remain the same
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
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                String urlString = "";
                try {
                    urlString = "http://cm8tes.com/getClasses.php?professor_id=" +
                            URLEncoder.encode(professor.getProfessorID(), StandardCharsets.UTF_8.toString());
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    // Using try-with-resources to ensure streams are closed properly
                    try (BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            response.append(line);
                        }
                        JSONObject json = new JSONObject(response.toString());
                        if ("success".equalsIgnoreCase(json.optString("status"))) {
                            JSONArray classesArray = json.getJSONArray("classes");
                            SwingUtilities.invokeLater(() -> {
                                for (int i = 0; i < classesArray.length(); i++) {
                                    JSONObject obj = classesArray.getJSONObject(i);
                                    int id = obj.optInt("class_id");
                                    String name = obj.optString("className");
                                    String section = obj.optString("section");
                                    String display = name + " - " + section;
                                    classComboBox.addItem(new ClassItem(id, display));
                                }
                                if (classComboBox.getItemCount() > 0) {
                                    ClassItem selected = (ClassItem) classComboBox.getItemAt(0);
                                    classId = selected.id;
                                }
                            });
                        } else {
                            SwingUtilities.invokeLater(() ->
                                    JOptionPane.showMessageDialog(ChatDialog.this, "Failed to load classes."));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(ChatDialog.this, "Error loading classes: " + ex.getMessage()));
                }
                return null;
            }
        };
        worker.execute();
    }

    // Process input text and send to ChatProcess along with the selected class_id and its context string
    private void processInput() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;
        appendChat("You: " + userText);
        inputField.setText("");

        ClassItem selected = (ClassItem) classComboBox.getSelectedItem();
        int selectedClassId = (selected != null) ? selected.id : classId;
        String classContext = (selected != null) ? selected.toString() : "Unknown Class";

        // Use SwingWorker for background processing
        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                // ChatProcess should handle network calls or heavy lifting
                return ChatProcess.processUserMessage(userText, selectedClassId, classContext);
            }

            @Override
            protected void done() {
                try {
                    String response = get();
                    appendChat("Saki: " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                    appendChat("Saki: [Error processing message]");
                }
            }
        };
        worker.execute();
    }

    public void appendChat(String message) {
        chatArea.append(message + "\n\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    // Inner class to hold class details for the combo box
    private static class ClassItem {
        final int id;
        final String display;

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
