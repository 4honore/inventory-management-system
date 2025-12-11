package view;

import controller.LoginController;
import model.User;
import model.UserSession; // NEW IMPORT
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * User Management Frame - Admin functionality for managing users
 */
public class UserManagementFrame extends JFrame {
    
    private final LoginController controller;
    private JTextField idField, usernameField;
    // Changed to JPasswordField for security best practice
    private JPasswordField passwordField; 
    private JComboBox<String> roleComboBox;
    private JTable table;
    private DefaultTableModel model;
    
    // Define CRUD buttons globally
    private JButton addButton, updateButton, deleteButton;
    
    // Define colors for consistency
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(192, 57, 43);

    
    public UserManagementFrame() {
        controller = new LoginController();
        
        // 1. Security Check: ONLY ADMIN has access
        if (!UserSession.hasRole("ADMIN")) {
            JOptionPane.showMessageDialog(null, "You are not authorized to access the User Management module.", "Permission Denied", JOptionPane.ERROR_MESSAGE);
            // Must safely dispose the frame on the EDT
            SwingUtilities.invokeLater(this::dispose); 
            return; // Prevents frame initialization
        }
        
        setTitle("Inventory Management - User Management (ADMIN ONLY)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // ===== HEADER =====
        JLabel header = new JLabel("User Account Management", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(header);
        add(headerPanel, BorderLayout.NORTH);
        
        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        
        idField = new JTextField();
        idField.setEditable(false); // ID should not be manually edited
        usernameField = new JTextField();
        passwordField = new JPasswordField(); // Using JPasswordField
        roleComboBox = new JComboBox<>(new String[]{"ADMIN", "MANAGER", "STAFF"});
        
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox);

        // ===== BUTTON PANEL (No RBAC checks needed here as the whole frame is ADMIN-only) =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        addButton = createStyledButton("➕ Add User", SUCCESS_COLOR);
        updateButton = createStyledButton("📝 Update User", WARNING_COLOR);
        deleteButton = createStyledButton("🗑️ Delete User", DANGER_COLOR);
        JButton clearButton = createStyledButton("🧹 Clear Fields", new Color(108, 117, 125));
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.add(formPanel, BorderLayout.NORTH);
        controlsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(controlsPanel, BorderLayout.NORTH);

        // ===== TABLE PANEL =====
        String[] columnNames = {"ID", "Username", "Password (Hidden)", "Role"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells non-editable
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // ===== BUTTON LISTENERS =====
        
        addButton.addActionListener(e -> {
            String username = usernameField.getText();
            // Get password from JPasswordField
            String password = new String(passwordField.getPassword()); 
            String role = (String) roleComboBox.getSelectedItem();
            
            // The LoginController's registerUser handles Add/Update/Delete logic
            // We use the same method for adding a new user here.
            controller.registerUser(username, password, password, role); // Pass password twice for consistency
            
            loadUsers();
            clearFields();
        });

        updateButton.addActionListener(e -> {
            try {
                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select a user to update.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idField.getText());
                String username = usernameField.getText();
                // When updating, we use the entered password (or assume it's a new password)
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                
                // If password field is empty, a robust update logic would typically ignore the password update.
                // For simplicity here, we assume if the field is not empty, update it. If it is empty, we must handle it.
                // The LoginController update method already handles the logic for updating the user.
                controller.updateUser(id, username, password, role);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Internal Error: Invalid User ID.", "System Error", JOptionPane.ERROR_MESSAGE);
            }
            loadUsers();
            clearFields();
        });

        deleteButton.addActionListener(e -> {
            try {
                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idField.getText());
                controller.deleteUser(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Internal Error: Invalid User ID.", "System Error", JOptionPane.ERROR_MESSAGE);
            }
            loadUsers();
            clearFields();
        });

        clearButton.addActionListener(e -> clearFields());

        // ===== TABLE SELECTION LISTENER =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Only fill ID, Username, and Role. Password field is usually left blank or cleared.
                    idField.setText(model.getValueAt(selectedRow, 0).toString());
                    usernameField.setText(model.getValueAt(selectedRow, 1).toString());
                    passwordField.setText(""); // IMPORTANT: Never populate password field from table!
                    
                    String role = model.getValueAt(selectedRow, 3).toString();
                    roleComboBox.setSelectedItem(role);
                }
            }
        });
        
        // Load users initially
        loadUsers();
        
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
    
    private void loadUsers() {
        model.setRowCount(0); // Clear existing rows
        List<User> users = controller.getAllUsers();
        
        for (User u : users) {
            model.addRow(new Object[]{
                u.getUserId(),
                u.getUsername(),
                "********", // Hide password in table for security
                u.getRole()
            });
        }
    }
    
    private void clearFields() {
        idField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        table.clearSelection();
    }
}