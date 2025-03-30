package form;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 *
 * @author Vinyukov
 */


class LoginTest {
    private Login loginForm;

    @BeforeEach
    void setUp() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            loginForm = new Login();
            loginForm.setVisible(true);
        });
    }

    @AfterEach
    void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (loginForm != null) loginForm.dispose();
        });
    }

    @Test
    void testFormInitialization() {
        assertNotNull(loginForm, "Login form should be initialized");
        assertTrue(loginForm.isUndecorated(), "Window should be undecorated");
        assertEquals(Color.WHITE, loginForm.getBackground(), "Background should be white");
    }

    @Test
    void testUIComponentsExist() {
        // Check for email field using multiple approaches
        JTextField emailField = findComponentByName(loginForm.getContentPane(), "jUserName");
        if (emailField == null) {
            emailField = findComponentByClass(loginForm.getContentPane(), JTextField.class);
        }
        assertNotNull(emailField, "Email field should exist");
        assertTrue(emailField.isVisible(), "Email field should be visible");

        // Check password field
        JPasswordField passwordField = findComponentByClass(loginForm.getContentPane(), JPasswordField.class);
        assertNotNull(passwordField, "Password field should exist");
        assertTrue(passwordField.isVisible(), "Password field should be visible");

        // Check login button
        JButton loginButton = findComponentByText(loginForm.getContentPane(), "LOGIN");
        assertNotNull(loginButton, "Login button should exist");
        assertTrue(loginButton.isVisible(), "Login button should be visible");
    }

    @Test
    void testLoginButtonBehavior() throws InvocationTargetException, InterruptedException {
        JButton loginButton = findComponentByText(loginForm.getContentPane(), "LOGIN");
        assertNotNull(loginButton, "Login button should exist");

        // Test button properties
        assertEquals("LOGIN", loginButton.getText(), "Button should have correct text");
        assertEquals(Color.WHITE, loginButton.getBackground(), "Button should have white background");

        // Test button action
        SwingUtilities.invokeAndWait(() -> loginButton.doClick());
    }

    @Test
    void testSignUpLinkExists() {
        JLabel signUpLink = findComponentByText(loginForm.getContentPane(), "Sign up here!");
        assertNotNull(signUpLink, "Sign up link should exist");
        assertEquals(Color.BLACK, signUpLink.getForeground(), "Link should be black");
        assertTrue(signUpLink.isVisible(), "Sign up link should be visible");
    }

    // Improved helper methods with multiple search approaches
    private <T extends Component> T findComponentByName(Container container, String name) {
        for (Component comp : container.getComponents()) {
            if (name.equals(comp.getName())) {
                return (T) comp;
            }
            if (comp instanceof Container) {
                T found = findComponentByName((Container) comp, name);
                if (found != null) return found;
            }
        }
        return null;
    }

    private <T extends Component> T findComponentByClass(Container container, Class<T> componentClass) {
        for (Component comp : container.getComponents()) {
            if (componentClass.isInstance(comp)) {
                return componentClass.cast(comp);
            }
            if (comp instanceof Container) {
                T found = findComponentByClass((Container) comp, componentClass);
                if (found != null) return found;
            }
        }
        return null;
    }

    private <T extends Component> T findComponentByText(Container container, String text) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton && text.equals(((JButton) comp).getText())) {
                return (T) comp;
            }
            if (comp instanceof JLabel && ((JLabel) comp).getText() != null &&
                    ((JLabel) comp).getText().contains(text)) {
                return (T) comp;
            }
            if (comp instanceof Container) {
                T found = findComponentByText((Container) comp, text);
                if (found != null) return found;
            }
        }
        return null;
    }

    // For debugging purposes (not used in tests but helpful during development)
    private void printComponentHierarchy(Container container, int level) {
        for (Component comp : container.getComponents()) {
            System.out.println("  ".repeat(level) + comp.getClass().getSimpleName() +
                    " - Name: " + comp.getName() +
                    " - Visible: " + comp.isVisible() +
                    (comp instanceof JLabel ? " - Text: " + ((JLabel)comp).getText() : "") +
                    (comp instanceof JButton ? " - Text: " + ((JButton)comp).getText() : "") +
                    (comp instanceof JTextField ? " - Text: " + ((JTextField)comp).getText() : ""));

            if (comp instanceof Container) {
                printComponentHierarchy((Container) comp, level + 1);
            }
        }
    }
}