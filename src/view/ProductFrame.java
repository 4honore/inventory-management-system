package view;

import controller.ProductController;
import model.Product;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ishimwe Honore
 */
public class ProductFrame extends JFrame {

    private final ProductController controller;
    private JTextField nameField, categoryField, qtyField, priceField, supplierField, idField;
    private final JTable table;
    private final DefaultTableModel model;

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
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Supplier ID:"));
        formPanel.add(supplierField);

        // ====== BUTTON PANEL ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton addBtn = new JButton("Add");
        JButton viewBtn = new JButton("View All");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // ====== TABLE PANEL ======
        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Qty", "Price", "Supplier ID"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Product List"));

        // ====== CONTENT PANEL ======
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== ADD MAIN PANELS TO FRAME ======
        add(formPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // ====== BUTTON ACTIONS ======
        addBtn.addActionListener(e -> {
            try {
                controller.addProduct(
                    nameField.getText(),
                    categoryField.getText(),
                    Integer.parseInt(qtyField.getText()),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(supplierField.getText())
                );
                clearFields();
                loadProducts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Invalid input! Quantity, Price, and Supplier ID must be numbers.");
            }
        });

        viewBtn.addActionListener(e -> loadProducts());

        updateBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Please select a product from the table to update!");
                    return;
                }
                
                controller.updateProduct(
                    Integer.parseInt(idField.getText()),
                    nameField.getText(),
                    categoryField.getText(),
                    Integer.parseInt(qtyField.getText()),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(supplierField.getText())
                );
                clearFields();
                loadProducts();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, " Invalid ID or number format!");
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, " Please select a product from the table to delete!");
                    return;
                }
                
                int id = Integer.parseInt(idField.getText());
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this product?", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteProduct(id);
                    clearFields();
                    loadProducts();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, " Invalid ID!");
            }
        });

        clearBtn.addActionListener(e -> clearFields());

        // ====== TABLE ROW CLICK EVENT (IMPORTANT FIX!) ======
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

    // ====== HELPER METHODS ======

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