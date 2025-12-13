package view;

import controller.ProductController;
import model.Product;
import model.UserSession; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Product Management Frame - Fully secured and structurally sound.
 */
public class ProductFrame extends JFrame {

    private final ProductController controller;

    // FIX 1: Initialize ALL fields to null to prevent "might not have been initialized" error
    private JTextField idField = null;
    private JTextField nameField = null;
    private JTextField categoryField = null;
    private JTextField qtyField = null; // Corrected field name (was quantityField/qtyField inconsistency)
    private JTextField priceField = null;
    private JTextField supplierField = null; // Corrected field name (was supplierIdField/supplierField inconsistency)

    private JTable table = null;
    private DefaultTableModel model = null;
    private JButton addButton = null;
    private JButton updateButton = null;
    private JButton deleteButton = null;
    
    // Define colors for consistency
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(192, 57, 43);

    public ProductFrame() {
        controller = new ProductController();

        // 1. Security Check
        if (UserSession.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(null, "You must be logged in to access the Products module.", "Permission Denied", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.invokeLater(this::dispose);
            return; // Prevents frame initialization
        }

        setTitle("Inventory Management - Product Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== HEADER =====
        JLabel header = new JLabel("Product Management", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(header);
        add(headerPanel, BorderLayout.NORTH);

        // ===== FORM PANEL (Contains Fields) =====
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        // Initialize components
        idField = new JTextField();
        idField.setEditable(false);
        nameField = new JTextField();
        categoryField = new JTextField();
        qtyField = new JTextField(); 
        priceField = new JTextField();
        supplierField = new JTextField();

        // Add labels and fields
        formPanel.add(new JLabel("Product ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Quantity (Stock):"));
        formPanel.add(qtyField); 
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Supplier ID:"));
        formPanel.add(supplierField); 

        // Quantity and Supplier ID fields are read-only for integrity
        qtyField.setEditable(false);
        supplierField.setEditable(false);


        // ===== BUTTON PANEL (Contains CRUD controls) =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        addButton = createStyledButton("➕ Add Product", SUCCESS_COLOR);
        updateButton = createStyledButton("📝 Update Product", WARNING_COLOR);
        deleteButton = createStyledButton("🗑️ Delete Product", DANGER_COLOR);
        JButton clearButton = createStyledButton("🧹 Clear Fields", new Color(108, 117, 125));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // 2. RBAC Implementation: Disable editing features for STAFF
        if (UserSession.hasRole("STAFF")) {
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            JOptionPane.showMessageDialog(this, 
                "You are logged in as STAFF. Editing functionality is disabled.", 
                "View Only Mode", JOptionPane.INFORMATION_MESSAGE);
        }

        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.add(formPanel, BorderLayout.NORTH);
        controlsPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(controlsPanel, BorderLayout.WEST);

        // ===== TABLE PANEL =====
        String[] columnNames = {"ID", "Name", "Category", "Quantity", "Price", "Supplier ID"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // ===== BUTTON LISTENERS (FIX 2: Add try-catch blocks for unreported exceptions) =====

        addButton.addActionListener(e -> {
            if (UserSession.hasRole("STAFF")) return; 
            try {
                String name = nameField.getText();
                String category = categoryField.getText();
                int quantity = 0; 
                double price = Double.parseDouble(priceField.getText().trim());
                int supplierId = Integer.parseInt(supplierField.getText().trim()); 

                try {
                    // FIX: Catch the potential Exception from the controller
                    controller.addProduct(name, category, quantity, price, supplierId);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Database Error during Add: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return; 
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Price or Supplier ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
            loadProducts();
            clearFields();
        });

        updateButton.addActionListener(e -> {
            if (UserSession.hasRole("STAFF")) return;
            try {
                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select a product to update.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                String category = categoryField.getText();
                int quantity = Integer.parseInt(qtyField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                int supplierId = Integer.parseInt(supplierField.getText().trim());

                try {
                    // FIX: Catch the potential Exception from the controller
                    controller.updateProduct(id, name, category, quantity, price, supplierId);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Database Error during Update: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return; 
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID, Quantity, Price, or Supplier ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
            loadProducts();
            clearFields();
        });

        deleteButton.addActionListener(e -> {
            if (UserSession.hasRole("STAFF")) return;
            try {
                if (idField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = Integer.parseInt(idField.getText());

                try {
                    // FIX: Catch the potential Exception from the controller
                    controller.deleteProduct(id);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Database Error during Delete: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return; 
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Product ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
            loadProducts();
            clearFields();
        });

        clearButton.addActionListener(e -> clearFields());

        // ===== TABLE SELECTION LISTENER =====
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
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

        // Load data on startup
        loadProducts();

        // Final visibility call
        setVisible(true);
    }

    // ===== Helper Methods =====

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void loadProducts() {
        model.setRowCount(0); // Clear existing rows
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
        table.clearSelection();
    }
}