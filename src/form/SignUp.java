/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class SignUp extends JFrame {
//hi
    // Declare your UI components
    private JLabel lblTitle;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField professorName;
    private JButton btnSubmit;
    private JCheckBox chkTerms;

    public SignUp() {
        // Set basic JFrame properties
        setTitle("Sign Up");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        //Set only the uppertop panel to white
        setBackground(Color.WHITE);
        //Set the whole background is white
        getContentPane().setBackground(Color.WHITE);
        // Initialize components
        initComponents();
    }
    
     // Method to open a web page in the default browser
    private void openWebPage(String url) {
    try {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URI(url));
                return;
            }
        }
        // Show error if browsing isn't supported
        JOptionPane.showMessageDialog(this, 
            "Unable to open browser. Please visit: " + url, 
            "Browser Error", 
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        // Show detailed error message
        JOptionPane.showMessageDialog(this, 
            "Error: " + e.getMessage(), 
            "Cannot Open Link", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private void initComponents() {
    // Using a null layout for absolute positioning
    setLayout(null);

    // Title Label
    lblTitle = new JLabel("Create New Account");
    lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
    lblTitle.setBounds(100, 20, 250, 30);
    add(lblTitle);

    // 1) Professor Name field
    professorName = new HintTextField("Enter Name");
    professorName.setFont(new Font("Arial", Font.PLAIN, 16));
    // Change the Y-position so it doesn't overlap with username
    professorName.setBounds(100, 70, 200, 30);
    professorName.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
    add(professorName);

    // 2) Username field
    txtUsername = new HintTextField("Enter email");
    txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
    // Move this down to avoid overlap (e.g., y=110)
    txtUsername.setBounds(100, 110, 200, 30);
    txtUsername.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
    add(txtUsername);

    // 3) Password field
    txtPassword = new HintPasswordField("Enter password");
    txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
    // Move this further down (e.g., y=150)
    txtPassword.setBounds(100, 150, 200, 30);
    txtPassword.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
    add(txtPassword);

    // 4) Terms & Conditions panel
    JPanel termsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    termsPanel.setBounds(100, 190, 300, 30);
    termsPanel.setOpaque(false);

    chkTerms = new JCheckBox("I agree to the ");
    termsPanel.add(chkTerms);

    JLabel lblTerms = new JLabel("<html><a href='https://www.google.com'>Terms and Conditions</a></html>");
    lblTerms.setCursor(new Cursor(Cursor.HAND_CURSOR));
    lblTerms.setForeground(Color.BLUE);
    termsPanel.add(lblTerms);

    add(termsPanel);

    // Link click action
    lblTerms.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            openWebPage("https://terms.cm8tes.com");
        }
    });

    // 5) Submit Button
    // Create and configure the submit button first
  btnSubmit = new JButton("Sign Up");
  btnSubmit.setFont(new Font("Arial", Font.BOLD, 18));
  btnSubmit.setBounds(140, 230, 120, 30);
  btnSubmit.setBackground(Color.WHITE);
  btnSubmit.setForeground(Color.BLACK);
  btnSubmit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
  btnSubmit.setEnabled(false); // Disabled until checkbox is checked
add(btnSubmit);

// Then add the listener for the checkbox that enables/disables the button
chkTerms.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        btnSubmit.setEnabled(chkTerms.isSelected());
    }
});

     // Submit button action to send HTTP POST to PHP
    btnSubmit.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        final String name = professorName.getText();
        final String email = txtUsername.getText();
        final String password = new String(txtPassword.getPassword());

        // Run HTTP request on a separate thread to avoid UI blocking
        new Thread(() -> {
            try {
                String response = sendSignUpRequest(name, email, password);
                // Update UI on the Event Dispatch Thread
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(SignUp.this, "Response from server: " + response);
                    // Check if the response indicates success before redirecting
                    if (response.toLowerCase().contains("success")) {
                        // Open the Login form and close this one
                        new Login().setVisible(true);
                        SignUp.this.dispose();
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(SignUp.this, "Error: " + ex.getMessage());
                });
            }
        }).start();
    }
});


        setVisible(true);
    }
    
   // Helper method to send sign-up data to the PHP bridge
    private String sendSignUpRequest(String name, String email, String password) throws IOException {
        // URL to your PHP endpoint
        String urlString = "http://cm8tes.com/signup.php";
        // Build the POST parameters
        String urlParameters = "professorName=" + URLEncoder.encode(name, "UTF-8") +
                "&email=" + URLEncoder.encode(email, "UTF-8") +
                "&passWord=" + URLEncoder.encode(password, "UTF-8");

        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        // Write POST data
        try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
            out.write(postData);
        }

        // Read the response
        int responseCode = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Optionally, you can parse the JSON response here using a JSON library
        return response.toString();
    }

    // For testing, you can run this class directly
    public static void main(String[] args) {
        new SignUp().setVisible(true);
    }
}
