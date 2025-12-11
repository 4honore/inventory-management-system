package controller;

import dao.ProductDao;
import dao.TransactionDao;
import model.Product;
import model.Transaction;

import javax.swing.JOptionPane;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Robust Transaction Controller with product-stock business logic and security.
 */
public class TransactionController {

    private final TransactionDao transactionDao;
    private final ProductDao productDao; 

    public TransactionController() {
        this.transactionDao = new TransactionDao();
        this.productDao = new ProductDao(); 
    }

    // =========================================================================
    // ADD TRANSACTION (CREATE) - NEW SIGNATURE (No date argument)
    // =========================================================================
    public void addTransaction(int productId, int quantity, String type, double total) {
        
        if (productId <= 0 || quantity <= 0 || total <= 0) {
            JOptionPane.showMessageDialog(null, "⚠️ Invalid Product ID, Quantity, or Total.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Product product = productDao.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(null, "❌ Product with ID " + productId + " does not exist.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String transactionType = type.toUpperCase().trim();
        int newQuantity = product.getQuantity();
        
        // Stock Calculation Logic
        if (transactionType.equals("SALE")) {
            if (quantity > product.getQuantity()) {
                JOptionPane.showMessageDialog(null, "❌ Insufficient stock! Available: " + product.getQuantity() + " units.", "Stock Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            newQuantity -= quantity; // Decrement stock for a SALE
        } else if (transactionType.equals("PURCHASE")) {
            newQuantity += quantity; // Increment stock for a PURCHASE
        } else {
            JOptionPane.showMessageDialog(null, "⚠️ Invalid transaction type. Must be SALE or PURCHASE.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Create Transaction Object (Date handled internally)
        Transaction t = new Transaction();
        t.setProductId(productId);
        t.setQuantity(quantity);
        t.setType(transactionType);
        t.setTotal(total);
        t.setDate(Timestamp.from(Instant.now())); 

        try {
            // 2. Persist the Transaction
            transactionDao.addTransaction(t);
            
            // 3. Update the Product Stock
            productDao.updateProductQuantity(productId, newQuantity);
            
            JOptionPane.showMessageDialog(null, "✅ Transaction recorded and stock updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ An error occurred during transaction processing: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // UPDATE TRANSACTION - NEW SIGNATURE (No date argument)
    // NOTE: This assumes transaction update in the DAO does not affect the date.
    // =========================================================================
    public void updateTransaction(int id, int productId, int qty, String type, double total) {
        if (id <= 0) {
            JOptionPane.showMessageDialog(null, "❌ Invalid Transaction ID for update.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // NOTE: In a professional system, updating a transaction that changes 
        // quantity requires complex stock reversal/re-calculation logic. 
        // For simplicity, this update only changes the transaction record, 
        // but does NOT affect the product stock quantity.
        Transaction t = new Transaction();
        t.setTransactionId(id);
        t.setProductId(productId);
        t.setQuantity(qty);
        t.setType(type);
        t.setTotal(total);
        // Date is not set here; the DAO should use the existing date.
        
        transactionDao.updateTransaction(t);
        JOptionPane.showMessageDialog(null, "✅ Transaction updated successfully (Stock NOT recalculated)!","Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================================
    // OTHER METHODS
    // =========================================================================

    public List<Transaction> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    public void deleteTransaction(int transactionId) {
        // Deletion in this simplified version does NOT reverse stock
        transactionDao.deleteTransaction(transactionId);
        JOptionPane.showMessageDialog(null, "✅ Transaction deleted successfully!","Success", JOptionPane.INFORMATION_MESSAGE);
    }
}