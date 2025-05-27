/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Dimension;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject; // Make sure to include a JSON library



// Import ZXing libraries for QR code generation
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.util.Iterator;
import java.util.Random;
import java.util.prefs.Preferences;

/**
 *
 * @author nguyenp
 */

public class Dashboard extends javax.swing.JFrame {

    private Professor professor;  // Store professor-specific info
    private ClassDashboard dashboard;
    private JCheckBox chkIP;
    boolean isIPRTurnOn;
    private String collectedIPv6 = "";
    JLabel ipStatusLabel;
     // user prefs to remember if they’ve turned the tour off
    private static final Preferences PREFS = 
        Preferences.userNodeForPackage(Dashboard.class);

    private static final String KEY_SHOW_TOUR = "showCoachMarkTour";
    static boolean show;
    
    private final JPanel notePanel;
    private final ModernButton noteButton;



    /**
     * Creates new form Dashboard with a Professor object.
     */
    public Dashboard(Professor professor) {
        this.professor = professor;
        setBackground(Color.WHITE);
        initComponents();
        noteButton = new ModernButton("\u270e", true);
        notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,200, 0));

        initializeUI();

        // Explicitly set the size of the JFrame
        setSize(1200, 1000);
        // Center the window on the screen
        setLocationRelativeTo(null);
    }

    private void openNoteStyleWindow() {
         Note.setProfessor(professor);
         Note.launch();  // Calls static method from other class
    }

    /**
     * This method is used to add custom components to the Dashboard.
     */

    private void initializeUI() {
        // Create the fancy logo panel (adjust the image path as needed)
        FancyLogoPanel fancyLogo = new FancyLogoPanel(
                "https://raw.githubusercontent.com/hioihia123/CheckMates_Application/refs/heads/master/inner-ground2.png"
        );

        // --- Existing header labels ---
        JLabel greetingLabel = new JLabel("Hello, " + professor.getProfessorName() + "!");
        greetingLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        greetingLabel.setForeground(new Color(50, 50, 50));
        greetingLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel professorIDLabel = new JLabel("Professor ID: " + professor.getProfessorID());
        professorIDLabel.setFont(new Font("Roboto", Font.PLAIN, 18));
        professorIDLabel.setForeground(new Color(80, 80, 80));
        professorIDLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create a header panel for the greeting and professor ID, stacked vertically
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        headerPanel.add(greetingLabel);
        headerPanel.add(professorIDLabel);

        // --- Top bar: left side (logo & header) and right side (buttons) ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        // Left side: logo and header panel
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(fancyLogo);
        leftPanel.add(Box.createHorizontalStrut(20)); // spacing
        leftPanel.add(headerPanel);

        // Right side: buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
       
        ModernButton setting = new ModernButton("\u2699", false);
        //setting.setPreferredSize(new Dimension(30,50));
        setting.setFont(new Font("Helvetica Neue", Font.BOLD, 50));
        setting.addActionListener(e -> openSetting());
        rightPanel.add(setting);

        
        topBar.add(leftPanel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);

        // "What's on your mind?" label
        JLabel questionHeader = new JLabel("What's on your mind, " + professor.getProfessorName() +"?");
        questionHeader.setFont(new Font("Helvetica Neue", Font.BOLD, 36));
        questionHeader.setForeground(new Color(50, 50, 50));

        // Wrap questionHeader in a center-aligned panel
        JPanel questionHeaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        questionHeaderPanel.setOpaque(false);
        questionHeaderPanel.add(questionHeader);

        // Create the fancy hover button
        FancyHoverButton fancyButton = new FancyHoverButton("Create Class");
        fancyButton.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        fancyButton.setPreferredSize(new Dimension(180, 50)); // size as needed
        // Add an action listener to open the create class dialog
        fancyButton.addActionListener(e -> openCreateClassDialog());

        // place this button in the same topSection panel, below the question header
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setOpaque(false);

        topSection.add(topBar);

        // Add the questionHeaderPanel (which centers the text)
        topSection.add(questionHeaderPanel);

        // Add some vertical spacing so the button doesn't overlap the label
        topSection.add(javax.swing.Box.createVerticalStrut(60));

        // Put the button in a small panel (FlowLayout) 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0, 0));
        //buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 185, 0, 0));


        buttonPanel.setOpaque(false);
        buttonPanel.add(fancyButton);
        topSection.add(buttonPanel);

        // Create a panel for additional buttons with BoxLayout along the X axis
        JPanel additionalButtonsPanel = new JPanel();
        additionalButtonsPanel.setLayout(new BoxLayout(additionalButtonsPanel, BoxLayout.X_AXIS));
        additionalButtonsPanel.setOpaque(false);

        //create a "View Class" button - explicitly set to modern style (false)
        FancyHoverButton viewClassButton = new FancyHoverButton("Records");
        viewClassButton.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        viewClassButton.setPreferredSize(new Dimension(180, 50));
        viewClassButton.setMaximumSize(new Dimension(180,50));
        viewClassButton.addActionListener(e -> {
            // Create a new ClassDashboard window with modern style (false)
            ClassDashboard classDash = new ClassDashboard(professor, false);
            classDash.setVisible(true);
        });


        //create a "Saki" button for Saki AI Agent
        FancyHoverButton AIbutton = new FancyHoverButton("Saki");
        AIbutton.setFont(new Font ("Helvetica Neueu", Font.BOLD, 24));
        AIbutton.setPreferredSize(new Dimension(180,50));
        AIbutton.setMaximumSize(new Dimension(180,50));
        AIbutton.addActionListener(e -> {
            ChatDialog chatDialog = new ChatDialog(Dashboard.this, professor);
            chatDialog.setVisible(true);
        });
        
        FancyHoverButton emailButton = new FancyHoverButton("Notify");
        emailButton.setFont(new Font ("Helvetica Neueu", Font.BOLD, 24));
        emailButton.setPreferredSize(new Dimension(180,50));
        emailButton.setMaximumSize(new Dimension(180, 50));
        emailButton.addActionListener(e -> sendEmail(professor.getProfessorID()));

        // Add spacing before adding the additional button
        additionalButtonsPanel.add(Box.createHorizontalStrut(20)); // 20-pixel space
        additionalButtonsPanel.add(viewClassButton);
       
        additionalButtonsPanel.add(Box.createHorizontalStrut(20));
        additionalButtonsPanel.add(AIbutton);
        
        additionalButtonsPanel.add(Box.createHorizontalStrut(20));
        additionalButtonsPanel.add(emailButton);

        // Then, add the additionalButtonsPanel to the main button panel that already contains the Create Class button.
        buttonPanel.add(additionalButtonsPanel);
        
         // Add some vertical spacing so the button doesn't overlap the note
        topSection.add(javax.swing.Box.createVerticalStrut(40));
        
        // inside your Dashboard constructor or initComponents()
        ModernSeparator sep = new ModernSeparator(
            true,                       // horizontal
            new Color(180, 180, 180),   // light gray
            new Color(100, 100, 100)    // darker gray
        );
        topSection.add(sep);

        // Add some vertical spacing so the button doesn't overlap the note
        topSection.add(javax.swing.Box.createVerticalStrut(20));
        
        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,200, 0));
        
        notePanel.setOpaque(false);
                
        noteButton.setFont(new Font("Helvetica Neue", Font.BOLD, 25));
        
        noteButton.setPreferredSize(new Dimension(60, 50));
        
        noteButton.addActionListener(e -> openNoteStyleWindow());
        

        notePanel.add(noteButton);
        topSection.add(notePanel);
        
        // --- Main panel setup ---
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Place the top section (top bar + question header) at the top
        panel.add(topSection, BorderLayout.NORTH);

        // --- Bottom: Create the text version label ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        
        ipStatusLabel = new JLabel("IP Restriction: OFF");
        ipStatusLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        ipStatusLabel.setForeground(new Color(50, 50, 50));

        bottomPanel.add(ipStatusLabel, BorderLayout.WEST);
        
        JLabel textLogoLabel = new JLabel("CheckMates Version 1.0");
        textLogoLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        textLogoLabel.setForeground(new Color(50, 50, 50));
      
        bottomPanel.add(textLogoLabel, BorderLayout.EAST);
        
        boolean show = PREFS.getBoolean(KEY_SHOW_TOUR, true);

        // Start the tour after the frame is shown
        if(show){
            SwingUtilities.invokeLater(() -> GuideTour.startTour(Dashboard.this,
                List.of(
                    new GuideStep(questionHeader,    
                   "Hello! \n Welcome to CheckMates. This is a short tour of the program. You can close the tour at anytime."),
                    
                    new GuideStep(setting, "Click here to view setting or log out."),
                    
                    new GuideStep(fancyButton,    
                   "1) Click here to create class to take attendance."),
                        
                    new GuideStep(viewClassButton,    
                   "2) Click here to view created classes and attendance records."),
                    
                    new GuideStep(AIbutton,    
                   "3) Click here to chat with Saki."),
                    
                    new GuideStep(emailButton,    
                   "4) Click here to notify missing students."),
                    
                    new GuideStep(noteButton,    
                   "5) Click here to create notes.")
                    
                    
                )
                   
            ));
        }


        panel.add(bottomPanel, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(1200, 1000));

        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().setBackground(new Color(240, 240, 240));
        validate();
        repaint();
    }

    private void sendEmail(String professorId){
      
    // send HTTP request to  PHP endpoint
    new Thread(() -> {
      try {
        URL url = new URL("https://cm8tes.com/emailAbsentees.php");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        // include professor_id
        String params = "professor_id=" + URLEncoder.encode(professorId,"UTF-8");

        try(OutputStream os = conn.getOutputStream()) {
          os.write(params.getBytes(StandardCharsets.UTF_8));
        }
        int code = conn.getResponseCode();
        
    if (code == 200) {
        SwingUtilities.invokeLater(() -> {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        String html = "<html>Reminder email sent successfully<br/></html>";
        JLabel message = new JLabel(html, SwingConstants.CENTER);
        message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(message, BorderLayout.CENTER);

        FancyHoverButton ok = new FancyHoverButton("OK");
        ok.addActionListener(e ->
            SwingUtilities.getWindowAncestor(panel).dispose()
        );        

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(ok);
        panel.add(btnPanel, BorderLayout.SOUTH);

        JDialog dlg = new JDialog(Dashboard.this, "Success", true);
        dlg.setContentPane(panel);
        dlg.pack();
        dlg.setLocationRelativeTo(Dashboard.this);
        dlg.setVisible(true);
    });
} else {
    SwingUtilities.invokeLater(() ->
        JOptionPane.showMessageDialog(
            Dashboard.this,
            "Error sending emails: HTTP " + code,
            "Error",
            JOptionPane.ERROR_MESSAGE
        )
    );
}

      } catch(Exception ex) {
        SwingUtilities.invokeLater(() ->
          JOptionPane.showMessageDialog(this,
            "Failed: "+ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE)
        );
      }
    }).start();

  }
    private void openSetting(){
        JFrame frame = new JFrame("Setting");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 550);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
               
        frame.getContentPane().setBackground(new Color(255, 255, 255));  
        frame.setVisible(true);
        
        // 1) Top toolbar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topPanel.setOpaque(false);

         ModernButton logOffButton = new ModernButton("Log Off", false);
        logOffButton.addActionListener(e -> {
            Dashboard.this.dispose();
            new Login().setVisible(true);
        });
        
        FancyHoverButton2 oldTimeyButton = new FancyHoverButton2("Old-Timey Style");
        oldTimeyButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        oldTimeyButton.addActionListener(e -> {
            this.dispose();
            new oldDashboard(professor).setVisible(true);
        });

        topPanel.add(logOffButton);
        topPanel.add(oldTimeyButton);

        frame.add(topPanel, BorderLayout.NORTH);
    }
    private void openCreateClassDialog() {
        JDialog dialog = new JDialog(this, "Create New Class", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        // Use GridBagLayout for more control
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);  // spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Class Name
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // label takes minimal width
        contentPanel.add(nameLabel, gbc);

        JTextField classNameField = new JTextField();
        classNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classNameField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;  // text field expands
        contentPanel.add(classNameField, gbc);

        // Row 1: Section
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        contentPanel.add(sectionLabel, gbc);

        JTextField sectionField = new JTextField();
        sectionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sectionField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        contentPanel.add(sectionField, gbc);

        // Row 2: Expiration (minutes)
        JLabel expirationLabel = new JLabel("Expiration (minutes):");
        expirationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        contentPanel.add(expirationLabel, gbc);

        JTextField expirationField = new JTextField();
        expirationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        expirationField.setEditable(true);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        contentPanel.add(expirationField, gbc);

        // Add ActionListeners for Enter key navigation
        classNameField.addActionListener(e -> sectionField.requestFocusInWindow());
        sectionField.addActionListener(e -> expirationField.requestFocusInWindow());
        expirationField.addActionListener(e -> chkIP.requestFocusInWindow());

        // IP Restriction
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.WHITE);
        chkIP = new JCheckBox("IP Restriction");
        chkIP.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkIP.setBackground(Color.WHITE);

        chkIP.addActionListener(e -> {
  if (chkIP.isSelected()) {
      isIPRTurnOn = true;
    new Thread(() -> {
      try {
        String[] ips = fetchBothPublicIPs();
        String ipv4 = ips[0], ipv6 = ips[1];
        // after fetchBothPublicIPs():
        String rawV6 = ips[1];              // full IPv6
        String prefix = prefix64(rawV6);    // "0000:0000:00f0:0000"
        collectedIPv6 = prefix;
        SwingUtilities.invokeLater(() ->
          ipStatusLabel.setText(String.format(
            "IP Restriction: ON  v4=%s  v6=%s",
            ipv4.isEmpty() ? "n/a" : ipv4,
            ipv6.isEmpty() ? "n/a" : ipv6
          ))
        );
      } catch(Exception ex) {
        SwingUtilities.invokeLater(() -> {
          chkIP.setSelected(false);
          JOptionPane.showMessageDialog(this,
            "Failed to fetch IPs: "+ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        });
      }
    }).start();
  } else {
    isIPRTurnOn = false;
    String collectedIPv4 = "";
    String collectedIPv6 = "";
    ipStatusLabel.setText("IP Restriction: OFF");
  }
});

        centerPanel.add(chkIP);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 0, 0);
        contentPanel.add(centerPanel, gbc);

        // Row 3: Create button (spanning two columns)
        FancyHoverButton createButton = new FancyHoverButton("Create");
        createButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        createButton.setBackground(new Color(0, 100, 0));  // modern blue
        createButton.setFocusPainted(false);
        createButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Make the create button the default button for the dialog
        dialog.getRootPane().setDefaultButton(createButton);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = classNameField.getText().trim();
                String section = sectionField.getText().trim();
                String expirationText = expirationField.getText().trim();

                if (className.isEmpty() || section.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in both Class Name and Section.", "Incomplete Form", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Default expiration minutes
                int expirationMinutes = 60;
                try {
                    if (!expirationText.isEmpty()) {
                        expirationMinutes = Integer.parseInt(expirationText);
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(dialog, "Expiration must be a number (minutes). Using default of 60 minutes.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }

                // Generate a random 4-digit passcode
                int passcode = (int) (Math.random() * 9000) + 1000;

                // Build the check-in URL
                String checkInUrl = "tinyurl.com/02221732";

                // Save the class information and generate QR code
                saveClassToDatabase(professor.getProfessorID(), className, section, passcode, expirationMinutes);
                Image qrImage = generateQRCodeImage(checkInUrl, 200, 200);
                showQRCodeDialog(qrImage, checkInUrl, passcode, expirationMinutes);

                dialog.dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(createButton, gbc);

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
    
    private String fetchURL(String urlStr) throws IOException {
    URL url = new URL(urlStr);
    HttpURLConnection c = (HttpURLConnection) url.openConnection();
    c.setConnectTimeout(3_000);
    c.setReadTimeout(3_000);
    try (BufferedReader in = new BufferedReader(
             new InputStreamReader(c.getInputStream()))) {
        return in.readLine().trim();
    }
}

/** Returns a 2‑element String[]: [0]=IPv4, [1]=IPv6 (or empty if none) */
private String[] fetchBothPublicIPs() {
    String v4 = "", v6 = "";
    try { v4 = fetchURL("https://api.ipify.org?format=text"); } catch(Exception e){ }
    try { v6 = fetchURL("https://api6.ipify.org?format=text"); } catch(Exception e){ }
    return new String[]{ v4, v6 };
}

private String prefix64(String rawIp) {
    String noZone = rawIp.split("%")[0];
    String[] parts = noZone.split(":");
    // pad to 8 parts
    List<String> p = new ArrayList<>(Arrays.asList(parts));
    while (p.size() < 8) p.add("0");
    // return first 4 hextets
    return String.join(":", p.subList(0, 4));
}



    // Method to generate a QR code image using ZXing
    private Image generateQRCodeImage(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
            java.awt.image.BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            return qrImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to show a dialog with the generated QR code
    private void showQRCodeDialog(Image qrImage, String url, int passcode, int expirationMinutes) {
        JDialog qrDialog = new JDialog(this, "Class Check-In QR Code", true);
        qrDialog.setSize(320, 480);
        qrDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Include passcode, URL, and expiration information in the info label
        JLabel infoLabel = new JLabel("<html>Passcode: " + passcode + "<br>" +
                "URL: " + url + "<br>" +
                "Expires in: " + expirationMinutes + " minute(s)</html>");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(50, 50, 50));

        contentPanel.add(qrLabel, BorderLayout.CENTER);
        contentPanel.add(infoLabel, BorderLayout.SOUTH);

        qrDialog.add(contentPanel);
        qrDialog.setVisible(true);
    }
    //FIX ME: Get professsor's ip address upon they check the box
    //Then store in the database
    //Write another php to do that
    


    private void saveClassToDatabase(String professorId,
                                 String className,
                                 String section,
                                 int    passcode,
                                 int    expirationMinutes) {
    new Thread(() -> {
        try {
            // 1) build & POST the data from the database
            String urlString = "http://cm8tes.com/createClass.php";
            String params = "professor_id=" + URLEncoder.encode(professorId, StandardCharsets.UTF_8.name())
                          + "&class="        + URLEncoder.encode(className, StandardCharsets.UTF_8.name())
                          + "&section="      + URLEncoder.encode(section, StandardCharsets.UTF_8.name())
                          + "&expiration="   + expirationMinutes
                          + "&passcode="     + passcode
                          + "&ip_restriction=" + (isIPRTurnOn ? "1" : "0");
                          
            if(isIPRTurnOn == true){
                params += "&ip_address_v6=" + URLEncoder.encode(collectedIPv6, StandardCharsets.UTF_8.name());                
            }
            
            byte[] postData = params.getBytes(StandardCharsets.UTF_8);

            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(postData);
            }

            // 2) read response
            InputStream  is       = conn.getInputStream();
            BufferedReader in      = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder  sb      = new StringBuilder();
            String         line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            conn.disconnect();

            JSONObject json = new JSONObject(sb.toString());

            // 3)UI
            SwingUtilities.invokeLater(() -> {
                if ("success".equalsIgnoreCase(json.optString("status"))) {
                    JPanel panel = new JPanel(new BorderLayout());
                    panel.setBackground(Color.WHITE);
                    panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

                    String html = "<html>"
                        + "Class created successfully!<br/>"
                        + "Passcode: " + json.optInt("passcode") + "<br/>"
                        + "Expires: "  + json.optString("passcode_expires")
                        + "</html>";
                    JLabel message = new JLabel(html, SwingConstants.CENTER);
                    message.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    panel.add(message, BorderLayout.CENTER);

                    FancyHoverButton ok = new FancyHoverButton("OK");
                    ok.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.add(ok);
                    panel.add(btnPanel, BorderLayout.SOUTH);

                    JDialog dlg = new JDialog(Dashboard.this, "Success", true);
                    dlg.setContentPane(panel);
                    dlg.pack();
                    dlg.setLocationRelativeTo(Dashboard.this);
                    dlg.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(Dashboard.this,
                        "Error: " + json.optString("message"),
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(Dashboard.this,
                    "Error creating class: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }).start();
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        greetingLabel = new javax.swing.JLabel();
        questionHeader = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        greetingLabel.setFont(new java.awt.Font("Hiragino Maru Gothic ProN", 3, 36)); // NOI18N
        greetingLabel.setText("jLabel1");

        questionHeader.setFont(new java.awt.Font("Helvetica Neue", 3, 36)); // NOI18N
        questionHeader.setText("What's on your mind?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(365, 365, 365)
                                                .addComponent(greetingLabel))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(questionHeader)))
                                .addContainerGap(386, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addComponent(questionHeader)
                                .addGap(134, 134, 134)
                                .addComponent(greetingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(286, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>//comment

        // Create a Professors object
        Professor prof = new Professor("Professor Name", "test@gmail.com", "it's not a game. I am not a robot AI challenging you");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard(prof).setVisible(true);
            }
        });
    }
 
    /**
 * Represents a single step in the coach-mark tour.
 */
static class GuideStep {
    final JComponent target;
    final String message;
    GuideStep(JComponent target, String message) {
        this.target = target;
        this.message = message;
    }
}

/**
 * Manages the sequence and display of guide steps.
 */
static class GuideTour {
    /**
     * Kick off the tour on the given frame with a list of steps.
     */
    public static void startTour(JFrame frame, List<GuideStep> steps) {
        Iterator<GuideStep> it = steps.iterator();
        showNext(frame, it);
    }

    private static void showNext(JFrame frame, Iterator<GuideStep> it) {
        if (!it.hasNext()) {
            // Tour finished; clear any glass pane
            frame.getGlassPane().setVisible(false);
            return;
        }
        GuideStep step = it.next();

        // 1) Set up overlay (glass pane)
        GuideOverlay overlay = new GuideOverlay(step);
        frame.setGlassPane(overlay);
        overlay.setVisible(true);

        // 2) Create a small tip dialog near the target
        Point targetOnScreen = step.target.getLocationOnScreen();
        JDialog tip = new JDialog(frame, false);
        tip.setUndecorated(true);
        tip.getContentPane().setLayout(new BorderLayout(5,5));        
        
        JLabel msg = new JLabel("<html><b>" + step.message + "</b><br/><i>Click \u2192 to continue</i></html>");
        msg.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tip.getContentPane().add(msg, BorderLayout.CENTER);
       
        FancyHoverButton2 next = new FancyHoverButton2("\u2192");
        ModernButton xButton = new ModernButton("X", false);
        ModernButton doNotShow = new ModernButton("Do Not Show This Again", false);


        next.addActionListener((ActionEvent e) -> {
            tip.dispose();
            overlay.setVisible(false);
            showNext(frame, it);
        });
        xButton.addActionListener((ActionEvent e) -> {
            overlay.setVisible(false);
            tip.dispose();
        
        });
        doNotShow.addActionListener((ActionEvent e) -> {
            PREFS.putBoolean(KEY_SHOW_TOUR, false);
            overlay.setVisible(false); 
            tip.dispose();
        });
        // top panel just for the X, right‐aligned
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topPanel.setOpaque(false);
        topPanel.add(xButton);
        tip.getContentPane().add(topPanel, BorderLayout.NORTH);
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(next);
        btnPanel.add(doNotShow);
        
        tip.getContentPane().add(btnPanel, BorderLayout.SOUTH);
        tip.pack();

        // Position the tip just below the target component
        tip.setLocation(targetOnScreen.x, targetOnScreen.y + step.target.getHeight() + 8);
        tip.setVisible(true);
    }
}

/**
 * A glass-pane overlay that dims the entire frame and "spotlights" the target component.
 */
static class GuideOverlay extends JComponent {
    private final GuideStep step;

    GuideOverlay(GuideStep step) {
        this.step = step;
        setOpaque(false);
        // Capture mouse events so underlying components are not clickable
        addMouseListener(new java.awt.event.MouseAdapter() {});
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth(), h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();

        // 1) Dim the entire area
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, w, h);

        

        g2.dispose();
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel greetingLabel;
    private javax.swing.JLabel questionHeader;
    // End of variables declaration//GEN-END:variables
}