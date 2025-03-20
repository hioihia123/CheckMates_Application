/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JButton;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject; // Make sure to include a JSON library


/**
 *
 * @author nguyenp
 */

public class Login extends javax.swing.JFrame {
    
    /**
     * Creates new form Login
     */
    private Image backgroundImage;
    private TextField awtPasswordField;
    public Login() {
    // Removes window decorations (title bar, borders)
    setUndecorated(true);

    // 1) Load the background image from GitHub
    try {
        URL backgroundUrl = new URL("https://raw.githubusercontent.com/hioihia123/CheckMates_Application/refs/heads/master/background5.png");
        backgroundImage = ImageIO.read(backgroundUrl);
    } catch (IOException e) {
        e.printStackTrace();
    }

    // 2) Create a custom panel that draws the background image
    JPanel backgroundPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Draw the image to fill the entire panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    };

    // Set a layout for your custom panel
    backgroundPanel.setLayout(null); // or any other layout

    // 3) Make this custom panel the frame's content pane
    setContentPane(backgroundPanel);

    // 4) Now call the auto-generated initComponents() to add your UI elements
    initComponents();

    // 5) Then call custom styling
    customizeComponents();

    // Create an instance of custom canvas
    OutlinedTextCanvas outlinedText = new OutlinedTextCanvas();

    // Set its size and location (adjust as needed)
    outlinedText.setSize(300, 100);
    outlinedText.setLocation(190, 68);  // Coordinates

    // Add it to the content pane.
    getContentPane().add(outlinedText);
}

  
    
     /*@Override
public void paint(Graphics g) {
    super.paint(g);  // Let Swing do its usual background painting first
    
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    // 1) Fill the entire background with white
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, getWidth(), getHeight());
    
    // 2) Then draw your round rectangle (if you want)
    g2.setColor(Color.WHITE); // or any other color
    g2.fillRoundRect(20, 20, getWidth() - 40, getHeight() - 40, 50, 50);
}
    */


    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        textField1 = new java.awt.TextField();
        app_exit = new javax.swing.JButton();
        label1 = new java.awt.Label();
        label3 = new java.awt.Label();
        label4 = new java.awt.Label();
        label5 = new java.awt.Label();
        userName = new java.awt.TextField();
        btnLogin = new javax.swing.JButton();

        jButton1.setText("jButton1");

        jLabel1.setText("jLabel1");

        textField1.setText("textField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 255, 255));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setUndecorated(true);

        app_exit.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        app_exit.setText("X");
        app_exit.setBorderPainted(false);
        app_exit.setContentAreaFilled(false);
        app_exit.setFocusPainted(false);
        app_exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                app_exitMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                app_exitMouseExited(evt);
            }
        });
        app_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                app_exitActionPerformed(evt);
            }
        });

        label1.setBackground(new java.awt.Color(255, 255, 255));
        label1.setFont(new java.awt.Font("Maku", 3, 18)); // NOI18N
        label1.setForeground(new java.awt.Color(0, 0, 0));
        label1.setText("Let's CheckMates");

        label3.setBackground(new java.awt.Color(255, 255, 255));
        label3.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 24)); // NOI18N
        label3.setForeground(new java.awt.Color(0, 0, 0));
        label3.setText("Email:");

        label4.setBackground(new java.awt.Color(255, 255, 255));
        label4.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 18)); // NOI18N
        label4.setForeground(new java.awt.Color(0, 0, 0));
        label4.setText("Sign up here!");

        label5.setBackground(new java.awt.Color(255, 255, 255));
        label5.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 24)); // NOI18N
        label5.setForeground(new java.awt.Color(0, 0, 0));
        label5.setText("Password:");

        userName.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        userName.setMinimumSize(new java.awt.Dimension(8, 24));
        userName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userNameActionPerformed(evt);
            }
        });

        btnLogin.setText("LOGIN    xxx");
        btnLogin.setAutoscrolls(true);
        btnLogin.setBorderPainted(false);
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(196, 196, 196)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnLogin)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(244, 244, 244)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(100, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(app_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(181, 181, 181)
                        .addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(app_exit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void app_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_app_exitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_app_exitActionPerformed

    private void app_exitMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_app_exitMouseEntered
        app_exit.setForeground(java.awt.Color.RED);
    }//GEN-LAST:event_app_exitMouseEntered

    private void app_exitMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_app_exitMouseExited
        app_exit.setForeground(java.awt.Color.BLACK);
    }//GEN-LAST:event_app_exitMouseExited

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
    // Retrieve email and password from  login form components.
    String email = userName.getText().trim();
    
    String password = awtPasswordField.getText().trim();

    if(email.isEmpty() || password.isEmpty()){
        JOptionPane.showMessageDialog(this, "Please enter both email and password.");
        return;
    }

    // Run the login HTTP request on a separate thread
    new Thread(() -> {
        try {
            String jsonResponse = sendLoginRequest(email, password);
            // Parse the JSON response (using org.json library)
            JSONObject obj = new JSONObject(jsonResponse);
            if(obj.getString("status").equalsIgnoreCase("success")){
                // Retrieve the professor's name
                String professorName = obj.getString("professorName");
                // Open the Dashboard window and pass the professor's name
                SwingUtilities.invokeLater(() -> {
                    new Dashboard(professorName).setVisible(true);
                    Login.this.dispose();
                });
            } else {
                // Login failed: display the error message from the response
                String errorMsg = obj.optString("message", "Invalid email or password.");
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Login.this, errorMsg, "Login Failed", JOptionPane.ERROR_MESSAGE);
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(Login.this, "Error: " + ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }).start();

    }//GEN-LAST:event_btnLoginActionPerformed

    private void userNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userNameActionPerformed
 
    private void customizeComponents() {
    // 1) Email field (AWT TextField) styling remains unchanged
    userName.setBackground(Color.WHITE);
    userName.setForeground(Color.BLACK);
    userName.setFont(new Font("Arial", Font.PLAIN, 16));
    // Note: AWT TextField cannot have borders like Swing components.
    
    /* 2) Password field (JPasswordField) styling remains unchanged
    pWord.setBackground(Color.WHITE);
    pWord.setForeground(Color.BLACK);
    pWord.setCaretColor(Color.BLACK);
    pWord.setFont(new Font("Arial", Font.PLAIN, 16));
    pWord.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
    */
 

    // 3) Style the Login button as text-only with no box or border.
    btnLogin.setContentAreaFilled(false); // No background fill
    btnLogin.setOpaque(true);            // Transparent
    btnLogin.setBorderPainted(false);     // Remove any border
    btnLogin.setForeground(Color.BLACK);  // Set text color to black
    btnLogin.setFocusPainted(false);
    btnLogin.setBackground(Color.WHITE); // fully transparent
    btnLogin.setFont(new Font("Arial", Font.BOLD, 18));
    
    btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    // 4) Add a hover effect: text turns red when the mouse hovers over the button.
    btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            btnLogin.setForeground(Color.RED);
        }
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            btnLogin.setForeground(Color.BLACK);
        }
    });
    
    label4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Change cursor on hover

    label4.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        System.out.println("Sign Up Button Clicked");
        // Create and show the SignUp form
        new SignUp().setVisible(true);

        //dispose the login form:
        Login.this.dispose();
    }
});
    
    // Hide the original auto‑generated password field (if not needed)
    //pWord.setVisible(false);
    
    Image closedEye = Toolkit.getDefaultToolkit().getImage("eye_closed.png");
    Image openEye   = Toolkit.getDefaultToolkit().getImage("open_eye.png");


    // Create an AWT TextField for the password
    awtPasswordField = new TextField(34);
    awtPasswordField.setEchoChar('*');
    awtPasswordField.setBackground(java.awt.Color.WHITE);
    awtPasswordField.setForeground(java.awt.Color.BLACK);
    awtPasswordField.setMinimumSize(new Dimension(8, 26));



    // Create an AWT Button for toggling the password visibility
    Button btnTogglePassword = new Button("Show");
    final char defaultEchoChar = '*';
    btnTogglePassword.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // If currently hiding the text, reveal it.
            if (awtPasswordField.getEchoChar() != (char) 0) {
                awtPasswordField.setEchoChar((char) 0);
                btnTogglePassword.setLabel("Hide");
            } else {
                // Otherwise, reset to the default echo character.
                awtPasswordField.setEchoChar(defaultEchoChar);
                btnTogglePassword.setLabel("Show"); 
            }
        }
    });

    // 1) Create two row panels.
     Panel row1 = new Panel(new FlowLayout(FlowLayout.LEFT, 0, 0));
     Panel row2 = new Panel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

     // 2) Add the password field to the top row (left-aligned).
     row1.add(awtPasswordField);

     // 3) Add the toggle button to the bottom row (right-aligned).
     row2.add(btnTogglePassword);

     // 4) Create a parent panel with 2 rows (GridLayout(2,1)).
     Panel awtPasswordPanel = new Panel(new GridLayout(2, 1, 0, 5));
     awtPasswordPanel.setBackground(Color.WHITE);

     // 5) Add row1 and row2 to the parent panel.
     awtPasswordPanel.add(row1);
     awtPasswordPanel.add(row2);

     // 6) Position the parent panel in  frame (null layout).
     getContentPane().setLayout(null);
     awtPasswordPanel.setBounds(195, 250, 300,70);  // Adjust as needed
     getContentPane().add(awtPasswordPanel);
    
}
    private String sendLoginRequest(String email, String password) throws IOException {
    // Update  PHP endpoint URL
    String urlString = "http://cm8tes.com/login.php";
    String urlParameters = "email=" + URLEncoder.encode(email, "UTF-8") +
                           "&passWord=" + URLEncoder.encode(password, "UTF-8");
    
    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
    
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    conn.setDoOutput(true);
    
    // Send POST data
    try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
        out.write(postData);
    }
    
    // Read the response
    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();
    return response.toString();
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    //kjkjlk

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton app_exit;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private java.awt.Label label1;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.TextField textField1;
    private java.awt.TextField userName;
    // End of variables declaration//GEN-END:variables
}
