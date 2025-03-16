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

    // Declare your UI components
    private JLabel lblTitle;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSubmit;
    private JCheckBox chkTerms;

    public SignUp() {
        // Set basic JFrame properties
        setTitle("Sign Up");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window

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
        // Use a null layout for simple absolute positioning (or use a layout manager)
        setLayout(null);

        // Title Label
        lblTitle = new JLabel("Create New Account");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBounds(100, 20, 250, 30);
        add(lblTitle);

        // Username field
        HintTextField txtUsername = new HintTextField("Enter email");
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsername.setBounds(100, 70, 200, 30);
        txtUsername.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        add(txtUsername);
        
        

        // Password field
        HintPasswordField txtPassword = new HintPasswordField("Enter password");
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBounds(100, 110, 200, 30);
        txtPassword.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        add(txtPassword);
        
         /* Terms and Conditions Checkbox
        chkTerms = new JCheckBox("I agree to the");
        chkTerms.setBounds(100, 150, 150, 30);
        add(chkTerms);
       
        
         // Terms and Conditions Hyperlink
        JLabel lblTerms = new JLabel("<html><a href='#'>Terms and Conditions</a></html>");
        lblTerms.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblTerms.setForeground(Color.BLUE);
        lblTerms.setBounds(255, 150, 150, 30);
        add(lblTerms);
        
        
        // Add click event to open Terms and Conditions link
        lblTerms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Terms and Conditions clicked");
                openWebPage("https://www.google.com"); // Change URL to actual Terms and Conditions
                
            }
        });
        */
        JPanel termsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        termsPanel.setBounds(100, 150, 300, 30);
        termsPanel.setOpaque(false); // make the panel transparent

        JCheckBox chkTerms = new JCheckBox("I agree to the ");
        termsPanel.add(chkTerms);

        JLabel lblTerms = new JLabel("<html><a href='https://www.google.com'>Terms and Conditions</a></html>");
        lblTerms.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblTerms.setForeground(Color.BLUE);
        termsPanel.add(lblTerms);

        add(termsPanel);
        
        // Add click event to open Terms and Conditions link
        lblTerms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Terms and Conditions clicked");
                openWebPage("https://terms.cm8tes.com"); // Change URL to actual Terms and Conditions
                
            }
        });

        // Submit Button
        btnSubmit = new JButton("Sign Up");
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 18));
        btnSubmit.setBounds(140, 190, 120, 30);
        btnSubmit.setOpaque(true);
        btnSubmit.setContentAreaFilled(true);
        btnSubmit.setBackground(Color.WHITE);
        btnSubmit.setForeground(Color.BLACK);
        btnSubmit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        add(btnSubmit);
        
         // Add action listener to enable button when checkbox is checked
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

