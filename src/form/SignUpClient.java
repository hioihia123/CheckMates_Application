/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package form;

/**
 *
 * @author nguyenp
 */
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class SignUpClient {
    public static void main(String[] args) {
        String urlString = "http://cm8tes.com/signup.php";
        String professorName = "Alice";
        String email = "alice@example.com";
        String passWord = "secret123";

        try {
            // Prepare the POST data
            String urlParameters = "professorName=" + URLEncoder.encode(professorName, "UTF-8") +
                                   "&email=" + URLEncoder.encode(email, "UTF-8") +
                                   "&passWord=" + URLEncoder.encode(passWord, "UTF-8");

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

            // Create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            // Send POST data
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            // Read response
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Print or parse the JSON response
                System.out.println("Response: " + response.toString());
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

