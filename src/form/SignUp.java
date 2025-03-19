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
import java.awt.Font;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;


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
    btnSubmit = new JButton("Sign Up");
    btnSubmit.setFont(new Font("Arial", Font.BOLD, 18));
    // Adjust y-position (e.g., y=230)
    btnSubmit.setBounds(140, 230, 120, 30);
    btnSubmit.setBackground(Color.WHITE);
    btnSubmit.setForeground(Color.BLACK);
    btnSubmit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
    btnSubmit.setEnabled(false); // Initially disabled until checkbox is checked
    add(btnSubmit);

    // Enable the button only if the Terms & Conditions checkbox is selected
    chkTerms.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            btnSubmit.setEnabled(chkTerms.isSelected());
        }
    });

    setVisible(true);
}


    // For testing, you can run this class directly
    public static void main(String[] args) {
        new SignUp().setVisible(true);
    }
}

