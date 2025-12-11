package view;

import controller.ProductController;
import model.Product;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * ProductFrame - The View component for Product Management.
 * * NOTE: The action listeners below now handle all UI messages (JOptionPane)
 * based on the success or failure (Exception) returned by the Controller.
 */
public class ProductFrame extends JFrame {

    private final ProductController controller;
    private JTextField nameField, categoryField, qtyField, priceField, supplierField, idField;
    private final JTable table;
    private final DefaultTableModel model;
    
    // Assuming these buttons are defined as fields in your original code
    private JButton addButton, updateButton, deleteButton, clearButton;

    public ProductFrame() {
        controller = new ProductController();

        setTitle("Inventory Management - Product Module");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));


        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        idField = new JTextField();
        idField.setEditable(false);
        nameField = new JTextField();
        categoryField = new JTextField();
        qtyField = new JTextField();
        priceField = new JTextField();
        supplierField = new JTextField();

        formPanel.add(new JLabel("Product ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(qtyField);
        formPanel.add(new JLabel("Price (RWF):"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Supplier ID:"));
        formPanel.add(supplierField);

        // ===== BUTTON PANEL (Assuming buttons are initialized here) =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButton = new JButton("Add Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");
        clearButton = new JButton("Clear Fields");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        // ===== TABLE SETUP =====
        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Quantity", "Price", "Supplier ID"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the frame
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // =================================================================
        // ===== REFACTORED BUTTON ACTIONS - UI LOGIC NOW HERE IN THE VIEW =====
        // =================================================================

        // ADD Button Action Listener
        addButton.addActionListener(e -> {
            try {
                // Input conversion (local validation)
                String name = nameField.getText();
                String category = categoryField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                double price = Double.parseDouble(priceField.getText());
                int supplierId = Integer.parseInt(supplierField.getText());

                // Call the Controller (Controller throws Exception on validation/DB failure)
                controller.addProduct(name, category, qty, price, supplierId);
                
                // SUCCESS HANDLING (The View displays the result!)
                JOptionPane.showMessageDialog(this, 
                    "✅ Product added successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                clearFields();
                loadProducts(); // Refresh the table
                
            } catch (NumberFormatException ex) {
                // CATCH 1: Handles invalid number input
                JOptionPane.showMessageDialog(this, 
                    "❌ Please enter valid numbers for Quantity, Price, and Supplier ID.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                // CATCH 2: Handles Business Logic (Validation) or Database Error
                JOptionPane.showMessageDialog(this, 
                    "❌ Operation Failed: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // UPDATE Button Action Listener
        updateButton.addActionListener(e -> {
            try {
                // Basic check for ID field
                if (idField.getText().trim().isEmpty()) {
                    throw new Exception("Please select a product to update.");
                }
                
                // Input conversion
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String category = categoryField.getText();
                int qty = Integer.parseInt(qtyField.getText());
                double price = Double.parseDouble(priceField.getText());
                int supplierId = Integer.parseInt(supplierField.getText());

                controller.updateProduct(id, name, category, qty, price, supplierId);
                
                // SUCCESS HANDLING
                JOptionPane.showMessageDialog(this, 
                    "✅ Product updated successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                clearFields();
                loadProducts(); 
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Invalid numeric input.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Update Failed: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // DELETE Button Action Listener
        deleteButton.addActionListener(e -> {
            try {
                // Basic check for ID field
                if (idField.getText().trim().isEmpty()) {
                    throw new Exception("Please select a product to delete.");
                }
                int id = Integer.parseInt(idField.getText());
                
                // Confirmation dialog (View logic)
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete Product ID: " + id + "?", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteProduct(id);
                    
                    // SUCCESS HANDLING
                    JOptionPane.showMessageDialog(this, 
                        "✅ Product deleted successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    clearFields();
                    loadProducts(); 
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Invalid Product ID format.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "❌ Deletion Failed: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // CLEAR Button Action Listener
        clearButton.addActionListener(e -> clearFields());


        // Table row click listener (remains the same)
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    idField.setText(model.getValueAt(selectedRow, 0).toString());
                    nameField.setText(model.getValueAt(selectedRow, 1).toString());
                    categoryField.setText(model.getValueAt(selectedRow, 2).toString());
                    qtyField.setText(model.getValueAt(selectedRow, 3).toString());
                    priceField.setText(model.getValueAt(selectedRow, 4).toString());
                    supplierField.setText(model.getValueAt(selectedRow, 5).toString());
                }
            }
        });

        // Load data initially
        loadProducts();
        setVisible(true);
    }

    // ====== HELPER METHODS (Should remain the same) ======\r\n

    private void loadProducts() {
        model.setRowCount(0); // clear existing rows
        List<Product> products = controller.getAllProducts();
        for (Product p : products) {
            model.addRow(new Object[]{
                p.getProductId(),
                p.getName(),
                p.getCategory(),
                p.getQuantity(),
                p.getPrice(),
                p.getSupplierId()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        qtyField.setText("");
        priceField.setText("");
        supplierField.setText("");
    }
}