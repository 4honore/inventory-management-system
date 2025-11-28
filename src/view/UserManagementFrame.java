package view;

import controller.LoginController;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * User Management Frame - Admin functionality for managing users
 * @author Ishimwe Honore
 */
public class UserManagementFrame extends JFrame {
    
    private final LoginController controller;
    private JTextField idField, usernameField, passwordField;
    private JComboBox<String> roleComboBox;
    private JTable table;
    private DefaultTableModel model;
    
    public UserManagementFrame() {
        controller = new LoginController();
        
        setTitle("User Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("User Details"));
        
        idField = new JTextField();
        idField.setEditable(false); // ID should not be manually edited
        usernameField = new JTextField();
        passwordField = new JTextField();
        roleComboBox = new JComboBox<>(new String[]{"ADMIN", "MANAGER", "STAFF"});
        
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox);
        
        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        JButton addBtn = new JButton("Add User");
        JButton viewBtn = new JButton("View All");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        
        // Style buttons
        addBtn.setBackground(new Color(60, 179, 113));
        addBtn.setForeground(Color.WHITE);
        viewBtn.setBackground(new Color(70, 130, 180));
        viewBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(new Color(255, 165, 0));
        updateBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(220, 20, 60));
        deleteBtn.setForeground(Color.WHITE);
        
        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        
        // ===== TABLE PANEL =====
        model = new DefaultTableModel(new String[]{"User ID", "Username", "Password", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("User List"));
        
        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // ===== INSTRUCTIONS PANEL =====
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));
        JTextArea instructions = new JTextArea(
            "• Username: 3-20 characters (letters, numbers, underscore only)\n" +
            "• Password: Minimum 6 characters with at least one letter and one number\n" +
            "• Click a row in the table to select and edit/delete a user"
        );
        instructions.setEditable(false);
        instructions.setBackground(new Color(240, 248, 255));
        instructionPanel.add(instructions);
        
        // ===== ADD PANELS TO FRAME =====
        add(formPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(instructionPanel, BorderLayout.SOUTH);
        
        // ===== BUTTON ACTIONS =====
        
        // Add Button
        addBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = password; // For add, we'll use same password
            String role = (String) roleComboBox.getSelectedItem();
            
            controller.registerUser(username, password, confirmPassword, role);
            clearFields();
            loadUsers();
        });
        
        // View All Button
        viewBtn.addActionListener(e -> loadUsers());
        
        // Update Button
        updateBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "⚠️ Please select a user from the table to update!", 
                        "Selection Required", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int id = Integer.parseInt(idField.getText());
                String username = usernameField.getText();
                String password = passwordField.getText();
                String role = (String) roleComboBox.getSelectedItem();
                
                controller.updateUser(id, username, password, role);
                clearFields();
                loadUsers();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Invalid User ID!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Delete Button
        deleteBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "⚠️ Please select a user from the table to delete!", 
                        "Selection Required", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int id = Integer.parseInt(idField.getText());
                
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this user?\n" +
                    "Username: " + usernameField.getText(),
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteUser(id);
                    clearFields();
                    loadUsers();
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Invalid User ID!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Clear Button
        clearBtn.addActionListener(e -> clearFields());
        
        // ===== TABLE ROW CLICK EVENT =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    idField.setText(model.getValueAt(selectedRow, 0).toString());
                    usernameField.setText(model.getValueAt(selectedRow, 1).toString());
                    passwordField.setText(model.getValueAt(selectedRow, 2).toString());
                    
                    String role = model.getValueAt(selectedRow, 3).toString();
                    roleComboBox.setSelectedItem(role);
                }
            }
        });
        
        // Load users initially
        loadUsers();
        
        setVisible(true);
    }
    
    // ===== HELPER METHODS =====
    
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