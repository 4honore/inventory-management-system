package view;

import dao.ProductDao;
import dao.SupplierDao;
import dao.TransactionDao;
import model.Product;
import model.Supplier;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Report Frame - Generate various reports and analytics
 * @author Ishimwe Honore
 */
public class ReportFrame extends JFrame {
    
    private final ProductDao productDao = new ProductDao();
    private final SupplierDao supplierDao = new SupplierDao();
    private final TransactionDao transactionDao = new TransactionDao();
    
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextArea summaryArea;
    
    public ReportFrame() {
        
        setTitle("Inventory Management - Reports & Analytics");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // ===== HEADER =====
        JLabel header = new JLabel("System Reports & Analytics", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(new Color(40, 40, 40));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);
        
        // ===== LEFT PANEL - Report Types =====
        JPanel leftPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Report Types"));
        leftPanel.setPreferredSize(new Dimension(200, 0));
        
        JButton allProductsBtn = new JButton("All Products");
        JButton lowStockBtn = new JButton("Low Stock Items");
        JButton allSuppliersBtn = new JButton("All Suppliers");
        JButton allTransactionsBtn = new JButton("All Transactions");
        JButton salesReportBtn = new JButton("Sales Report");
        JButton purchaseReportBtn = new JButton("Purchase Report");
        JButton inventoryValueBtn = new JButton("Inventory Value");
        JButton summaryBtn = new JButton("Overall Summary");
        
        // Style buttons
        styleButton(allProductsBtn, new Color(70, 130, 180));
        styleButton(lowStockBtn, new Color(220, 20, 60));
        styleButton(allSuppliersBtn, new Color(60, 179, 113));
        styleButton(allTransactionsBtn, new Color(255, 165, 0));
        styleButton(salesReportBtn, new Color(46, 139, 87));
        styleButton(purchaseReportBtn, new Color(138, 43, 226));
        styleButton(inventoryValueBtn, new Color(184, 134, 11));
        styleButton(summaryBtn, new Color(25, 25, 112));
        
        leftPanel.add(allProductsBtn);
        leftPanel.add(lowStockBtn);
        leftPanel.add(allSuppliersBtn);
        leftPanel.add(allTransactionsBtn);
        leftPanel.add(salesReportBtn);
        leftPanel.add(purchaseReportBtn);
        leftPanel.add(inventoryValueBtn);
        leftPanel.add(summaryBtn);
        
        // ===== CENTER PANEL - Report Display =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table for detailed reports
        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        reportTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane tableScroll = new JScrollPane(reportTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Report Details"));
        
        // Summary area
        summaryArea = new JTextArea(5, 30);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryArea.setBackground(new Color(240, 248, 255));
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setBorder(BorderFactory.createTitledBorder("Summary"));
        
        // Split pane for table and summary
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, summaryScroll);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.7);
        
        centerPanel.add(splitPane, BorderLayout.CENTER);
        
        // ===== BUTTON ACTIONS =====
        
        allProductsBtn.addActionListener(e -> showAllProductsReport());
        lowStockBtn.addActionListener(e -> showLowStockReport());
        allSuppliersBtn.addActionListener(e -> showAllSuppliersReport());
        allTransactionsBtn.addActionListener(e -> showAllTransactionsReport());
        salesReportBtn.addActionListener(e -> showSalesReport());
        purchaseReportBtn.addActionListener(e -> showPurchaseReport());
        inventoryValueBtn.addActionListener(e -> showInventoryValueReport());
        summaryBtn.addActionListener(e -> showOverallSummary());
        
        // ===== ADD PANELS =====
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        
        // Show initial report
        showOverallSummary();
        
        setVisible(true);
    }
    
    // ===== HELPER METHOD FOR BUTTON STYLING =====
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    // ===== REPORT METHODS =====
    
    private void showAllProductsReport() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price (RWF)");
        tableModel.addColumn("Supplier ID");
        
        List<Product> products = productDao.getAllProducts();
        
        double totalValue = 0;
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getProductId(),
                p.getName(),
                p.getCategory(),
                p.getQuantity(),
                String.format("%.2f", p.getPrice()),
                p.getSupplierId()
            });
            totalValue += p.getPrice() * p.getQuantity();
        }
        
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "               ALL PRODUCTS REPORT                \n" +
            "══════════════════════════════════════════════════\n" +
            "Total Products:        " + products.size() + "\n" +
            "Total Inventory Value: " + String.format("%.2f RWF", totalValue) + "\n" +
            "══════════════════════════════════════════════════"
        );
    }
    
    private void showLowStockReport() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price (RWF)");
        tableModel.addColumn("Status");
        
        List<Product> products = productDao.getAllProducts();
        int lowStockCount = 0;
        
        for (Product p : products) {
            if (p.getQuantity() < 10) {
                String status = p.getQuantity() == 0 ? "OUT OF STOCK" : "LOW STOCK";
                tableModel.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    p.getQuantity(),
                    String.format("%.2f", p.getPrice()),
                    status
                });
                lowStockCount++;
            }
        }
        
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "              LOW STOCK ALERT REPORT              \n" +
            "══════════════════════════════════════════════════\n" +
            "Total Low Stock Items: " + lowStockCount + "\n" +
            "Threshold Level:       10 units\n" +
            "⚠️  URGENT: Reorder these items immediately!\n" +
            "══════════════════════════════════════════════════"
        );
    }
    
    private void showAllSuppliersReport() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        tableModel.addColumn("Supplier ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Contact");
        tableModel.addColumn("Address");
        tableModel.addColumn("Email");
        
        List<Supplier> suppliers = supplierDao.getAllSupplier();
        
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{
                s.getSupplierId(),
                s.getName(),
                s.getContact(),
                s.getAddress(),
                s.getEmail()
            });
        }
        
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "              ALL SUPPLIERS REPORT                \n" +
            "══════════════════════════════════════════════════\n" +
            "Total Suppliers: " + suppliers.size() + "\n" +
            "══════════════════════════════════════════════════"
        );
    }
    
    private void showAllTransactionsReport() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        tableModel.addColumn("Transaction ID");
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Type");
        tableModel.addColumn("Date");
        tableModel.addColumn("Total (RWF)");
        
        List<Transaction> transactions = transactionDao.getAllTransactions();
        
        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                t.getTransactionId(),
                t.getProductId(),
                t.getQuantity(),
                t.getType(),
                t.getDate(),
                String.format("%.2f", t.getTotal())
            });
        }
        
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "           ALL TRANSACTIONS REPORT                \n" +
            "══════════════════════════════════════════════════\n" +
            "Total Transactions: " + transactions.size() + "\n" +
            "══════════════════════════════════════════════════"
        );
    }
    
    private void showSalesReport() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        tableModel.addColumn("Transaction ID");
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Date");
        tableModel.addColumn("Total (RWF)");
        
        List<Transaction> transactions = transactionDao.getAllTransactions();
        
        double totalSales = 0;
        int salesCount = 0;
        
        for (Transaction t : transactions) {
            if (t.getType().equalsIgnoreCase("SALE")) {
                tableModel.addRow(new Object[]{
                    t.getTransactionId(),
                    t.getProductId(),
                    t.getQuantity(),
                    t.getDate(),
                    String.format("%.2f", t.getTotal())
                });
                totalSales += t.getTotal();
                salesCount++;
            }
        }
        
        double avgSale = salesCount > 0 ? totalSales / salesCount : 0;
        
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "                  SALES REPORT                    \n" +
            "══════════════════════════════════════════════════\n" +
            "Total Sales Count:    " + salesCount + "\n" +
            "Total Sales Revenue:  " + String.format("%.2f RWF", totalSales) + "\n" +
            "Average Sale Value:   " + String.format("%.2f RWF", avgSale) + "\n" +
            "══════════════════════════════════════════════════"
        );
    }
    
    private void showPurchaseReport() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        tableModel.addColumn("Transaction ID");
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Date");
        tableModel.addColumn("Total (RWF)");
        
        List<Transaction> transactions = transactionDao.getAllTransactions();
        
        double totalPurchases = 0;
        int purchaseCount = 0;
        
        for (Transaction t : transactions) {
            if (t.getType().equalsIgnoreCase("PURCHASE")) {
                tableModel.addRow(new Object[]{
                    t.getTransactionId(),
                    t.getProductId(),
                    t.getQuantity(),
                    t.getDate(),
                    String.format("%.2f", t.getTotal())
                });
                totalPurchases += t.getTotal();
                purchaseCount++;
            }
        }
        
        double avgPurchase = purchaseCount > 0 ? totalPurchases / purchaseCount : 0;
        
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "                PURCHASE REPORT                   \n" +
            "══════════════════════════════════════════════════\n" +
            "Total Purchases Count: " + purchaseCount + "\n" +
            "Total Purchase Cost:   " + String.format("%.2f RWF", totalPurchases) + "\n" +
            "Average Purchase:      " + String.format("%.2f RWF", avgPurchase) + "\n" +
            "══════════════════════════════════════════════════"
        );
    }
    
    private void showInventoryValueReport() {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Unit Price");
        tableModel.addColumn("Total Value (RWF)");
        
        List<Product> products = productDao.getAllProducts();
        
        double totalInventoryValue = 0;
        
        for (Product p : products) {
            double productValue = p.getPrice() * p.getQuantity();
            tableModel.addRow(new Object[]{
                p.getProductId(),
                p.getName(),
                p.getQuantity(),
                String.format("%.2f", p.getPrice()),
                String.format("%.2f", productValue)
            });
            totalInventoryValue += productValue;
        }
        
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "            INVENTORY VALUE REPORT                \n" +
            "══════════════════════════════════════════════════\n" +
            "Total Products:        " + products.size() + "\n" +
            "Total Inventory Value: " + String.format("%.2f RWF", totalInventoryValue) + "\n" +
            "══════════════════════════════════════════════════"
        );
    }
    
    private void showOverallSummary() {
        // Clear table for summary view
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        
        tableModel.addColumn("Metric");
        tableModel.addColumn("Value");
        
        // Gather data
        List<Product> products = productDao.getAllProducts();
        List<Supplier> suppliers = supplierDao.getAllSupplier();
        List<Transaction> transactions = transactionDao.getAllTransactions();
        
        int totalProducts = products.size();
        int totalSuppliers = suppliers.size();
        int lowStockCount = 0;
        double totalInventoryValue = 0;
        double totalSales = 0;
        double totalPurchases = 0;
        int salesCount = 0;
        int purchaseCount = 0;
        
        for (Product p : products) {
            if (p.getQuantity() < 10) lowStockCount++;
            totalInventoryValue += p.getPrice() * p.getQuantity();
        }
        
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
        
        // Add rows to table
        tableModel.addRow(new Object[]{"Total Products", totalProducts});
        tableModel.addRow(new Object[]{"Total Suppliers", totalSuppliers});
        tableModel.addRow(new Object[]{"Low Stock Items", lowStockCount});
        tableModel.addRow(new Object[]{"Total Transactions", transactions.size()});
        tableModel.addRow(new Object[]{"Sales Count", salesCount});
        tableModel.addRow(new Object[]{"Purchase Count", purchaseCount});
        tableModel.addRow(new Object[]{"Inventory Value", String.format("%.2f RWF", totalInventoryValue)});
        tableModel.addRow(new Object[]{"Total Sales", String.format("%.2f RWF", totalSales)});
        tableModel.addRow(new Object[]{"Total Purchases", String.format("%.2f RWF", totalPurchases)});
        tableModel.addRow(new Object[]{"Net Profit", String.format("%.2f RWF", profit)});
        
        summaryArea.setText(
            "══════════════════════════════════════════════════\n" +
            "         INVENTORY MANAGEMENT SYSTEM              \n" +
            "              OVERALL SUMMARY REPORT              \n" +
            "══════════════════════════════════════════════════\n\n" +
            "INVENTORY STATUS:\n" +
            "  • Total Products:        " + totalProducts + "\n" +
            "  • Inventory Value:       " + String.format("%.2f RWF", totalInventoryValue) + "\n" +
            "  • Low Stock Items:       " + lowStockCount + "\n\n" +
            "BUSINESS PARTNERS:\n" +
            "  • Total Suppliers:       " + totalSuppliers + "\n\n" +
            "FINANCIAL SUMMARY:\n" +
            "  • Total Sales:           " + String.format("%.2f RWF", totalSales) + "\n" +
            "  • Total Purchases:       " + String.format("%.2f RWF", totalPurchases) + "\n" +
            "  • Net Profit:            " + String.format("%.2f RWF", profit) + "\n\n" +
            "TRANSACTION ACTIVITY:\n" +
            "  • Total Transactions:    " + transactions.size() + "\n" +
            "  • Sales Transactions:    " + salesCount + "\n" +
            "  • Purchase Transactions: " + purchaseCount + "\n" +
            "══════════════════════════════════════════════════"
        );
    }
}