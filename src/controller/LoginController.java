package controller;

import dao.UserDao;
import model.User;
import model.UserSession; // <-- NEW IMPORT
import javax.swing.JOptionPane;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Enhanced Login Controller with comprehensive validations and Session Management
 * @author FRANK
 */
public class LoginController {
    
    private final UserDao userDao;
    
    // Validation patterns
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{6,}$"); // At least 6 characters
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 6;
    
    public LoginController() {
        this.userDao = new UserDao();
    }
    
    /**
     * Login user with validations and session management.
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return true if login is successful and session is established, false otherwise.
     */
    public boolean login(String username, String password) {
        
        // ===== BUSINESS VALIDATION 1: Username cannot be empty =====
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Username cannot be empty!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // ===== BUSINESS VALIDATION 2: Password cannot be empty =====
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Password cannot be empty!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // --- Database Authentication ---
        
        // Now calls UserDao.login() which returns User or null
        User user = userDao.login(username, password);
        
        if (user != null) {
            // Login successful. Establish User Session.
            UserSession.login(user);
            return true;
        } else {
            // Login failed (DAO already displayed error message)
            return false;
        }
    }

    // CREATE - Add user
    public void registerUser(String username, String password, String confirmPassword, String role) {
        // Validation 1: Username format
        if (username == null || !USERNAME_PATTERN.matcher(username).matches()) {
            JOptionPane.showMessageDialog(null, 
                "❌ Username must be 3-20 alphanumeric characters or underscores.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation 2: Password strength
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            JOptionPane.showMessageDialog(null, 
                "❌ Password must be at least 6 characters long.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation 3: Password match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, 
                "❌ Passwords do not match!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation 4: Role
        String roleUpper = role != null ? role.toUpperCase() : null;
        if (roleUpper == null || (!roleUpper.equals("ADMIN") && !roleUpper.equals("MANAGER") && !roleUpper.equals("STAFF"))) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid role selected!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); 
        user.setRole(roleUpper);

        try {
            userDao.addUser(user);
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, 
                "❌ Error adding user: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // UPDATE - Update user
    public void updateUser(int userId, String username, String password, String role) {
        
        if (userId <= 0) {
            JOptionPane.showMessageDialog(null, "❌ Invalid user ID!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validation 1: Username format
        if (username == null || !USERNAME_PATTERN.matcher(username).matches()) {
            JOptionPane.showMessageDialog(null, "❌ Username must be 3-20 alphanumeric characters or underscores.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation 2: Password strength
        if (password == null || password.trim().isEmpty() || !PASSWORD_PATTERN.matcher(password).matches()) {
             JOptionPane.showMessageDialog(null, "❌ Password must be at least 6 characters long.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation 3: Role
        String roleUpper = role != null ? role.toUpperCase() : null;
        if (roleUpper == null || (!roleUpper.equals("ADMIN") && !roleUpper.equals("MANAGER") && !roleUpper.equals("STAFF"))) {
            JOptionPane.showMessageDialog(null, "❌ Invalid role selected!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password); 
        user.setRole(roleUpper);
        
        try {
            userDao.updateUser(user);
            JOptionPane.showMessageDialog(null, 
                "✅ User updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Error updating user: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete user
     */
    public void deleteUser(int userId) {
        
        if (userId <= 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid user ID!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            userDao.deleteUser(userId);
            JOptionPane.showMessageDialog(null, 
                "✅ User deleted successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Error deleting user: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}