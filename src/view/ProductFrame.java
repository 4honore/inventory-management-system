package view;

import controller.ProductController;
import model.Product;
import model.UserSession; // NEW IMPORT
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductFrame extends JFrame {

    private final ProductController controller;
    private JTextField nameField, categoryField, qtyField, priceField, supplierField, idField;
    private final JTable table;
    private final DefaultTableModel model;
    
    // Define CRUD buttons globally
    private JButton addButton, updateButton, deleteButton;

    // Define colors for consistency
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color DANGER_COLOR = new Color(192, 57, 43);

    public ProductFrame() {
        controller = new ProductController();

        // 1. Security Check: Block unauthorized access
        if (!UserSession.hasAnyRole("ADMIN", "MANAGER", "STAFF")) {
            JOptionPane.showMessageDialog(null, "You are not authorized to access the Products module.", "Permission Denied", JOptionPane.ERROR_MESSAGE);
            // Must safely dispose the frame on the EDT
            SwingUtilities.invokeLater(this::dispose); 
            return; // Prevents frame initialization
        }
        
        // Check if the user is authorized to perform modifications
        boolean canModify = UserSession.hasAnyRole("ADMIN", "MANAGER");

        setTitle("Inventory Management - Product Module (" + (canModify ? "Full Access" : "Read-Only") + ")");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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


        // ===== INPUT FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));

        idField = new JTextField();
        idField.setEditable(false);
        nameField = new JTextField();
        categoryField = new JTextField();
        qtyField = new JTextField();
        priceField = new JTextField();
        supplierField = new JTextField(); // Note: Should ideally be a JComboBox of suppliers

        formPanel.add(new JLabel("Product ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        formPanel.add(new JLabel("Quantity (Qty):"));
        formPanel.add(qtyField);
        formPanel.add(new JLabel("Price (RWF):"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Supplier ID:"));
        formPanel.add(supplierField);

        // ===== BUTTON PANEL (RBAC IMPLEMENTATION HERE) =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        
        addButton = createStyledButton("➕ Add Product", SUCCESS_COLOR);
        updateButton = createStyledButton("📝 Update Product", WARNING_COLOR);
        deleteButton = createStyledButton("🗑️ Delete Product", DANGER_COLOR);
        JButton clearButton = createStyledButton("🧹 Clear Fields", new Color(108, 117, 125)); // Non-CRUD action

        // --- RBAC: Enable/Disable/Hide CRUD buttons based on role ---
        if (canModify) {
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
        } else {
            // Read-Only users have all input fields non-editable for safety
            nameField.setEditable(false);
            categoryField.setEditable(false);
            qtyField.setEditable(false);
            priceField.setEditable(false);
            supplierField.setEditable(false);
        }
        
        buttonPanel.add(clearButton);
        
        JPanel controlsPanel = new JPanel(new BorderLayout());
        controlsPanel.add(formPanel, BorderLayout.NORTH);
        controlsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(controlsPanel, BorderLayout.NORTH);

        // ===== TABLE PANEL =====
        String[] columnNames = {"ID", "Name", "Category", "Quantity", "Price", "Supplier ID"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells non-editable
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        // ===== BUTTON LISTENERS (With robust input parsing) =====

        addButton.addActionListener(e -> {
            if (canModify) {
                try {
                    String name = nameField.getText();
                    String category = categoryField.getText();
                    // Handle potential NumberFormatExceptions
                    int quantity = Integer.parseInt(qtyField.getText().trim());
                    double price = Double.parseDouble(priceField.getText().trim());
                    int supplierId = Integer.parseInt(supplierField.getText().trim());
                    
                    // Controller handles business validation (e.g., negative quantity, empty name)
                    controller.addProduct(name, category, quantity, price, supplierId);
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "❌ Quantity, Price, and Supplier ID must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ An unexpected error occurred: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadProducts();
            clearFields();
        });

        updateButton.addActionListener(e -> {
            if (canModify) {
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

                    controller.updateProduct(id, name, category, quantity, price, supplierId);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "❌ Quantity, Price, and Supplier ID must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ An unexpected error occurred: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadProducts();
            clearFields();
        });

        deleteButton.addActionListener(e -> {
            if (canModify) {
                try {
                    if (idField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int id = Integer.parseInt(idField.getText());
                    controller.deleteProduct(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Internal Error: Invalid Product ID.", "System Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ An unexpected error occurred: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                }
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

        // Load data initially
        loadProducts();
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
        table.clearSelection();
    }
}