package view;

import controller.LoginController;
import javax.swing.*;
import java.awt.*;

/**
 * Login Frame - Entry point to the system
 * @author Ishimwe Honore
 */
public class LoginFrame extends JFrame {
    
    private final LoginController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        controller = new LoginController();
        
        setTitle("Inventory Management System - Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel headerLabel = new JLabel("Inventory Management System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // ===== LOGIN FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("User Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Username:"), gbc);
        
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(100, 35));
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        
        JButton registerBtn = new JButton("Register");
        registerBtn.setPreferredSize(new Dimension(100, 35));
        registerBtn.setBackground(new Color(60, 179, 113));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        
        JButton exitBtn = new JButton("Exit");
        exitBtn.setPreferredSize(new Dimension(100, 35));
        exitBtn.setBackground(new Color(220, 20, 60));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        buttonPanel.add(exitBtn);
        
        // ===== ADD PANELS TO FRAME =====
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // ===== BUTTON ACTIONS =====
        
        // Login Button
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (controller.login(username, password)) {
                // Login successful - open dashboard
                this.dispose();
                new DashboardFrame();
            }
            // Error message is handled in controller
        });
        
        // Register Button - Open registration window
        registerBtn.addActionListener(e -> {
            new UserRegistrationDialog(this);
        });
        
        // Exit Button
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        // Enter key to login
        passwordField.addActionListener(e -> loginBtn.doClick());
        
        setVisible(true);
    }
    
    // ===== INNER CLASS: Registration Dialog =====
    class UserRegistrationDialog extends JDialog {
        
        private JTextField regUsernameField;
        private JPasswordField regPasswordField;
        private JPasswordField confirmPasswordField;
        private JComboBox<String> roleComboBox;
        
        public UserRegistrationDialog(JFrame parent) {
            super(parent, "User Registration", true);
            
            setSize(400, 400);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));
            
            // ===== FORM PANEL =====
            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            regUsernameField = new JTextField();
            regPasswordField = new JPasswordField();
            confirmPasswordField = new JPasswordField();
            roleComboBox = new JComboBox<>(new String[]{"ADMIN", "MANAGER", "STAFF"});
            
            formPanel.add(new JLabel("Username:"));
            formPanel.add(regUsernameField);
            formPanel.add(new JLabel("Password:"));
            formPanel.add(regPasswordField);
            formPanel.add(new JLabel("Confirm Password:"));
            formPanel.add(confirmPasswordField);
            formPanel.add(new JLabel("Role:"));
            formPanel.add(roleComboBox);
            
            // ===== BUTTON PANEL =====
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            
            JButton registerBtn = new JButton("Register");
            registerBtn.setBackground(new Color(60, 179, 113));
            registerBtn.setForeground(Color.WHITE);
            
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.setBackground(new Color(220, 20, 60));
            cancelBtn.setForeground(Color.WHITE);
            
            buttonPanel.add(registerBtn);
            buttonPanel.add(cancelBtn);
            
            // ===== INSTRUCTIONS =====
            JTextArea instructions = new JTextArea(
                "Registration Instructions:\n" +
                "• Username: 3-20 characters (letters, numbers, underscore)\n" +
                "• Password: Minimum 6 characters with letters and numbers\n" +
                "• Role: Select appropriate access level"
            );
            instructions.setEditable(false);
            instructions.setBackground(new Color(240, 248, 255));
            instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // ===== ADD TO DIALOG =====
            add(instructions, BorderLayout.NORTH);
            add(formPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
            
            // ===== BUTTON ACTIONS =====
            registerBtn.addActionListener(e -> {
                String username = regUsernameField.getText();
                String password = new String(regPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                
                controller.registerUser(username, password, confirmPassword, role);
                
                // If registration successful, clear fields
                if (username.length() >= 3) { // Simple check if likely successful
                    regUsernameField.setText("");
                    regPasswordField.setText("");
                    confirmPasswordField.setText("");
                }
            });
            
            cancelBtn.addActionListener(e -> dispose());
            
            setVisible(true);
        }
    }
    
    // ===== MAIN METHOD - Entry Point =====
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start application with login
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}