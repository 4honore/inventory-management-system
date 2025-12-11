package view;

import controller.SupplierController;
import model.Supplier;
import model.UserSession; // NEW IMPORT
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SupplierFrame extends JFrame {

    private final SupplierController controller;
    private JTextField idField, nameField, contactField, addressField, emailField;
    private JTable table;
    private DefaultTableModel model;

    // Define CRUD buttons globally
    private JButton addButton, updateButton, deleteButton;

    // Define colors for consistency
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(192, 57, 43);


    public SupplierFrame() {
        controller = new SupplierController();

        // 1. Security Check: Block unauthorized access
        if (!UserSession.hasAnyRole("ADMIN", "MANAGER", "STAFF")) {
            JOptionPane.showMessageDialog(null, "You are not authorized to access the Suppliers module.", "Permission Denied", JOptionPane.ERROR_MESSAGE);
            // Must safely dispose the frame on the EDT
            SwingUtilities.invokeLater(this::dispose); 
            return; // Prevents frame initialization
        }
        
        // Check if the user is authorized to perform modifications
        boolean canModify = UserSession.hasAnyRole("ADMIN", "MANAGER");

        setTitle("Inventory Management - Supplier Module");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // ===== HEADER =====
        JLabel header = new JLabel("Supplier Management (" + (canModify ? "Full Access" : "Read-Only") + ")", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(header);
        add(headerPanel, BorderLayout.NORTH);

        // ===== INPUT FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));

        idField = new JTextField();
        idField.setEditable(false);
        nameField = new JTextField();
        contactField = new JTextField();
        addressField = new JTextField();
        emailField = new JTextField();

        formPanel.add(new JLabel("Supplier ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        // ===== BUTTON PANEL (RBAC IMPLEMENTATION HERE) =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        addButton = createStyledButton("➕ Add Supplier", SUCCESS_COLOR);
        updateButton = createStyledButton("📝 Update Supplier", WARNING_COLOR);
        deleteButton = createStyledButton("🗑️ Delete Supplier", DANGER_COLOR);
        JButton clearButton = createStyledButton("🧹 Clear Fields", new Color(108, 117, 125)); // Non-CRUD action

        // --- RBAC: Enable/Disable/Hide CRUD buttons based on role ---
        if (canModify) {
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
        } else {
            // Read-Only users have all input fields non-editable for safety
            nameField.setEditable(false);
            contactField.setEditable(false);
            addressField.setEditable(false);
            emailField.setEditable(false);
        }
        
        buttonPanel.add(clearButton);
        
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.add(formPanel, BorderLayout.NORTH);
        controlsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(controlsPanel, BorderLayout.NORTH);


        // ===== TABLE PANEL =====
        String[] columnNames = {"ID", "Name", "Contact", "Address", "Email"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells non-editable
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTON LISTENERS (Using try-catch for robustness) =====
        
        addButton.addActionListener(e -> {
            if (canModify) {
                // Supplier doesn't have numeric fields, so NumberFormatException is unlikely,
                // but we keep the robust structure for consistency and future-proofing.
                try {
                    controller.addSupplier(nameField.getText(), contactField.getText(), 
                        addressField.getText(), emailField.getText());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ An unexpected error occurred while adding supplier: " + ex.getMessage(), 
                        "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadSuppliers();
            clearFields();
        });

        updateButton.addActionListener(e -> {
            if (canModify) {
                try {
                    // Check if an ID is selected
                    if (idField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please select a supplier to update.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int id = Integer.parseInt(idField.getText());
                    controller.updateSupplier(id, nameField.getText(), contactField.getText(), 
                        addressField.getText(), emailField.getText());
                } catch (NumberFormatException ex) {
                     // Should not happen as ID is populated from table
                    JOptionPane.showMessageDialog(this, "Internal Error: Invalid Supplier ID.", "System Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ An unexpected error occurred while updating supplier: " + ex.getMessage(), 
                        "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadSuppliers();
            clearFields();
        });

        deleteButton.addActionListener(e -> {
            if (canModify) {
                try {
                    if (idField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please select a supplier to delete.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int id = Integer.parseInt(idField.getText());
                    controller.deleteSupplier(id);
                } catch (NumberFormatException ex) {
                    // Should not happen as ID is populated from table
                    JOptionPane.showMessageDialog(this, "Internal Error: Invalid Supplier ID.", "System Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ An unexpected error occurred while deleting supplier: " + ex.getMessage(), 
                        "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadSuppliers();
            clearFields();
        });

        clearButton.addActionListener(e -> clearFields());

        // ===== TABLE SELECTION LISTENER =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    idField.setText(model.getValueAt(selectedRow, 0).toString());
                    nameField.setText(model.getValueAt(selectedRow, 1).toString());
                    contactField.setText(model.getValueAt(selectedRow, 2).toString());
                    addressField.setText(model.getValueAt(selectedRow, 3).toString());
                    emailField.setText(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        // Load suppliers initially
        loadSuppliers();

        setVisible(true);
    }

    // ===== Helper Methods =====
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }
    
    private void loadSuppliers() {
        model.setRowCount(0);
        List<Supplier> suppliers = controller.getAllSupplier();
        for (Supplier s : suppliers) {
            model.addRow(new Object[]{
                    s.getSupplierId(),
                    s.getName(),
                    s.getContact(),
                    s.getAddress(),
                    s.getEmail()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
        emailField.setText("");
        table.clearSelection();
    }
}