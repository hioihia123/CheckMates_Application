/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package checkmates;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.json.JSONObject;

/**
 *
 * @author nguyenp
 */
public class CheckMates {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    /*
    private static void deleteProfessorFromDatabase(String owner_id, String contact_id){
     System.out.println("DELETING: owner_id=" + owner_id + ", contact_id=" + contact_id);

    new Thread(() -> {
        HttpURLConnection conn = null;
        try {
            // Open connection to  delete endpoint
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
 */
}
