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
import java.util.Random;
import javax.swing.UIManager;

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
        topPanel.add(professorComboBox);
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
        
         // after classComboBox…
       JButton addButton = new JButton("\u002b");
       addButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
       addButton.setBackground(Color.WHITE);
       addButton.setFocusPainted(false);
       addButton.addActionListener(e -> addProfessor());
       topPanel.add(addButton);
       
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
       inputPanel.add(inputField, BorderLayout.CENTER);
       inputPanel.setBackground(new Color(255, 255, 255));
       inputPanel.add(sendButton, BorderLayout.EAST);
       add(inputPanel, BorderLayout.SOUTH);
        
       
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
                "Professor \"" + idToAdd + "\" is already in your list.",
                "Already Added",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;  // <-- only return when `existingId.equals(idToAdd)` is true
        }
    }

    // 2) If we get here, no duplicate was found. Now fetch from PHP/DB.
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

    // 3) Add the new professor to the combo box
    String displayName = foundProfessor.getProfessorName();
    //System.out.println(displayName);
    professorComboBox.addItem(new otherProfessors(foundProfessor, displayName));
    JOptionPane.showMessageDialog(
        profChat.this,
        "Added Professor \"" + displayName + "\" to your list.",
        "Success",
        JOptionPane.INFORMATION_MESSAGE
    );
}

    
    public static Professor fetchProfessorById(String professorId){
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

                    Professor fetchedProfessor = new Professor(name, id, null);
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
        //    a) A parent frame 
        JFrame parentFrame = null; // or: new Dashboard();

        //    b) A Professor object 


        // Launch the dialog on the EDT
        SwingUtilities.invokeLater(() -> {
          profChat chat = new profChat(parentFrame, professor);
          chat.setVisible(true);
        });
  }
    
}
