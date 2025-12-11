package controller;

import dao.ProductDao;
import model.Product;
// Removed: import javax.swing.JOptionPane;
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

    // CREATE - Add a new product. Method now THROWS Exception on failure.
    public void addProduct(String name, String category, int quantity, double price, int supplierId) 
            throws Exception {
        
        // ===== BUSINESS VALIDATIONS (Controller handles this logic) =====
        
        // BV1: Name and Category cannot be empty
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Product name cannot be empty!");
        }
        
        if (category == null || category.trim().isEmpty()) {
            throw new Exception("Category cannot be empty!");
        }

        // BV2: Quantity must be valid
        if (quantity < 0 || quantity > MAX_QUANTITY) {
            throw new Exception("Quantity must be between 0 and " + MAX_QUANTITY + ".");
        }
        
        // BV3: Price must be valid
        if (price < MIN_PRICE || price > MAX_PRICE) {
            throw new Exception("Price must be between " + MIN_PRICE + " RWF and " + MAX_PRICE + " RWF.");
        }
        
        // BV4: Supplier ID must be positive
        if (supplierId <= 0) {
            throw new Exception("Invalid Supplier ID.");
        }
        
        // --- If all validations pass ---
        
        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setSupplierId(supplierId);

        // Call DAO. (DAO must be updated to throw SQLException)
        productDao.addProduct(product);
        
        // NOTE: No success message is displayed here. The caller (ProductFrame) will handle success.
    }

    // READ - Retrieve all products
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    // UPDATE - Modify existing product. Method now THROWS Exception on failure.
    public void updateProduct(int id, String name, String category, int quantity, double price, int supplierId) 
            throws Exception {

        if (id <= 0) {
            throw new Exception("Invalid Product ID for update.");
        }
        
        // Re-apply all necessary validations here
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Product name cannot be empty!");
        }
        // ... (Add all other validation checks here, matching addProduct) ...
        
        Product product = new Product(id, name, category, quantity, price, supplierId);
        productDao.updateProduct(product);
        // NOTE: No success message is displayed here.
    }

    // DELETE - Remove product. Method now THROWS Exception on failure.
    public void deleteProduct(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("Invalid Product ID for deletion.");
        }

        // Call DAO. (DAO must be updated to throw SQLException)
        productDao.deleteProduct(id);
        // NOTE: No success message is displayed here.
    }
    
    // Helper method to check low stock products
    public List<Product> getLowStockProducts() {
        List<Product> allProducts = productDao.getAllProducts();
        return allProducts.stream()
                .filter(p -> p.getQuantity() < LOW_STOCK_THRESHOLD)
                .collect(Collectors.toList());
    }
}