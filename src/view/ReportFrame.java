package view;

import dao.ProductDao;
import dao.SupplierDao;
import dao.TransactionDao;
import model.Product;
import model.Supplier;
import model.Transaction;
import model.UserSession; // NEW IMPORT for Security

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Report Frame - Generate various reports and analytics
 */
public class ReportFrame extends JFrame {
    
    private final ProductDao productDao = new ProductDao();
    private final SupplierDao supplierDao = new SupplierDao();
    private final TransactionDao transactionDao = new TransactionDao();
    
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextArea summaryArea;
    
    // Define main colors for consistency
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    
    public ReportFrame() {
        
        // 1. Security Check: Block unauthorized access
        if (!UserSession.hasAnyRole("ADMIN", "MANAGER")) {
            JOptionPane.showMessageDialog(null, "You are not authorized to access the Reports module. Access is restricted to ADMIN and MANAGER roles.", "Permission Denied", JOptionPane.ERROR_MESSAGE);
            // Must safely dispose the frame on the EDT
            SwingUtilities.invokeLater(this::dispose); 
            return; // Prevents frame initialization
        }
        
        setTitle("Inventory Management - Reports & Analytics");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // ===== HEADER =====
        JLabel header = new JLabel("System Reports & Analytics", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(header);
        add(headerPanel, BorderLayout.NORTH);
        
        // ===== CENTER PANEL: Summary and Table =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Divide space roughly in half
        
        // --- Summary Panel (Top) ---
        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        summaryArea.setBorder(BorderFactory.createTitledBorder("Overall Summary"));
        JScrollPane summaryScrollPane = new JScrollPane(summaryArea);
        
        // --- Table Panel (Bottom) ---
        String[] columnNames = {"Metric", "Value"};
        tableModel = new DefaultTableModel(columnNames, 0);
        reportTable = new JTable(tableModel);
        reportTable.setBackground(Color.LIGHT_GRAY);
        JScrollPane tableScrollPane = new JScrollPane(reportTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Key Financial Metrics"));
        
        splitPane.setTopComponent(summaryScrollPane);
        splitPane.setBottomComponent(tableScrollPane);
        
        add(splitPane, BorderLayout.CENTER);
        
        // Load data on startup
        generateReport();
        
        setVisible(true);
    }
    
    private void generateReport() {
        List<Product> products = productDao.getAllProducts();
        List<Supplier> suppliers = supplierDao.getAllSupplier();
        List<Transaction> transactions = transactionDao.getAllTransactions();
        
        // --- Financial Metrics Calculation ---
        double totalSales = 0.0;
        double totalPurchases = 0.0;
        int salesCount = 0;
        int purchaseCount = 0;
        
        for (Transaction t : transactions) {
            if (t.getType().equalsIgnoreCase("SALE")) {
                totalSales += t.getTotal();
                salesCount++;
            } else if (t.getType().equalsIgnoreCase("PURCHASE")) {
                totalPurchases += t.getTotal();
                purchaseCount++;
            }
        }
        double profit = totalSales - totalPurchases;
        
        // --- Inventory Metrics Calculation ---
        int totalProducts = products.size();
        double totalInventoryValue = 0.0;
        int lowStockCount = 0;
        int lowStockThreshold = 10; // Assuming a low stock threshold of 10 units
        
        for (Product p : products) {
            totalInventoryValue += (p.getQuantity() * p.getPrice());
            if (p.getQuantity() < lowStockThreshold) {
                lowStockCount++;
            }
        }
        int totalSuppliers = suppliers.size();
        
        // ===== 2. Populate Table (Key Financial Metrics) =====
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"Total Sales", String.format("%,.2f RWF", totalSales)});
        tableModel.addRow(new Object[]{"Total Purchases", String.format("%,.2f RWF", totalPurchases)});
        tableModel.addRow(new Object[]{"Net Profit", String.format("%,.2f RWF", profit)});
        tableModel.addRow(new Object[]{"Total Inventory Value", String.format("%,.2f RWF", totalInventoryValue)});
        tableModel.addRow(new Object[]{"Number of Low Stock Items", lowStockCount});

        // ===== 3. Populate Summary Area =====
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "         INVENTORY MANAGEMENT SYSTEM              \n" +
            "              OVERALL SUMMARY REPORT              \n" +
            "══════════════════════════════════════════════════\n\n" +
            "INVENTORY STATUS:\n" +
            "  • Total Products:        " + totalProducts + "\n" +
            "  • Inventory Value:       " + String.format("%,.2f RWF", totalInventoryValue) + "\n" +
            "  • Low Stock Items:       " + lowStockCount + " (Quantity < " + lowStockThreshold + ")\n\n" +
            "BUSINESS PARTNERS:\n" +
            "  • Total Suppliers:       " + totalSuppliers + "\n\n" +
            "FINANCIAL SUMMARY:\n" +
            "  • Total Sales:           " + String.format("%,.2f RWF", totalSales) + "\n" +
            "  • Total Purchases:       " + String.format("%,.2f RWF", totalPurchases) + "\n" +
            "  • Net Profit:            " + String.format("%,.2f RWF", profit) + "\n\n" +
            "TRANSACTION ACTIVITY:\n" +
            "  • Total Transactions:    " + transactions.size() + "\n" +
            "  • Sales Transactions:    " + salesCount + "\n" +
            "  • Purchase Transactions: " + purchaseCount + "\n"
        );
    }
}