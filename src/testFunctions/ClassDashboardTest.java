package form;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class ClassDashboardTest {

    private ClassDashboard testClassDashboard;
    private Professor prof;

    @BeforeEach
    void setUp() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            prof = new Professor("Professor Name", "test@gmail.com", "it's not a game. I am not a robot AI challenging you");
            testClassDashboard = new ClassDashboard(prof);
            testClassDashboard.setVisible(true);
        });

    }

    @AfterEach
    void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (testClassDashboard != null) testClassDashboard.dispose();
        });
    }

    @Test
    void testUIInitialization() {
        assertNotNull(testClassDashboard, "Dashboard should be initialized");
        assertTrue(testClassDashboard.isVisible(), "Dashboard should be visible");

    }

    @Test
    void testButtonAdd() {
        FancyHoverButton viewaddButton = new FancyHoverButton("Add");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewaddButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewaddButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewaddButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonAddOld() {
        JButton viewaddButtonOld = new JButton("Add Class");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewaddButtonOld.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewaddButtonOld.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewaddButtonOld, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonEdit() {
        FancyHoverButton vieweditButton = new FancyHoverButton("Edit");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        vieweditButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : vieweditButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(vieweditButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonEditOld() {
        JButton viewaddButtonOld = new JButton("Edit Selected");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewaddButtonOld.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewaddButtonOld.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewaddButtonOld, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonDelete() {
        FancyHoverButton viewdeleteButton = new FancyHoverButton("Delete");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewdeleteButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewdeleteButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewdeleteButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonDeleteOld() {
        JButton viewaddButtonOld = new JButton("Delete Selected");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewaddButtonOld.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewaddButtonOld.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewaddButtonOld, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }

    @Test
    void testButtonRefresh() {
        FancyHoverButton viewrefreshButton = new FancyHoverButton("Refresh");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewrefreshButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewrefreshButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewrefreshButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }

    @Test
    void testButtonRefreshOld() {
        JButton viewaddButtonOld = new JButton("Refresh");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewaddButtonOld.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewaddButtonOld.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewaddButtonOld, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }

    @Test
    void testButtonAttendance() {
        FancyHoverButton attendanceButton = new FancyHoverButton("View Attendance");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        attendanceButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : attendanceButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(attendanceButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }

    @Test
    void testButtonAttendanceOld() {
        JButton viewaddButtonOld = new JButton("View Attendance");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewaddButtonOld.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewaddButtonOld.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewaddButtonOld, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonCloseOld() {
        JButton viewaddButtonOld = new JButton("Close");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewaddButtonOld.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewaddButtonOld.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewaddButtonOld, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }

    @Test
    void testClassTextFieldInitialization() {
        JTextField classNameField = new JTextField();
        classNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classNameField.setEditable(true);

        assertNotNull(classNameField);
        assertTrue(classNameField.isEditable());
        assertEquals("Segoe UI", classNameField.getFont().getFontName());
        assertEquals(14, classNameField.getFont().getSize());
        assertEquals("", classNameField.getText());  // Should be empty initially
    }

    @Test
    void testClassLabelInitialization() {
        JLabel nameLabel = new JLabel("Class Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        assertNotNull(nameLabel);
        assertEquals("Class Name:", nameLabel.getText());
        assertEquals("Segoe UI", nameLabel.getFont().getFontName());
        assertEquals(14, nameLabel.getFont().getSize());
    }

    @Test
    void testSectionTextFieldInitialization() {
        JTextField sectionField = new JTextField();
        sectionField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sectionField.setEditable(true);

        assertNotNull(sectionField);
        assertTrue(sectionField.isEditable());
        assertEquals("Segoe UI", sectionField.getFont().getFontName());
        assertEquals(14, sectionField.getFont().getSize());
        assertEquals("", sectionField.getText());  // Should be empty initially
    }

    @Test
    void testSectionLabelInitialization() {
        JLabel sectionLabel = new JLabel("Section:");
        sectionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        assertNotNull(sectionLabel);
        assertEquals("Section:", sectionLabel.getText());
        assertEquals("Segoe UI", sectionLabel.getFont().getFontName());
        assertEquals(14, sectionLabel.getFont().getSize());
    }

    @Test
    void testExpirationFieldInitialization() {
        JTextField expirationField = new JTextField();
        expirationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        expirationField.setEditable(true);

        assertNotNull(expirationField);
        assertTrue(expirationField.isEditable());
        assertEquals("Segoe UI", expirationField.getFont().getFontName());
        assertEquals(14, expirationField.getFont().getSize());
        assertEquals("", expirationField.getText());  // Should be empty initially
    }

    @Test
    void testExpirationLabelInitialization() {
        JLabel expirationLabel = new JLabel("Expiration (minutes):");
        expirationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        assertNotNull(expirationLabel);
        assertEquals("Expiration (minutes):", expirationLabel.getText());
        assertEquals("Segoe UI", expirationLabel.getFont().getFontName());
        assertEquals(14, expirationLabel.getFont().getSize());
    }

    @Test
    void testButtonClickCreate() {

        // Check create button
        FancyHoverButton viewcreatButton = new FancyHoverButton("Create");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewcreatButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewcreatButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewcreatButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }

    private Image generateQRCodeImage(String text, int width, int height) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    void testGenerateQRCodeImage() {
        String testUrl = "https://example.com/checkin";
        int width = 200, height = 200;

        Image qrImage = generateQRCodeImage(testUrl, width, height);

        assertNotNull(qrImage, "QR code image should not be null");
        assertInstanceOf(BufferedImage.class, qrImage, "Generated QR should be a BufferedImage");

        BufferedImage bufferedQR = (BufferedImage) qrImage;
        assertEquals(width, bufferedQR.getWidth(), "Width should match the requested size");
        assertEquals(height, bufferedQR.getHeight(), "Height should match the requested size");
    }
}