package form;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatProcess {
    
    // Store class_id
    private static int class_id; 

    /**
     * Processes the user message. If the message is an attendance query,
     * it retrieves data from the database (via a PHP endpoint) and appends
     * a summary to the prompt.
     */
    public static String processUserMessage(String message, int class_id) {
        String lowerMsg = message.toLowerCase();
        // Relaxed condition: if user mentions "attendance" or mentions both "checked" and "student"
        if (lowerMsg.contains("attendance") || lowerMsg.contains("name") || lowerMsg.contains("date")
                || lowerMsg.contains("student name") 
                || lowerMsg.contains("checked in date")
                || lowerMsg.contains("checked in date")
                || lowerMsg.contains("total checked in for this class") 
                || lowerMsg.contains("total students with date and time") 
                || lowerMsg.contains("checked in students with name, date, student id, time")
                || lowerMsg.contains("Did this student attended this class or not?")
                || lowerMsg.contains("List all the checked in students in this class at this time")
                ||
           (lowerMsg.contains("checked") && lowerMsg.contains("student")
                &&lowerMsg.contains("time") && lowerMsg.contains("student ID"))) {
            
            // Automatically retrieve the attendance summary from getAttendanceSummary.php endpoint
            String attendanceData = getAttendanceSummary(class_id);
            
            // Construct the prompt by injecting the retrieved data
            String prompt = "For the selected class, here is the attendance data:\n" 
                            + attendanceData +
                            "\nNow answer the following question: " + message;
            return getChatGPTResponse(prompt);
        } else {
            return getChatGPTResponse(message);
        }
    }

    /**
     * Retrieves a summary of attendance for a given class by calling a PHP endpoint.
     * This version parses student IDs, names, and check-in times.
     */
    private static String getAttendanceSummary(int classId) {
        try {
            String urlString = "http://cm8tes.com/getAttendanceSummary.php?class_id=" +
                           URLEncoder.encode(String.valueOf(classId), StandardCharsets.UTF_8.toString());
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
            );
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            if ("success".equalsIgnoreCase(json.optString("status"))) {
                int total = json.optInt("total", 0);
                JSONArray records = json.optJSONArray("records");
                StringBuilder recordsBuilder = new StringBuilder();
                if (records != null) {
                    for (int i = 0; i < records.length(); i++) {
                        JSONObject record = records.getJSONObject(i);
                        String studentId = record.optString("studentId");
                        String name = record.optString("name");
                        String time = record.optString("time");
                        String date = record.optString("date");
                        recordsBuilder.append("- Student ").append(studentId)
                                      .append(" (").append(name)
                                      .append(") checked in at ").append(time)
                                      .append("on ").append(date)
                                      .append("\n");
                    }
                }
                return "Total: " + total + "\nRecords:\n" + recordsBuilder.toString();
            } else {
                return "Attendance data unavailable.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Attendance data unavailable.";
        }
    }

    /**
     * Calls the ChatGPT API using the given prompt.
     * Replace the authorization token with your own.
     */
    private static String getChatGPTResponse(String prompt) {
    try {
        HttpClient client = HttpClient.newHttpClient();
        
        // Build the JSON payload using org.json
        JSONObject payload = new JSONObject();
        payload.put("model", "gpt-3.5-turbo");
        
        JSONArray messages = new JSONArray();
        
        // System message
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Your name is Saki. You are an AI assistant that answers questions about class attendance for the specific class stored in the database.");
        messages.put(systemMessage);
        
        // User message
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.put(userMessage);
        
        payload.put("messages", messages);
        payload.put("temperature", 0.7);
        
        String jsonPayload = payload.toString();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer API")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        JSONObject jsonResponse = new JSONObject(response.body());

        if (jsonResponse.has("error")) {
            JSONObject errorObj = jsonResponse.getJSONObject("error");
            return "API Error: " + errorObj.getString("message");
        }

        if (!jsonResponse.has("choices")) {
            return "Error: API response did not contain choices.";
        }

        String reply = jsonResponse.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");
        return reply;
    } catch (Exception e) {
        e.printStackTrace();
        return "Error retrieving response.";
    }
}


    // For testing purposes
    public static void main(String[] args) {
        class_id = 12;
        String userQuery = "What is the attendance for this class? How many students have checked in?";
        String response = processUserMessage(userQuery, class_id);
        System.out.println("ChatGPT Response: " + response);
    }
}
