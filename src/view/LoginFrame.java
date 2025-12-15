package view;

import controller.LoginController;
import model.UserSession; // Import the UserSession for logout functionality
import javax.swing.*;
import java.awt.*;

/**
 * Login Frame - Entry point to the system, now with enhanced UI and Session handling.
 * @author Ishimwe Honore
 */
public class LoginFrame extends JFrame {
    
    private final LoginController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    // Define professional color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Professional Blue
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray
    private static final Color FORM_PANEL_COLOR = Color.WHITE;
    private static final Color BUTTON_COLOR = new Color(39, 174, 96); // Green for action
    private static final Color BUTTON_HOVER_COLOR = new Color(46, 204, 113);

    public LoginFrame() {
        System.out.println("DEBUG: LoginFrame constructor started."); // <-- DEBUG POINT 1
        controller = new LoginController();
        
        setTitle("Inventory Management System - Login");
        setSize(800, 500); // Increased size for better layout
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Main container uses BorderLayout
        
        // Clear any previous session on startup (Good practice)
        UserSession.logout();

        // ===== 1. LEFT PANEL (Branding/Image) =====
        System.out.println("DEBUG: Creating Branding Panel."); // <-- DEBUG POINT 2
        JPanel leftPanel = createBrandingPanel();
        add(leftPanel, BorderLayout.WEST);

        // ===== 2. RIGHT PANEL (Login Form) =====
        System.out.println("DEBUG: Creating Login Form Panel."); // <-- DEBUG POINT 3
        JPanel rightPanel = createLoginFormPanel();
        add(rightPanel, BorderLayout.CENTER);
        
        System.out.println("DEBUG: Calling setVisible(true)."); // <-- DEBUG POINT 4
        setVisible(true);
        System.out.println("DEBUG: LoginFrame constructor finished successfully."); // <-- DEBUG POINT 5
    }
    
    // ... (rest of createBrandingPanel, createLoginFormPanel, createStyledButton methods are unchanged)
    
    // =================================================================
    //                    UI HELPER METHODS (Unchanged)
    // =================================================================
    
    private JPanel createBrandingPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(300, 500));
        
        // Add a visual element (Image/Logo Placeholder)
        // If the crash happens here, it's likely due to Font initialization
        JLabel logoLabel = new JLabel("INVENTORY HUB", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
        logoLabel.setForeground(Color.WHITE);
        
        JLabel sloganLabel = new JLabel("Streamlined Stock Control", SwingConstants.CENTER);
        sloganLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        sloganLabel.setForeground(Color.LIGHT_GRAY);
        
        panel.add(logoLabel, new GridBagConstraints());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(sloganLabel, gbc);

        return panel;
    }

    private JPanel createLoginFormPanel() {
        // ... (body of this method is unchanged)
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // --- Header ---
        JLabel titleLabel = new JLabel("LOGIN TO YOUR ACCOUNT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR.darker());
        panel.add(titleLabel, BorderLayout.NORTH);

        // --- Form Fields ---
        JPanel formLayoutPanel = new JPanel(new GridBagLayout());
        formLayoutPanel.setBackground(FORM_PANEL_COLOR);
        formLayoutPanel.setBorder(BorderFactory.createCompoundBorder(
             BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
             BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Username Label
        gbc.gridx = 0; gbc.gridy = 0; 
        gbc.anchor = GridBagConstraints.WEST;
        formLayoutPanel.add(new JLabel("Username:"), gbc);
        
        // Username Field
        gbc.gridx = 0; gbc.gridy = 1; 
        gbc.weightx = 1.0; 
        formLayoutPanel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0; gbc.gridy = 2; 
        gbc.anchor = GridBagConstraints.WEST;
        formLayoutPanel.add(new JLabel("Password:"), gbc);

        // Password Field
        gbc.gridx = 0; gbc.gridy = 3; 
        gbc.weightx = 1.0; 
        formLayoutPanel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = createStyledButton("LOGIN", BUTTON_COLOR, BUTTON_HOVER_COLOR);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(25, 10, 5, 10);
        formLayoutPanel.add(loginButton, gbc);
        
        // Register Link/Button
        JButton registerButton = new JButton("Register New User");
        registerButton.setFont(new Font("Arial", Font.ITALIC, 12));
        registerButton.setForeground(PRIMARY_COLOR);
        registerButton.setBackground(FORM_PANEL_COLOR);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.insets = new Insets(5, 10, 0, 10);
        formLayoutPanel.add(registerButton, gbc);

        panel.add(formLayoutPanel, BorderLayout.CENTER);
        
        // ===== ACTION LISTENERS =====
        loginButton.addActionListener(e -> attemptLogin());
        registerButton.addActionListener(e -> new RegistrationDialog(this).setVisible(true));
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        // ... (body of this method is unchanged)
        
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }
    
    // ... (rest of attemptLogin and RegistrationDialog methods are unchanged)

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // The controller now handles validation AND session establishment
        boolean success = controller.login(username, password);
        
        if (success) {
            // Login successful and UserSession is established
            this.dispose(); // Close the login window
            new DashboardFrame().setVisible(true); // Open the main dashboard
        } else {
            // Login failed (Controller/DAO already showed error message)
            passwordField.setText(""); // Clear password field on failure
        }
    }
    
    private class RegistrationDialog extends JDialog {
        
        private final JTextField regUsernameField;
        private final JPasswordField regPasswordField;
        private final JPasswordField confirmPasswordField;
        private final JComboBox<String> roleComboBox;
        
        public RegistrationDialog(JFrame parent) {
            super(parent, "Register New User", true);
            
            setSize(400, 450);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));
            
            // --- Header ---
            JLabel headerLabel = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
            headerLabel.setForeground(PRIMARY_COLOR);
            headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            add(headerLabel, BorderLayout.NORTH);

            // --- Form Panel ---
            JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            regUsernameField = new JTextField();
            regPasswordField = new JPasswordField();
            confirmPasswordField = new JPasswordField();
            roleComboBox = new JComboBox<>(new String[]{"ADMIN", "MANAGER", "STAFF"}); // Roles

            formPanel.add(new JLabel("Username:"));
            formPanel.add(regUsernameField);
            formPanel.add(new JLabel("Password:"));
            formPanel.add(regPasswordField);
            formPanel.add(new JLabel("Confirm Password:"));
            formPanel.add(confirmPasswordField);
            formPanel.add(new JLabel("Role:"));
            formPanel.add(roleComboBox);
            
            // Empty row for spacing
            formPanel.add(new JLabel()); 
            formPanel.add(new JLabel()); 

            add(formPanel, BorderLayout.CENTER);
            
            // --- Button Panel ---
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            JButton registerBtn = createStyledButton("Register", BUTTON_COLOR, BUTTON_HOVER_COLOR);
            JButton cancelBtn = createStyledButton("Cancel", Color.GRAY, Color.DARK_GRAY);
            
            buttonPanel.add(registerBtn);
            buttonPanel.add(cancelBtn);
            add(buttonPanel, BorderLayout.SOUTH);
            
            // ===== BUTTON ACTIONS =====
            registerBtn.addActionListener(e -> {
                String username = regUsernameField.getText();
                String password = new String(regPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                
                controller.registerUser(username, password, confirmPassword, role);
                
                // If registration was successful (simple heuristic check), close dialog
                // The controller shows a success message, so we just clear/close
                if (UserSession.getCurrentUser() == null) { // If the user wasn't logged in (which shouldn't happen here)
                    regUsernameField.setText("");
                    regPasswordField.setText("");
                    confirmPasswordField.setText("");
                    this.dispose();
                }
            });
            
            cancelBtn.addActionListener(e -> dispose());
        }
    }
    
    // ===== MAIN METHOD - Entry Point =====
    public static void main(String[] args) {
        System.out.println("--- Starting LoginFrame Main Method ---"); // <-- DEBUG POINT A
        
        // Use the default cross-platform L&F instead of the system L&F,
        // which often fails in non-native environments like Docker/Linux.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            System.out.println("DEBUG: Set UIManager L&F successfully."); // <-- DEBUG POINT B
        } catch (Exception e) {
            System.err.println("ERROR: Failed to set Look and Feel. Starting anyway.");
            e.printStackTrace();
            // Do not crash the application if L&F fails.
        }
        
        // Start application with login
        System.out.println("DEBUG: Calling SwingUtilities.invokeLater."); // <-- DEBUG POINT C
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}