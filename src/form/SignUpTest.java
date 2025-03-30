package form;

import org.junit.jupiter.api.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class SignUpTest {
    private SignUp signUpForm;

    @BeforeEach
    void setUp() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            signUpForm = new SignUp();
            signUpForm.setVisible(true);
        });
    }

    @AfterEach
    void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (signUpForm != null) signUpForm.dispose();
        });
    }

    @Test
    void testFormInitialization() {
        assertNotNull(signUpForm, "SignUp form should be initialized");
        assertEquals("Sign Up", signUpForm.getTitle(), "Window title should be correct");
        assertEquals(Color.WHITE, signUpForm.getBackground(), "Background should be white");
        assertEquals(400, signUpForm.getWidth(), "Width should be 400");
        assertEquals(300, signUpForm.getHeight(), "Height should be 300");
    }

    @Test
    void testUIComponentsExist() {
        // Print component hierarchy for debugging
        printComponentHierarchy(signUpForm.getContentPane(), 0);

        // Test all main components exist
        assertNotNull(findComponentByType(signUpForm.getContentPane(), JLabel.class), "Title label should exist");
        assertNotNull(findComponentByType(signUpForm.getContentPane(), JTextField.class), "Name field should exist");
        assertNotNull(findComponentByType(signUpForm.getContentPane(), JPasswordField.class), "Password field should exist");
        assertNotNull(findComponentByType(signUpForm.getContentPane(), JButton.class), "Submit button should exist");
    }

    @Test
    void testFieldProperties() {
        JTextField nameField = findComponentByType(signUpForm.getContentPane(), JTextField.class);
        assertNotNull(nameField, "Name field should exist");

        // Test field properties
        assertEquals(new Font("Arial", Font.PLAIN, 16), nameField.getFont(), "Name field should have correct font");

        if (nameField.getBorder() instanceof LineBorder) {
            assertEquals(Color.GRAY, ((LineBorder)nameField.getBorder()).getLineColor(),
                    "Name field should have gray border");
        }
    }

    // Helper method to find components by type
    private <T extends Component> T findComponentByType(Container container, Class<T> type) {
        for (Component comp : container.getComponents()) {
            if (type.isInstance(comp)) {
                return type.cast(comp);
            }
            if (comp instanceof Container) {
                T found = findComponentByType((Container) comp, type);
                if (found != null) return found;
            }
        }
        return null;
    }

    // Debug method to print component hierarchy
    private void printComponentHierarchy(Container container, int level) {
        for (Component comp : container.getComponents()) {
            System.out.println("  ".repeat(level) + comp.getClass().getSimpleName() +
                    " - Name: " + comp.getName() +
                    " - Text: " + getComponentText(comp) +
                    " - Visible: " + comp.isVisible());
            if (comp instanceof Container) {
                printComponentHierarchy((Container) comp, level + 1);
            }
        }
    }

    private String getComponentText(Component comp) {
        if (comp instanceof JLabel) return ((JLabel) comp).getText();
        if (comp instanceof JButton) return ((JButton) comp).getText();
        if (comp instanceof JTextField) return ((JTextField) comp).getText();
        if (comp instanceof JCheckBox) return ((JCheckBox) comp).getText();
        return "";
    }
}