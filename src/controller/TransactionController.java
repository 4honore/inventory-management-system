package controller;

import dao.TransactionDao;
import dao.ProductDao; // Need to check stock and product existence
import model.Transaction;
import model.Product;
import javax.swing.JOptionPane;
import java.sql.Timestamp;
import java.util.List;

/**
 * Transaction Controller with comprehensive validations and GUI feedback.
 */
public class TransactionController {

    private final TransactionDao transactionDao;
    private final ProductDao productDao; // Dependency for validation

    public TransactionController() {
        this.transactionDao = new TransactionDao();
        this.productDao = new ProductDao(); // Initialize ProductDao
    }

    /**
     * CREATE: Add a new transaction with comprehensive validations.
     */
    public void addTransaction(int productId, int quantity, String type, double total, Timestamp date) {
        
        // BV1: Product ID must be valid
        if (productId <= 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid Product ID!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // BV2: Product must exist in the database
        Product product = productDao.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Product with ID " + productId + " does not exist.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // BV3: Quantity must be positive
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Quantity must be a positive number.", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // BV4: Total must be non-negative (can be 0 for promotional items, etc.)
        if (total < 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Total price cannot be negative.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // BV5: Type validation and Stock Check (Crucial Inventory Logic)
        String upperType = type.toUpperCase();
        if (!upperType.equals("SALE") && !upperType.equals("PURCHASE")) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid transaction type. Must be SALE or PURCHASE.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (upperType.equals("SALE")) {
            if (quantity > product.getQuantity()) {
                JOptionPane.showMessageDialog(null, 
                    "❌ Insufficient stock for product " + productId + 
                    ". Available: " + product.getQuantity(), 
                    "Inventory Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Passed validations, proceed to DAO
        Transaction t = new Transaction();
        t.setProductId(productId);
        t.setQuantity(quantity);
        t.setType(upperType);
        t.setTotal(total);
        t.setDate(date); // Use the date provided by the frame (current timestamp)

        transactionDao.addTransaction(t);
        
        // NOTE: The DAO method (TransactionDao.addTransaction) must also handle the 
        // subsequent inventory adjustment (increment/decrement product quantity) 
        // for a complete solution. Assuming it does, or we must modify the DAO later.
    }

    /**
     * UPDATE: Update an existing transaction (Use with caution in real systems).
     */
    public void updateTransaction(int id, int productId, int quantity, String type, double total, Timestamp date) {
        
        // BV0: ID must be valid
        if (id <= 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid Transaction ID for update.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Re-use core ADD validations (BV1-BV5 checks on productId, quantity, total, type)
        // For a full system, UPDATING a transaction would require a complex rollback/re-apply
        // of inventory changes, which is complex. For this project scope, we perform basic checks.

        // BV1-4: Basic field checks
        if (productId <= 0 || quantity <= 0 || total < 0 || type == null || type.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ All fields except ID and Date must be valid (Quantity > 0, Total >= 0).", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String upperType = type.toUpperCase();
        if (!upperType.equals("SALE") && !upperType.equals("PURCHASE")) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid transaction type. Must be SALE or PURCHASE.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Passed simple validations, proceed to DAO
        Transaction t = new Transaction(id, productId, quantity, date, upperType, total);
        
        // NOTE: A production-ready UPDATE should handle the inventory change carefully!
        transactionDao.updateTransaction(t);
    }
    
    /**
     * DELETE: Delete a transaction.
     */
    public void deleteTransaction(int transactionId) {
        if (transactionId <= 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid Transaction ID for deletion.", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        transactionDao.deleteTransaction(transactionId);
        // NOTE: A production-ready DELETE should also revert the inventory change!
    }

    /**
     * READ: Get all transactions.
     */
    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }
    
    /**
     * READ: Get transaction by ID (optional).
     */
    public Transaction getTransactionById(int id) {
        if (id <= 0) {
            return null;
        }
        return transactionDao.getTransactionById(id);
    }
}