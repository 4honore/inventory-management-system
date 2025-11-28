/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.User;

/**
 *
 * @author FRANK
 */
public class UserDao {

    // CREATE - Add User
    public void addUser(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getRole());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, " User added successfully!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, " Failed to add user: " + e.getMessage());
        }
    }

    // READ - Retrieve all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                users.add(u);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, " Failed to retrieve users: " + e.getMessage());
        }

        return users;
    }

    // UPDATE - Edit User
    public void updateUser(User user) {
        String sql = "UPDATE users SET username=?, password=?, role=? WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, user.getUsername());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getRole());
            pst.setInt(4, user.getUserId());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, " User updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, " User not found!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, " Failed to update user: " + e.getMessage());
        }
    }

    // DELETE - Remove User
    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, userId);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, " User deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, " User not found!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, " Failed to delete user: " + e.getMessage());
        }
    }

    // LOGIN - Verify credentials
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, username);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, " Login successful!");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, " Invalid username or password!");
                    return false;
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, " Login error: " + e.getMessage());
            return false;
        }
    }
}

