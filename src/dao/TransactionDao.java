package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Transaction;

public class TransactionDao {

    // ===== CREATE - Add Transaction =====
    public void addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (product_id, quantity, date, type, total) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, transaction.getProductId());
            pst.setInt(2, transaction.getQuantity());
            pst.setTimestamp(3, transaction.getDate());
            pst.setString(4, transaction.getType());
            pst.setDouble(5, transaction.getTotal());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, " Transaction recorded successfully!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Failed to add transaction: " + e.getMessage());
        }
    }

    // ===== READ - Retrieve all transactions =====
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setProductId(rs.getInt("product_id"));
                t.setQuantity(rs.getInt("quantity"));
                t.setDate(rs.getTimestamp("date"));
                t.setType(rs.getString("type"));
                t.setTotal(rs.getDouble("total"));
                transactions.add(t);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Failed to retrieve transactions: " + e.getMessage());
        }

        return transactions;
    }

    // ===== UPDATE - Edit Transaction =====
    public void updateTransaction(Transaction transaction) {
        String sql = "UPDATE transactions SET product_id=?, quantity=?, date=?, type=?, total=? WHERE transaction_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, transaction.getProductId());
            pst.setInt(2, transaction.getQuantity());
            pst.setTimestamp(3, transaction.getDate());
            pst.setString(4, transaction.getType());
            pst.setDouble(5, transaction.getTotal());
            pst.setInt(6, transaction.getTransactionId());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, " Transaction updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, " Transaction not found!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, " Failed to update transaction: " + e.getMessage());
        }
    }

    // ===== DELETE - Remove Transaction =====
    public void deleteTransaction(int transactionId) {
        String sql = "DELETE FROM transactions WHERE transaction_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, transactionId);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, " Transaction deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "⚠️ Transaction not found!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Failed to delete transaction: " + e.getMessage());
        }
    }

    // ===== READ SINGLE - Get by ID =====
    public Transaction getTransactionById(int id) {
        Transaction transaction = null;
        String sql = "SELECT * FROM transactions WHERE transaction_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                transaction = new Transaction();
                transaction.setTransactionId(rs.getInt("transaction_id"));
                transaction.setProductId(rs.getInt("product_id"));
                transaction.setQuantity(rs.getInt("quantity"));
                transaction.setDate(rs.getTimestamp("date"));
                transaction.setType(rs.getString("type"));
                transaction.setTotal(rs.getDouble("total"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, " Failed to fetch transaction: " + e.getMessage());
        }
        return transaction;
    }
}
