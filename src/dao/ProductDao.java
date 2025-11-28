package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Product;

public class ProductDao {

    // Add new product
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (name, category, quantity, price, supplier_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, product.getName());
            pst.setString(2, product.getCategory());
            pst.setInt(3, product.getQuantity());
            pst.setDouble(4, product.getPrice());
            pst.setInt(5, product.getSupplierId());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "✅ Product added successfully!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Failed to add product: " + e.getMessage());
        }
    }

    // Update existing product
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name=?, category=?, quantity=?, price=?, supplier_id=? WHERE product_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, product.getName());
            pst.setString(2, product.getCategory());
            pst.setInt(3, product.getQuantity());
            pst.setDouble(4, product.getPrice());
            pst.setInt(5, product.getSupplierId());
            pst.setInt(6, product.getProductId());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "✅ Product updated successfully!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Failed to update product: " + e.getMessage());
        }
    }

    // Delete product by ID
    public void deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, productId);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "🗑️ Product deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "⚠️ No product found with that ID!");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Failed to delete product: " + e.getMessage());
        }
    }

    // Retrieve all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setCategory(rs.getString("category"));
                p.setQuantity(rs.getInt("quantity"));
                p.setPrice(rs.getDouble("price"));
                p.setSupplierId(rs.getInt("supplier_id"));
                products.add(p);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "❌ Failed to retrieve products: " + e.getMessage());
        }

        return products;
    }
}
