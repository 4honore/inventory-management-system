package view;

import controller.SupplierController;
import model.Supplier;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupplierFrame extends JFrame {

    private final SupplierController controller;
    private JTextField idField, nameField, contactField, addressField, emailField;
    private JTable table;
    private DefaultTableModel model;

    public SupplierFrame() {
        controller = new SupplierController();

        setTitle("Supplier Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));

        idField = new JTextField();
        idField.setEditable(false); // ID should not be manually edited
        nameField = new JTextField();
        contactField = new JTextField();
        addressField = new JTextField();
        emailField = new JTextField();

        formPanel.add(new JLabel("Supplier ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

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
        model = new DefaultTableModel(new String[]{"ID", "Name", "Contact", "Address", "Email"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Supplier List"));

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(buttonPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== ADD PANELS =====
        add(formPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // ===== BUTTON ACTIONS =====
        addBtn.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Supplier name cannot be empty!");
                return;
            }
            
            controller.addSupplier(
                    nameField.getText(),
                    contactField.getText(),
                    addressField.getText(),
                    emailField.getText()
            );
            clearFields();
            loadSuppliers();
        });

        viewBtn.addActionListener(e -> loadSuppliers());

        updateBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Please select a supplier from the table to update!");
                    return;
                }
                
                if (nameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Supplier name cannot be empty!");
                    return;
                }
                
                int id = Integer.parseInt(idField.getText());
                controller.updateSupplier(
                        id,
                        nameField.getText(),
                        contactField.getText(),
                        addressField.getText(),
                        emailField.getText()
                );
                clearFields();
                loadSuppliers();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid Supplier ID");
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                if (idField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "⚠️ Please select a supplier from the table to delete!");
                    return;
                }
                
                int id = Integer.parseInt(idField.getText());
                
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this supplier?\nThis may affect associated products!", 
                    "Confirm Delete", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteSupplier(id);
                    clearFields();
                    loadSuppliers();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid Supplier ID");
            }
        });

        clearBtn.addActionListener(e -> clearFields());

        // ===== TABLE ROW CLICK EVENT =====
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    idField.setText(model.getValueAt(selectedRow, 0).toString());
                    nameField.setText(model.getValueAt(selectedRow, 1).toString());
                    contactField.setText(model.getValueAt(selectedRow, 2).toString());
                    addressField.setText(model.getValueAt(selectedRow, 3).toString());
                    emailField.setText(model.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        // Load suppliers initially
        loadSuppliers();

        setVisible(true);
    }

    // ===== Helper Methods =====
    private void loadSuppliers() {
        model.setRowCount(0);
        List<Supplier> suppliers = controller.getAllSupplier();
        for (Supplier s : suppliers) {
            model.addRow(new Object[]{
                    s.getSupplierId(),
                    s.getName(),
                    s.getContact(),
                    s.getAddress(),
                    s.getEmail()
            });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        contactField.setText("");
        addressField.setText("");
        emailField.setText("");
    }
}