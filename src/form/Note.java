package form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject; // Make sure to include a JSON library
import org.json.JSONArray;
import javafx.embed.swing.JFXPanel;



/**
 *sticky note" style panels in Java Swing
 * with push-pins on the right side and an "Add Note" button.
 */
public class Note {
    private static Professor professor;
    private static int totalNote = 0;
    private static int noteIndex = 0;
    
    public static int getNoteIndex(){
        return noteIndex;
    }
    public static void setNoteIndex(int nI){
        noteIndex = nI;
    }
    public static void decrementNoteIndex(){
        noteIndex--;
    }
    
    public static void incrementNoteIndex(){
        noteIndex++;
    }
    
    public static void setProfessor(Professor prof){
        professor = prof;
    }
    
    public static String getProfessorID(){
        return professor.getProfessorID();
    }
    public static void launch() {
    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Attendnace Notes");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(950, 750);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));
               
        frame.getContentPane().setBackground(new Color(255, 255, 255));  
;

        // 1) Top toolbar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topPanel.setOpaque(false);

        ModernButton addNoteBtn  = new ModernButton("Add Note", true);

        topPanel.add(addNoteBtn);

        frame.add(topPanel, BorderLayout.NORTH);

        // 2) Notes container
        // after you download WrapLayout.java into your project:
        JPanel notesContainer = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));


        JScrollPane scroll = new JScrollPane(notesContainer,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        
        notesContainer.setBackground(new Color(255, 255, 255)); 
        scroll.setOpaque(false);
        scroll.setBackground(new Color(0,0,0,0));

        // Don’t let the viewport paint a background
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(new Color(0,0,0,0));

        // Remove any border so there’s no white border around it:
        scroll.setBorder(null);

        frame.add(scroll, BorderLayout.CENTER);
        
        //Create a list of notes to store the notes
        List<RealisticNotePanel> notes = new ArrayList<>();

        // 3) “Add Note” behavior
        addNoteBtn.addActionListener(e ->
            showNoteDialog(frame, notesContainer, notes)
        );


        //Initial load on startup
        if (professor != null) {
            fetchAndDisplayNotes(
                professor.getProfessorID(),
                notesContainer,
                notes
            );
        }

        frame.setVisible(true);
    });
}



    private static void showNoteDialog(JFrame parent, JPanel container, List<RealisticNotePanel> notes) {
        JDialog dialog = new JDialog(parent, "New Note", true);
        dialog.setLayout(new BorderLayout(0, 0));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JTextArea inputArea = new JTextArea(5, 20);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScroll = new JScrollPane(inputArea);
        
        inputScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        inputScroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        
        dialog.add(inputScroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        FancyHoverButton saveBtn = new FancyHoverButton("Save");
        FancyHoverButton cancelBtn = new FancyHoverButton("Cancel");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        btnPanel.setBackground(new Color(255,255,255));
        dialog.add(btnPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener((ActionEvent ev) -> {
            String text = inputArea.getText().trim();
            if (!text.isEmpty()) {
                Random rand = new Random();
                int firstColorCode = rand.nextInt(55) + 200;
                int secondColorCode = rand.nextInt(55) + 200;
                int thirdColorCode = rand.nextInt(55) + 200;
                RealisticNotePanel note = new RealisticNotePanel(Note.getNoteIndex()+1, text, new Color(firstColorCode, secondColorCode, thirdColorCode), notes);
                
                note.setPreferredSize(new Dimension(260, 180));
                notes.add(note);
                container.add(note);
                container.revalidate();
                container.repaint();
                
                Note.incrementNoteIndex();

            }
            System.out.println("Saving note for professor ID → " + professor.getProfessorID());

            saveNoteToDatabase(professor.getProfessorID(), inputArea.getText().trim());
            
            
            dialog.dispose();
        });
        cancelBtn.addActionListener(ev -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    private static void saveNoteToDatabase(String professorId, String noteText) {
    new Thread(() -> {
        HttpURLConnection conn = null;
        try {
            // 1) Prepare URL & open connection
            URL url = new URL("https://cm8tes.com/createNote.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // 2) Build POST body
            String body = "professor_id=" + URLEncoder.encode(professorId, "UTF-8")
                        + "&note="         + URLEncoder.encode(noteText,   "UTF-8");

            // 3) Send it
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            // 4) Read the response
            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

            String jsonText;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                jsonText = sb.toString();
            }

            // 5) Parse JSON
            JSONObject json = new JSONObject(jsonText);
            String status  = json.optString("status",  "error");
            String message = json.optString("message", "Unknown error");

            // 6) Show result on the Swing thread
            SwingUtilities.invokeLater(() -> {
                if ("success".equalsIgnoreCase(status)) {
                    JOptionPane.showMessageDialog(
                        ((Frame) null),
                        message,
                        "Note Saved",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        ((Frame) null),
                        message,
                        "Error Saving Note",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    ((Frame) null),
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
    private static void fetchAndDisplayNotes(String professorId,JPanel container,List<RealisticNotePanel> notes) {
    new Thread(() -> {
        try {
            String urlString = "http://cm8tes.com/getNote.php?professor_id=" +
                    URLEncoder.encode(String.valueOf(professorId), StandardCharsets.UTF_8.toString());
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300)
                         ? conn.getInputStream()
                         : conn.getErrorStream();

            StringBuilder sb = new StringBuilder();
            try (BufferedReader r = new BufferedReader(
                     new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = r.readLine()) != null) sb.append(line);
            }
            /*
            String raw = sb.toString();
            System.out.println("▶ getNotes raw response: " + raw);
            */

            JSONObject resp = new JSONObject(sb.toString());
            if (!"success".equalsIgnoreCase(resp.optString("status"))) {
                System.err.println("getNotes error: " + resp.optString("message"));
                return;
            }

            JSONArray arr = resp.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String text = obj.getString("note");
                //  pick a random pastel color:
                Random rand = new Random();
                Color bg = new Color(
                    200 + rand.nextInt(56),
                    200 + rand.nextInt(56),
                    200 + rand.nextInt(56)
                );

                RealisticNotePanel note = new RealisticNotePanel(i+1, text, bg, notes);
                note.setPreferredSize(new Dimension(260, 180));
                notes.add(note);
                SwingUtilities.invokeLater(() -> {
                    container.add(note);
                    container.revalidate();
                    container.repaint();
                });
                totalNote++;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }).start();
   }
    
   private static void deleteNoteFromDatabase(String professorId, String note) {
    new Thread(() -> {
        HttpURLConnection conn = null;
        try {
            // 1) Open connection to your delete endpoint
            URL url = new URL("https://cm8tes.com/deleteNote.php");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8"
            );
            conn.setDoOutput(true);

            // 2) Build and send the form body
            String urlParameters = 
                  "professor_id=" + URLEncoder.encode(professorId, "UTF-8")
                + "&note="         + URLEncoder.encode(note,   "UTF-8");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(urlParameters.getBytes(StandardCharsets.UTF_8));
            }

            // 3) Check HTTP response
            int code = conn.getResponseCode();
            System.out.println("▶ deleteNote returned HTTP " + code);

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
                        "Note Deleted",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        message,
                        "Error Deleting Note",
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

   public static void deleteNote(String professorId, String note){
       deleteNoteFromDatabase(professorId, note);
   }

}

/* b*
 * A custom JPanel that renders itself like a "sticky note" with:
 * - drop shadow
 * - colored background
 * - horizontal ruled lines
 * - rounded corners
 * - a push-pin on the right side with a visible needle
 */
class RealisticNotePanel extends JPanel {
    private final JTextArea textArea;

    public RealisticNotePanel(int i, String text, Color background, List<RealisticNotePanel> notes) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBackground(background);
        setBorder(new EmptyBorder(16, 16, 16, 16));
        
        //X button
        ModernButton xButton = new ModernButton("X", false);
        
        // top panel just for the X, right‐aligned
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, -5, -10));
        topPanel.setOpaque(false);
        topPanel.add(xButton);
        add(topPanel, BorderLayout.NORTH);
        
        /*xButton.addActionListener((ActionEvent e) -> {
            dispose();
        });
        */
        
        
    
        textArea = new JTextArea(text);
        textArea.setBackground(new Color(0,0,0,0));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setEditable(false);
        textArea.setBorder(null);
        
         // Wrap in scroll pane for the scroll function
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setOpaque(false);
        scroll.setBackground(new Color(0,0,0,0));
        //Don't let the viewport paint a background
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(new Color(0,0,0,0));
        
        scroll.setBorder(null); // remove border
        scroll.setViewportBorder(null); //Remove default border
        
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        scroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        
        add(scroll, BorderLayout.CENTER);
        
        // 1) Build a wrapper panel
        JPanel textWrapper = new JPanel(new BorderLayout());
        textWrapper.setOpaque(false);

        // 2) Add the scroll‐pane in the center
         textWrapper.add(scroll, BorderLayout.CENTER);

        // 3) Create your footer label (e.g. “1” or any number)
         JLabel footer = new JLabel(String.valueOf(i));
         footer.setHorizontalAlignment(SwingConstants.RIGHT);
         footer.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 6));
        // optional styling:
        footer.setFont(footer.getFont().deriveFont(Font.ITALIC, 12f));
        footer.setForeground(getBackground().darker().darker());


        // 4) Put it in the south
        textWrapper.add(footer, BorderLayout.SOUTH);

        // 5) Finally, add the wrapper to your panel
        add(textWrapper, BorderLayout.CENTER);
        
        xButton.addActionListener(e -> {
            Container parent = RealisticNotePanel.this.getParent();
            if (parent != null) {
                Note.deleteNote(Note.getProfessorID(), text);
                Note.decrementNoteIndex();
                parent.remove(RealisticNotePanel.this);
                parent.revalidate();
                parent.repaint();
            }
            
        });
    }
    
    private void setIndex(JLabel footer,int index){
        footer.setText(String.valueOf(index));
        footer.revalidate();
        footer.repaint();
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        int arc = 20;
        int padding = 16;
        int w = getWidth();
        int h = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1) Drop-shadow
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(padding/2, padding/2, w - padding, h - padding, arc, arc);

        // 2) Note background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, w - padding, h - padding, arc, arc);


        // 4) Border
        g2.setColor(getBackground().darker().darker());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, w - padding, h - padding, arc, arc);

        // 5) Push-pin on right side
        int pinSize = 24;
        int pinX = w - pinSize/2 - padding;
        int pinY = padding/2 + 4;
        
        Random rand = new Random();
        int firstColorCode = rand.nextInt(255) + 1;
        int secondColorCode = rand.nextInt(255) + 1;
        int thirdColorCode = rand.nextInt(255) + 1;

        // Pin head gradient
        GradientPaint headPaint = new GradientPaint(
            pinX, pinY, new Color(firstColorCode, secondColorCode, thirdColorCode),
            pinX + pinSize, pinY + pinSize, new Color(firstColorCode, secondColorCode, thirdColorCode)
        );
        g2.setPaint(headPaint);
        g2.fillOval(pinX, pinY, pinSize, pinSize);

        // Head highlight
        Ellipse2D hi = new Ellipse2D.Float(
            pinX + pinSize*0.2f, pinY + pinSize*0.15f,
            pinSize*0.3f, pinSize*0.3f
        );
        g2.setColor(new Color(255, 255, 255, 180));
        g2.fill(hi);

        // Head outline
        g2.setColor(new Color(150, 100, 0));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawOval(pinX, pinY, pinSize, pinSize);

        // Needle (line and tip)
        int centerX = pinX + pinSize/2;
        int startY = pinY + pinSize;
        int endY = startY + 40;
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(new Color(100, 80, 0));
        g2.drawLine(centerX, startY, centerX, endY);

        Polygon tip = new Polygon(
            new int[]{centerX - 4, centerX + 4, centerX},
            new int[]{endY, endY, endY + 6}, 3
        );
        g2.fill(tip);

        g2.dispose();
        super.paintComponent(g);
    }
}
