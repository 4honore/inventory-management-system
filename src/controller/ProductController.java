package controller;

import dao.ProductDao;
import model.Product;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProductController {

    private final ProductDao productDao;
    
    // Business validation constants
    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final int MAX_QUANTITY = 10000;
    private static final double MAX_PRICE = 1000000.0;
    private static final double MIN_PRICE = 0.01;

    public ProductController() {
        productDao = new ProductDao();
    }

    // CREATE - Add a new product with comprehensive validations
    public void addProduct(String name, String category, int quantity, double price, int supplierId) {
        
        // ===== BUSINESS VALIDATIONS =====
        
        // BV1: Name and Category cannot be empty
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Product name cannot be empty!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (category == null || category.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Category cannot be empty!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // BV2: Quantity must be positive and within reasonable range
        if (quantity < 0) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Quantity cannot be negative!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // BV3: Quantity cannot exceed maximum limit
        if (quantity > MAX_QUANTITY) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Quantity cannot exceed " + MAX_QUANTITY + " units!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // BV4: Price must be within valid range
        if (price < MIN_PRICE) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Price must be at least " + MIN_PRICE + " RWF!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (price > MAX_PRICE) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Price cannot exceed " + MAX_PRICE + " RWF!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // BV5: Warning for low stock items
        if (quantity < LOW_STOCK_THRESHOLD) {
            int choice = JOptionPane.showConfirmDialog(null, 
                "⚠️ Warning: Quantity is below minimum stock threshold (" + LOW_STOCK_THRESHOLD + ").\n" +
                "Do you want to proceed?", 
                "Low Stock Warning", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }
        
        // ===== TECHNICAL VALIDATIONS =====
        
        // TV1: Name length validation (minimum 2, maximum 100 characters)
        if (name.trim().length() < 2) {
            JOptionPane.showMessageDialog(null, 
                "❌ Product name must be at least 2 characters long!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (name.trim().length() > 100) {
            JOptionPane.showMessageDialog(null, 
                "❌ Product name cannot exceed 100 characters!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TV2: Name format validation (no special characters except spaces and hyphens)
        if (!Pattern.matches("^[a-zA-Z0-9\\s\\-]+$", name.trim())) {
            JOptionPane.showMessageDialog(null, 
                "❌ Product name contains invalid characters!\n" +
                "Only letters, numbers, spaces, and hyphens are allowed.", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TV3: Category format validation
        if (!Pattern.matches("^[a-zA-Z\\s]+$", category.trim())) {
            JOptionPane.showMessageDialog(null, 
                "❌ Category must contain only letters and spaces!", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TV4: Supplier ID must be valid (positive integer)
        if (supplierId <= 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid Supplier ID! Must be a positive number.", 
                "Technical Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // TV5: Check if supplier exists (database constraint validation)
        // This would require a method to check supplier existence
        // For now, we'll add a note that this should be implemented in DAO
        
        // Create Product object
        Product product = new Product(0, name.trim(), category.trim(), quantity, price, supplierId);

        // Save to database using DAO
        try {
            productDao.addProduct(product);
            JOptionPane.showMessageDialog(null, 
                "✅ Product added successfully!\n" +
                "Name: " + name + "\n" +
                "Category: " + category + "\n" +
                "Quantity: " + quantity, 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Error adding product: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // READ - Retrieve all products
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    // UPDATE - Modify existing product with validations
    public void updateProduct(int id, String name, String category, int quantity, double price, int supplierId) {
        
        // Technical validation for ID
        if (id <= 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid Product ID!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Apply same validations as addProduct
        if (name == null || name.trim().isEmpty() || category == null || category.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Name and Category cannot be empty!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (quantity < 0 || quantity > MAX_QUANTITY) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Quantity must be between 0 and " + MAX_QUANTITY + "!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (price < MIN_PRICE || price > MAX_PRICE) {
            JOptionPane.showMessageDialog(null, 
                "⚠️ Price must be between " + MIN_PRICE + " and " + MAX_PRICE + " RWF!", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Product product = new Product(id, name.trim(), category.trim(), quantity, price, supplierId);
        
        try {
            productDao.updateProduct(product);
            JOptionPane.showMessageDialog(null, 
                "✅ Product updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Error updating product: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE - Remove product
    public void deleteProduct(int id) {
        if (id <= 0) {
            JOptionPane.showMessageDialog(null, 
                "❌ Invalid Product ID!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            productDao.deleteProduct(id);
            JOptionPane.showMessageDialog(null, 
                "✅ Product deleted successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Error deleting product: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to check low stock products
    public List<Product> getLowStockProducts() {
        List<Product> allProducts = productDao.getAllProducts();
        return allProducts.stream()
                .filter(p -> p.getQuantity() < LOW_STOCK_THRESHOLD)
                .collect(Collectors.toList());
    }
}