package view;

import controller.TransactionController;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class TransactionFrame extends JFrame {

    private final TransactionController controller;

    private JTextField idField, productIdField, quantityField, typeField, totalField;
    private JTable table;
    private DefaultTableModel model;

    public TransactionFrame() {
        controller = new TransactionController();

        setTitle("Transaction Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Transaction Details"));

        idField = new JTextField();
        productIdField = new JTextField();
        quantityField = new JTextField();
        typeField = new JTextField();
        totalField = new JTextField();

        formPanel.add(new JLabel("Transaction ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Product ID:"));
        formPanel.add(productIdField);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);
        formPanel.add(new JLabel("Type (Sale/Purchase):"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("Total Amount:"));
        formPanel.add(totalField);

        // ===== BUTTON PANEL =====
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

        // ===== TABLE PANEL =====
        model = new DefaultTableModel(
                new String[]{"ID", "Product ID", "Qty", "Type", "Date", "Total"}, 0
        );
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Transaction List"));

        // ===== MAIN PANEL =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(formPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // ====== BUTTON ACTIONS ======

        // ADD
        addBtn.addActionListener(e -> {
            try {
                int productId = Integer.parseInt(productIdField.getText());
                int qty = Integer.parseInt(quantityField.getText());
                double total = Double.parseDouble(totalField.getText());
                String type = typeField.getText();
                Timestamp date = new Timestamp(System.currentTimeMillis());

                controller.addTransaction(productId, qty, type, total, date);
                clearFields();
                loadTransactions();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Enter valid values for Product ID, Qty, and Total.");
            }
        });

        // VIEW ALL
        viewBtn.addActionListener(e -> loadTransactions());

        // UPDATE
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int productId = Integer.parseInt(productIdField.getText());
                int qty = Integer.parseInt(quantityField.getText());
                double total = Double.parseDouble(totalField.getText());
                String type = typeField.getText();
                Timestamp date = new Timestamp(System.currentTimeMillis());

                controller.updateTransaction(id, productId, qty, type, total, date);
                clearFields();
                loadTransactions();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Invalid input! Make sure all fields are filled.");
            }
        });

        // DELETE
        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                controller.deleteTransaction(id);
                clearFields();
                loadTransactions();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Invalid Transaction ID!");
            }
        });

        // CLEAR
        clearBtn.addActionListener(e -> clearFields());

        // TABLE ROW CLICK — fill fields
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    productIdField.setText(model.getValueAt(row, 1).toString());
                    quantityField.setText(model.getValueAt(row, 2).toString());
                    typeField.setText(model.getValueAt(row, 3).toString());
                    totalField.setText(model.getValueAt(row, 5).toString());
                }
            }
        });

        // Load data initially
        loadTransactions();
        setVisible(true);
    }

    // ===== HELPER METHODS =====
    private void loadTransactions() {
        model.setRowCount(0);
        List<Transaction> list = controller.getAllTransactions();
        for (Transaction t : list) {
            model.addRow(new Object[]{
                    t.getTransactionId(),
                    t.getProductId(),
                    t.getQuantity(),
                    t.getType(),
                    t.getDate(),
                    t.getTotal()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        productIdField.setText("");
        quantityField.setText("");
        typeField.setText("");
        totalField.setText("");
    }
}
