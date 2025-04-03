package form;

import static org.junit.jupiter.api.Assertions.*;

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



class DashboardTest {
    private Dashboard testDashboard;
    private Professor prof;

    @BeforeEach
    void setUp()throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
        prof = new Professor("Professor Name", "test@gmail.com", "it's not a game. I am not a robot AI challenging you");
        testDashboard = new Dashboard(prof);
        testDashboard.setVisible(true);
    });

    }

    @Test
    void testUIInitialization() {
        assertNotNull(testDashboard, "Dashboard should be initialized");
        assertTrue(testDashboard.isVisible(), "Dashboard should be visible");
        assertEquals(Color.WHITE, testDashboard.getBackground(), "Background should be white");
    }



    @Test
    void testButtonClickCreateClass() {
        FancyHoverButton viewClassButton = new FancyHoverButton("Create Class");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewClassButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewClassButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewClassButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonClickSaki() {
        FancyHoverButton viewSakiButton = new FancyHoverButton("Saki");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewSakiButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewSakiButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewSakiButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonClickRecord() {
        FancyHoverButton viewRecordButton = new FancyHoverButton("Record");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewRecordButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewRecordButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewRecordButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonClickOldTimeyStyle() {
        FancyHoverButton viewOldButton = new FancyHoverButton("Old-Timey Style");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewOldButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewOldButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewOldButton, ActionEvent.ACTION_PERFORMED, "click"));
        }

        assertTrue(clicked[0], "Button click action should be triggered.");
    }
    @Test
    void testButtonClickLogoff() {
        ModernButton viewLogoffButton = new ModernButton("Log Off");
        final boolean[] clicked = {false}; // Track if actionPerformed executes

        viewLogoffButton.addActionListener(e -> clicked[0] = true);

        // Simulate a button click
        for (ActionListener al : viewLogoffButton.getActionListeners()) {
            al.actionPerformed(new ActionEvent(viewLogoffButton, ActionEvent.ACTION_PERFORMED, "click"));
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
        assertEquals("Expiration (minutes):",expirationLabel.getText());
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

