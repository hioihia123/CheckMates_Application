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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.json.JSONArray;
import org.json.JSONObject;

public class ChatProcess {
    
    // Store conversation history for contextual memory
    private static List<String> conversationHistory = new ArrayList<>();

    /**
     * Processes the user message.  
     * If the message is detected as an attendance query, it retrieves data from the database (via a PHP endpoint) 
     * and appends a summary to the prompt.
     * 
     * @param message The user message.
     * @param class_id The selected class ID.
     * @param classContext The string representation (name, section, etc.) of the class.
     * @return The response from ChatGPT.
     */
    public static String processUserMessage(String message, int class_id, String classContext) {
        // Save conversation history (for adaptive learning)
        conversationHistory.add("User: " + message);

        if (isAttendanceQuery(message)) {
            String attendanceData = getAttendanceSummary(class_id);
            String prompt = "For class " + classContext + ", here is the attendance data:\n" 
                            + attendanceData +
                            "\nNow answer the following question: " + message;
            String response = getChatGPTResponse(prompt);
            conversationHistory.add("Saki: " + response);
            return response;
        } else {
            String response = getChatGPTResponse(message);
            conversationHistory.add("Saki: " + response);
            return response;
        }
    }

    /**
     * Uses regex to determine if the user's message is related to attendance.
     */
    private static boolean isAttendanceQuery(String message) {
        // Define a few key patterns related to attendance queries
        String[] patterns = {
            "(?i)attendance",                   // case-insensitive 'attendance'
            "(?i)checked\\s*in",                // 'checked in' with optional spaces
            "(?i)student.*(name|id)",            // mentions student with name or id
            "(?i)date.*(check|attend)",          // mentions date and check/attend
            "(?i)total.*(checked|attendance)"    // total checked in or attendance
        };

        for (String regex : patterns) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a summary of attendance for a given class by calling a PHP endpoint.
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
                                      .append(" on ").append(date)
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
            systemMessage.put("content", "Your name is Saki. You are an AI assistant for CheckMates attendance applications that answers questions about class attendance. "
                    + "You can answer anything about CheckMates application.CheckMates application has Create Class, Records and Saki ( which is you )"
                    + "Create Class button is to create class as a one instance that create  the QR code and 4 random passcodes that lead students to check in website where they can type in their student id, name, passcode and date. Students must type in correct passcodes in order for their submission to be save in the database. Please show the students the generated passcode"
                    + "Records button is to view created class attendance, each created class is as one instance, the class that user created will not be save as a permanent instance but rather a one instance that user can delete, edit and view the checked in students of each created class, where user can do the same operations as View Class (add,edit,delete,refresh)"
                    + "Saki button is to launch Saki (You!) to help the user about attendance data, you have to help them access specific student data when asked about. The user don't have to look all over at the Records. They just can ask you. "
                    + "Saki (You!) will also have provide analyze between classes as well. For example, if the user asks for the analysis of attendance between all of the create classes, you have the data so you will provide the analysis"
                    + "You must provide the data in a bullet point manner with each section with a new line. For example, student id then new line and so on. In summary, make the answer looks beautiful as much as possible. Don't concatenate all the infos in one single line."
                    + "Maintain context from previous interactions when possible.");
            messages.put(systemMessage);
            
            // Add previous conversation history as context (if available)
            if (!conversationHistory.isEmpty()) {
                StringBuilder history = new StringBuilder();
                for (String line : conversationHistory) {
                    history.append(line).append("\n");
                }
                JSONObject historyMessage = new JSONObject();
                historyMessage.put("role", "system");
                historyMessage.put("content", "Conversation history:\n" + history.toString());
                messages.put(historyMessage);
            }
            
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
                    .header("Authorization", "Bearer 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")
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

    /**
     * Logs user feedback for adaptive learning.
     */
    public static void logUserFeedback(String feedback) {
        System.out.println("User Feedback: " + feedback);
        conversationHistory.add("Feedback: " + feedback);
    }

    // For testing purposes
    public static void main(String[] args) {
        int classId = 12;
        String classContext = "Math 101 - A";
        String userQuery = "What is the attendance for this class? How many students have checked in?";
        String response = processUserMessage(userQuery, classId, classContext);
        System.out.println("ChatGPT Response: " + response);

        // Simulate collecting user feedback after the response
        String feedback = "The response was helpful but could include more detailed statistics.";
        logUserFeedback(feedback);
    }
}
