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
import javax.swing.border.EmptyBorder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;
import com.formdev.flatlaf.FlatLightLaf;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import javax.swing.UIManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.net.HttpURLConnection;
import java.lang.Exception;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import java.util.*;
import java.util.Map;

public class profChat extends JDialog {
    private final JTextArea chatArea;
    private final JTextField inputField;
    private final JButton sendButton;
    private final Professor professor;
    public final JComboBox<otherProfessors> professorComboBox;
    
    public profChat(JFrame parent, Professor professor){
        super(parent, "Messenger", true);
        this.professor = professor;
        setLayout(new BorderLayout(10,10));
        setSize(600,500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        //Apply padding around the dialog content
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10,10,10,10));
        
        getContentPane().setBackground(Color.WHITE);
        
        //Top panel for professor selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        JLabel professorSelection = new JLabel("Select Professor:");
        professorSelection.setFont(new Font("SansSerif", Font.BOLD, 14));
        topPanel.add(professorSelection);
        
        //Create comboBox of professors
        professorComboBox = new JComboBox();
        professorComboBox.setFont(new Font("SansSerif", Font.PLAIN,14));
        professorComboBox.setBackground(Color.WHITE);
        professorComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
              otherProfessors sel = (otherProfessors)e.getItem();
              loadHistory(sel.professor.getProfessorID());
            }
        });

        topPanel.add(professorComboBox);
        loadComboBox();
        add(topPanel, BorderLayout.NORTH);
        
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
        
         // after classComboBox… -> Add button
       JButton addButton = new JButton("\u002b");
       addButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
       addButton.setBackground(Color.WHITE);
       addButton.setFocusPainted(false);
       addButton.addActionListener(e -> addProfessor());
       topPanel.add(addButton);
       
       //Delete button
       JButton removeButton = new JButton("\u2212"); // Unicode minus sign
       removeButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
       removeButton.setBackground(Color.WHITE);
       removeButton.setFocusPainted(false);
       removeButton.addActionListener(ev -> {
        otherProfessors sel = (otherProfessors) professorComboBox.getSelectedItem();
        System.out.println("Selected professor id: " + sel.professor.getProfessorID()); //Null
        
        if (sel == null) return;
        
        //Extract the Professor object and its ID
        Professor selectedProf = sel.professor;
        System.out.println("Selected professor id: " + selectedProf.getProfessorID()); //Null

        
        int choice = JOptionPane.showConfirmDialog(
            profChat.this,
            "Remove \"" + sel.display + "\" from your contacts?",
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            // (A) Delete from DB
            deleteProfessorFromDatabase(this.professor.getProfessorID(), selectedProf.getProfessorID());
            // (B) Remove from combo box
            professorComboBox.removeItem(sel);
            // (C) Clear chat area if they were selected
            chatArea.setText("");
        }
    });

       topPanel.add(removeButton);

        //TO DO Delete conversation button
       JButton deleteConversationButton = new JButton("🗑️"); // Unicode minus sign
       deleteConversationButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
       deleteConversationButton.setBackground(Color.WHITE);
       deleteConversationButton.setFocusPainted(false);
       deleteConversationButton.addActionListener(ev -> {
 
    });
       topPanel.add(deleteConversationButton);
       
       JScrollPane scrollPane = new JScrollPane(chatArea);
      scrollPane.setBorder(null);
      scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
      scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

       add(scrollPane, BorderLayout.CENTER);
       
         // Input panel with text field and arrow up button
       JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
       inputField = new HintTextField("Chat Here");
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
       sendButton.addActionListener(evt -> {
            String text = inputField.getText().trim(); if (text.isEmpty()) return;
            otherProfessors sel = (otherProfessors)professorComboBox.getSelectedItem();

            // 1) encrypt
            Map<String,String> enc;
            try{
                enc = AESUtil.encrypt(text);
            } catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(parent, "Encryption error: " + 
                        e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return; //stop if encryption fails
            }
            
            String c  = enc.get("cipher");
            String iv = enc.get("iv");

            // 2) POST to PHP
            new Thread(() -> {
              try{
                JSONObject resp = httpPost(
                  "https://cm8tes.com/postMessages.php",
                  Map.of(
                    "sender_id",   professor.getProfessorID(),
                    "receiver_id", sel.professor.getProfessorID(),
                    "cipher",      c,
                    "iv",          iv
                  )
                );
              } catch(Exception ex){
                  ex.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    ((Frame) null),
                    "Network error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            });
              }
              // handle resp if needed
            }).start();

            // 3) append locally
            chatArea.append("Me: " + text + "\n");
            inputField.setText("");
       });

       inputPanel.add(inputField, BorderLayout.CENTER);
       inputPanel.setBackground(new Color(255, 255, 255));
       inputPanel.add(sendButton, BorderLayout.EAST);
       add(inputPanel, BorderLayout.SOUTH);
        
       Executors.newSingleThreadScheduledExecutor()
  .scheduleAtFixedRate(() -> {
        SwingUtilities.invokeLater(() -> {
          otherProfessors sel = (otherProfessors) professorComboBox.getSelectedItem();
          if (sel != null) {
            loadHistory(sel.professor.getProfessorID());
          }
        });
      }, 2, 2, TimeUnit.SECONDS);

}
    
    private void addProfessor() {
    String idToAdd = JOptionPane.showInputDialog(
        profChat.this,
        "Enter the Professor ID to add:",
        "Add Professor",
        JOptionPane.PLAIN_MESSAGE
    );
    if (idToAdd == null || idToAdd.trim().isEmpty()) {
        return;
    }
    idToAdd = idToAdd.trim();

    // 1) Duplicate check: return only if you actually find a match
    for (int i = 0; i < professorComboBox.getItemCount(); i++) {
        otherProfessors existing = (otherProfessors) professorComboBox.getItemAt(i);
        String existingId = existing.professor.getProfessorID();
        if (existingId != null && existingId.equals(idToAdd)) {
            JOptionPane.showMessageDialog(
                profChat.this,
                "Professor \"" + existing.professor.getProfessorName() +"\n with id:  "+idToAdd + "\" is already in your list.",
                "Already Added",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;  // <-- only return when `existingId.equals(idToAdd)` is true
        }
    }

    // 2) If we get here, no duplicate was found. Now fetch from PHP.
    Professor foundProfessor = fetchProfessorById(idToAdd);
    if (foundProfessor == null) {
        JOptionPane.showMessageDialog(
            profChat.this,
            "No professor found with ID: " + idToAdd + "\nInvalid ID",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }
    saveContactToDatabase(professor.getProfessorID(), foundProfessor.getProfessorID(), foundProfessor);

    
}

    
    private static Professor fetchProfessorById(String professorId){
            try{
                String urlString ="http://cm8tes.com/getProfessors.php?professor_id=" +
                       URLEncoder.encode(String.valueOf(professorId), StandardCharsets.UTF_8.toString());
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                
                int code = conn.getResponseCode();
                InputStream is = (code >= 200 && code < 300)
                                ? conn.getInputStream()
                                : conn.getErrorStream();
                
                StringBuilder sb = new StringBuilder();
                try(BufferedReader r = new BufferedReader(new InputStreamReader(is, 
                                        StandardCharsets.UTF_8))){
                    String line;
                    while ((line = r.readLine()) != null){
                        sb.append(line);
                    }
                }
                
                //Test out
                String raw = sb.toString();
                //System.out.println("JSon Rawreponse: " + raw);
                
                
                
                JSONObject response = new JSONObject(sb.toString());
                if(response.getString("status").equals("success")){
                    String id = response.getString("professor_id");
                    String name = response.getString("professorName");
                    //System.out.println("Parsed id:   " + id);
                    //System.out.println("Parsed name: " + name);

                    Professor fetchedProfessor = new Professor(name, null, id);
                    //System.out.println("professor.getProfessorName(): " + fetchedProfessor.getProfessorName());

                    return fetchedProfessor;
                }
                else{
                    //404 or error from the php
                    return null;
                }
                        
            } catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
            
   }
            
    private void saveContactToDatabase(String owner_id, String contact_id, Professor foundProfessor) {
        System.out.println("DEBUG: owner_id=" + owner_id + ", contact_id=" + contact_id);
        if (owner_id == null || contact_id == null) {
        System.err.printf("Cannot save contact – missing ID(s): owner_id=%s, contact_id=%s%n",
                          owner_id, contact_id);
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this,
                "Error Saving Contact: missing owner or contact ID",
                "Error",
                JOptionPane.ERROR_MESSAGE)
        );
        return;
    }
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL("https://cm8tes.com/addProfessorContact.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty(
                  "Content-Type","application/x-www-form-urlencoded; charset=UTF-8"
                );

                String body = "owner_id="  + URLEncoder.encode(
                                  owner_id, StandardCharsets.UTF_8.name())
                            + "&contact_id=" + URLEncoder.encode(
                                  contact_id,    StandardCharsets.UTF_8.name());

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body.getBytes(StandardCharsets.UTF_8));
                }

                int code = conn.getResponseCode();
                InputStream is = (code>=200 && code<300)
                              ? conn.getInputStream()
                              : conn.getErrorStream();
                StringBuilder sb = new StringBuilder();
                try (BufferedReader rd = new BufferedReader(
                      new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = rd.readLine()) != null) sb.append(line);
                }

                JSONObject json = new JSONObject(sb.toString());
                String status  = json.optString("status", "error");
                String message = json.optString("message","Unknown error");

                // Update UI on EDT
                SwingUtilities.invokeLater(() -> {
                    if ("success".equalsIgnoreCase(status)) {
                        professorComboBox.addItem(
                            new otherProfessors(foundProfessor, foundProfessor.getProfessorName())
                        );
                        JOptionPane.showMessageDialog(
                            this,
                            message,
                            "Contact Saved",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            message,
                            "Error Saving Contact",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                        this,
                        "Network error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                });
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
    
     private void loadComboBox() {
        // Clear any existing items
        professorComboBox.removeAllItems();

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                // Build URL for fetching contacts
                String ownerId = URLEncoder.encode(professor.getProfessorID(), StandardCharsets.UTF_8.name());
                URL url = new URL("https://cm8tes.com/loadContacts.php?owner_id=" + ownerId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int code = conn.getResponseCode();
                InputStream is = (code >= 200 && code < 300)
                                ? conn.getInputStream()
                                : conn.getErrorStream();

                StringBuilder sb = new StringBuilder();
                try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }
                }

                // Debug: print raw response to catch unexpected content
                String raw = sb.toString();
                System.out.println("DEBUG loadComboBox raw response: [" + raw + "]");

                // Trim BOM or whitespace before parsing
                raw = raw.trim();

                JSONObject json;
                try {
                    json = new JSONObject(raw);
                } catch (JSONException je) {
                    // Print error and raw for analysis
                    System.err.println("JSON parse error in loadComboBox: " + je.getMessage());
                    return;
                }

                if ("success".equalsIgnoreCase(json.optString("status"))) {
                    JSONArray contacts = json.optJSONArray("contacts");
                    if (contacts != null) {
                        // Populate combo box on the EDT
                        SwingUtilities.invokeLater(() -> {
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject obj = contacts.optJSONObject(i);
                                if (obj == null) continue;
                                String id = obj.optString("professor_id", null);
                                String name = obj.optString("professorName", "");
                                if (id != null) {
                                    Professor p = new Professor(name, null, id);
                                    professorComboBox.addItem(new otherProfessors(p, name));
                                }
                            }
                        });
                    }
                } else {
                    System.err.println("Failed to load contacts: " + json.optString("message"));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }
     private static void deleteProfessorFromDatabase(String owner_id, String contact_id){
     System.out.println("DELETING: owner_id=" + owner_id + ", contact_id=" + contact_id);

    new Thread(() -> {
        HttpURLConnection conn = null;
        try {
            // 1) Open connection to your delete endpoint
            URL url = new URL("https://cm8tes.com/deleteChat.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8"
            );
            conn.setDoOutput(true);

            // 2) Build and send the form body
            String urlParameters = 
                  "owner_id=" + URLEncoder.encode(owner_id, "UTF-8")
                + "&contact_id="         + URLEncoder.encode(contact_id,   "UTF-8");
            
            try (OutputStream os = conn.getOutputStream()) {
                os.write(urlParameters.getBytes(StandardCharsets.UTF_8));
            }

            // 3) Check HTTP response
            int code = conn.getResponseCode();
            System.out.println("▶ deleteChat returned HTTP " + code);

            // 4) Read JSON response
            InputStream is = (code >= 200 && code < 300)
                          ? conn.getInputStream()
                          : conn.getErrorStream();
            StringBuilder sb = new StringBuilder();
            try (BufferedReader in = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
            }

            JSONObject json = new JSONObject(sb.toString());
            String status  = json.optString("status",  "error");
            String message = json.optString("message", "Unknown error");

            // 5) Update UI on the EDT
            SwingUtilities.invokeLater(() -> {
                if ("success".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(
                        null,
                        message,
                        "Contact Deleted Successfully",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        message,
                        "Error Deleting Contact",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    null,
                    "Network error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            });
        } finally {
            if (conn != null) conn.disconnect();
        }
    }).start();
}

    
        //Inner class to hold other professor details for the combo Box
        public static class otherProfessors{
            final String display;
            final Professor professor;
            
            public otherProfessors(Professor professor, String display){
                this.professor = professor;
                this.display = display;
            }
            
            @Override
            public String toString(){
                return display;
        }
    }
    
    // Simple HTTP GET → String
private String httpGet(String urlStr) throws IOException {
    URL url = new URL(urlStr);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    try (BufferedReader in = new BufferedReader(
           new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) sb.append(line);
        return sb.toString();
    } finally {
        con.disconnect();
    }
}

// Simple HTTP POST form → JSON response
private JSONObject httpPost(String urlStr, Map<String,String> params) throws IOException, JSONException {
    URL url = new URL(urlStr);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("POST");
    con.setDoOutput(true);
    con.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

    // build body: key1=val1&key2=val2...
    StringJoiner sj = new StringJoiner("&");
    for (var e : params.entrySet()) {
        sj.add(URLEncoder.encode(e.getKey(),"UTF-8")
             + "="
             + URLEncoder.encode(e.getValue(),"UTF-8"));
    }
    try (OutputStream os = con.getOutputStream()) {
        os.write(sj.toString().getBytes(StandardCharsets.UTF_8));
    }

    try (BufferedReader in = new BufferedReader(
           new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) sb.append(line);
        return new JSONObject(sb.toString());
    } finally {
        con.disconnect();
    }
}

private void loadHistory(String otherId) {
  new Thread(() -> {
    try {
      String u1 = URLEncoder.encode(professor.getProfessorID(),"UTF-8");
      String u2 = URLEncoder.encode(otherId,             "UTF-8");
      String json = httpGet("https://cm8tes.com/getMessages.php?user1=" + u1 + "&user2=" + u2);

      JSONArray arr = new JSONArray(json);
      StringBuilder buf = new StringBuilder();
      for (int i = 0; i < arr.length(); i++) {
        JSONObject m = arr.getJSONObject(i);
        String from = m.getString("from");
        String text = m.getString("plaintext");
        String time = m.getString("time");
        buf.append(from).append(": ").append(text).append("\n");
      }

      SwingUtilities.invokeLater(() -> chatArea.setText(buf.toString()));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }).start();
}

    
    public static void main(String[] args) {
        // set FlatLaf globally
        FlatLightLaf.setup();

        // tweak scrollbar to be thin, rounded, no arrows
        UIManager.put("ScrollBar.width",     6);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.trackArc", 999);
        UIManager.put("ScrollBar.showButtons", false);

        Professor professor = new Professor("Dr Smith", "example@123.com", "dsmith");
        // Prepare the objects that ChatDialog needs:
        //    A parent frame 
        JFrame parentFrame = null; // 



        // Launch the dialog on the EDT
        SwingUtilities.invokeLater(() -> {
          profChat chat = new profChat(parentFrame, professor);
          chat.setVisible(true);
        });
  }
    
}
