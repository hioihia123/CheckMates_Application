package form;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.lang.reflect.Field;

public class AttendanceDashboardTest {
    private AttendanceDashboard dashboard;

    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            dashboard = new AttendanceDashboard(1, false); // classId = 1, modern mode
            dashboard.setVisible(false); // Don't show the GUI during tests
        });
    }

    @Test
    public void testTableInitialization() throws Exception {
        JTable table = getPrivateField(dashboard, "recordsTable", JTable.class);
        assertNotNull("recordsTable should be initialized", table);
    }

    @Test
    public void testSearchFieldInitialization() throws Exception {
        JTextField search = getPrivateField(dashboard, "searchField", JTextField.class);
        assertNotNull("searchField should be initialized", search);
    }

    @Test
    public void testRowFilterSearch() throws Exception {
        JTable table = getPrivateField(dashboard, "recordsTable", JTable.class);
        JTextField search = getPrivateField(dashboard, "searchField", JTextField.class);

        String[] columns = {"ID", "Name"};
        String[][] data = {
                {"1", "Alice"},
                {"2", "Bob"},
                {"3", "Charlie"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);

        SwingUtilities.invokeAndWait(() -> {
            table.setModel(model);
            table.setRowSorter(sorter);
            search.setText("Bob");
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search.getText()));
        });

        assertEquals("Only one row should match 'Bob'", 1, table.getRowCount());
    }

    // Generic helper method to access private fields via reflection
    private <T> T getPrivateField(Object instance, String fieldName, Class<T> type) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return type.cast(field.get(instance));
    }
}


