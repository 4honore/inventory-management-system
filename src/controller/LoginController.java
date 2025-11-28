package controller;

import dao.UserDao;
import model.User;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Enhanced Login Controller with comprehensive validations
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
     * Login user with validations
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
        
        // ===== TECHNICAL VALIDATION 1: Username format =====
        if (!USERNAME_PATTERN.matcher(username.trim()).matches()) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid username format!\n" +
                "Username must be 3-20 characters and contain only letters, numbers, and underscores.", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // ===== TECHNICAL VALIDATION 2: Password minimum length =====
        if (password.length() < MIN_PASSWORD_LENGTH) {
            JOptionPane.showMessageDialog(null, 
                "❌ Password must be at least " + MIN_PASSWORD_LENGTH + " characters long!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Attempt login
        return userDao.login(username.trim(), password);
    }
    
    /**
     * Register new user with comprehensive validations
     */
    public void registerUser(String username, String password, String confirmPassword, String role) {
        
        // ===== BUSINESS VALIDATION 1: All fields required =====
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Username is required!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Password is required!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (role == null || role.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Role is required!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // ===== BUSINESS VALIDATION 2: Password confirmation must match =====
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Passwords do not match!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // ===== BUSINESS VALIDATION 3: Role must be valid =====
        String roleUpper = role.trim().toUpperCase();
        if (!roleUpper.equals("ADMIN") && !roleUpper.equals("MANAGER") && !roleUpper.equals("STAFF")) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Invalid role! Must be ADMIN, MANAGER, or STAFF.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // ===== BUSINESS VALIDATION 4: Check for duplicate username =====
        List<User> existingUsers = userDao.getAllUsers();
        for (User u : existingUsers) {
            if (u.getUsername().equalsIgnoreCase(username.trim())) {
                JOptionPane.showMessageDialog(null, 
                    "⚠️ Username already exists! Please choose another.", 
                    "Duplicate Username", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        
        // ===== TECHNICAL VALIDATION 1: Username format =====
        if (!USERNAME_PATTERN.matcher(username.trim()).matches()) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid username format!\n" +
                "Username must be 3-20 characters and contain only letters, numbers, and underscores.", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ===== TECHNICAL VALIDATION 2: Username length =====
        if (username.trim().length() < MIN_USERNAME_LENGTH || 
            username.trim().length() > MAX_USERNAME_LENGTH) {
            JOptionPane.showMessageDialog(null, 
                "❌ Username must be between " + MIN_USERNAME_LENGTH + 
                " and " + MAX_USERNAME_LENGTH + " characters!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ===== TECHNICAL VALIDATION 3: Password strength =====
        if (password.length() < MIN_PASSWORD_LENGTH) {
            JOptionPane.showMessageDialog(null, 
                "❌ Password must be at least " + MIN_PASSWORD_LENGTH + " characters long!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ===== TECHNICAL VALIDATION 4: Password complexity check =====
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        
        if (!hasLetter || !hasDigit) {
            JOptionPane.showMessageDialog(null, 
                "❌ Password must contain at least one letter and one number!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ===== TECHNICAL VALIDATION 5: Role format validation =====
        if (!role.trim().matches("^[A-Z]+$")) {
            JOptionPane.showMessageDialog(null, 
                "❌ Role must contain only uppercase letters!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create and save user
        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setPassword(password); // In production, hash the password!
        newUser.setRole(roleUpper);
        
        try {
            userDao.addUser(newUser);
            JOptionPane.showMessageDialog(null, 
                "✅ User registered successfully!\n" +
                "Username: " + username + "\n" +
                "Role: " + roleUpper, 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Error registering user: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update user with validations
     */
    public void updateUser(int userId, String username, String password, String role) {
        
        // Technical validation for ID
        if (userId <= 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid user ID!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Apply same validations as registration
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Username is required!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!USERNAME_PATTERN.matcher(username.trim()).matches()) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid username format!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String roleUpper = role.trim().toUpperCase();
        if (!roleUpper.equals("ADMIN") && !roleUpper.equals("MANAGER") && !roleUpper.equals("STAFF")) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Invalid role!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username.trim());
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