package view;

import dao.ProductDao;
import dao.SupplierDao;
import dao.TransactionDao;
import model.Product;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final ProductDao productDao = new ProductDao();
    private final SupplierDao supplierDao = new SupplierDao();
    private final TransactionDao transactionDao = new TransactionDao();

    private JLabel productCountLabel;
    private JLabel supplierCountLabel;
    private JLabel salesTodayLabel;
    private JLabel purchasesTodayLabel;
    private JLabel lowStockLabel;

    public DashboardFrame() {

        setTitle("Inventory Management - Dashboard");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel header = new JLabel("Inventory Management Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 26));
        header.setForeground(Color.WHITE);
        
        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.setBackground(new Color(60, 179, 113));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> loadDashboardData());
        
        headerPanel.add(header, BorderLayout.CENTER);
        headerPanel.add(refreshBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL (Grid for statistics) =====
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        productCountLabel = createCard("Total Products", "0", new Color(135, 206, 250));
        supplierCountLabel = createCard("Total Suppliers", "0", new Color(152, 251, 152));
        salesTodayLabel = createCard("Total Sales", "0 RWF", new Color(255, 182, 193));
        purchasesTodayLabel = createCard("Total Purchases", "0 RWF", new Color(221, 160, 221));
        lowStockLabel = createCard("Low Stock Items", "0", new Color(255, 218, 185));
        
        JLabel netProfitLabel = createCard("Net Profit", "0 RWF", new Color(173, 216, 230));

        statsPanel.add(productCountLabel);
        statsPanel.add(supplierCountLabel);
        statsPanel.add(salesTodayLabel);
        statsPanel.add(purchasesTodayLabel);
        statsPanel.add(lowStockLabel);
        statsPanel.add(netProfitLabel);

        add(statsPanel, BorderLayout.CENTER);

        // ===== SIDE MENU BUTTONS =====
        JPanel menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton productBtn = createStyledButton("📦 Manage Products", new Color(70, 130, 180));
        JButton supplierBtn = createStyledButton("🏢 Manage Suppliers", new Color(60, 179, 113));
        JButton transBtn = createStyledButton("💰 Manage Transactions", new Color(255, 165, 0));
        JButton reportBtn = createStyledButton("📊 View Reports", new Color(138, 43, 226));
        JButton userBtn = createStyledButton("👥 Manage Users", new Color(184, 134, 11));
        JButton aboutBtn = createStyledButton("ℹ️ About System", new Color(100, 149, 237));
        JButton logoutBtn = createStyledButton("🚪 Logout", new Color(220, 20, 60));

        menuPanel.add(productBtn);
        menuPanel.add(supplierBtn);
        menuPanel.add(transBtn);
        menuPanel.add(reportBtn);
        menuPanel.add(userBtn);
        menuPanel.add(aboutBtn);
        menuPanel.add(logoutBtn);

        add(menuPanel, BorderLayout.EAST);

        // ===== BUTTON ACTIONS =====
        productBtn.addActionListener(e -> new ProductFrame());
        supplierBtn.addActionListener(e -> new SupplierFrame());
        transBtn.addActionListener(e -> new TransactionFrame());
        reportBtn.addActionListener(e -> new ReportFrame());
        userBtn.addActionListener(e -> new UserManagementFrame());
        
        aboutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "═══════════════════════════════════════\n" +
                "  INVENTORY MANAGEMENT SYSTEM v1.0\n" +
                "═══════════════════════════════════════\n\n" +
                "Developed by: Ishimwe Honore\n" +
                "Course: JAVA PROGRAMMING (INSY 7312)\n" +
                "Instructor: Dr SEBAGENZI Jason\n" +
                "           & Jeremie U. Tuyisenge\n\n" +
                "Features:\n" +
                "• Product Management (CRUD)\n" +
                "• Supplier Management (CRUD)\n" +
                "• Transaction Processing\n" +
                "• User Management & Authentication\n" +
                "• Comprehensive Reports & Analytics\n" +
                "• Business & Technical Validations\n\n" +
                "Design Patterns:\n" +
                "• Model-View-Controller (MVC)\n" +
                "• Data Access Object (DAO)\n\n" +
                "Technologies:\n" +
                "• Java Swing GUI\n" +
                "• JDBC API\n" +
                "• MySQL Database\n" +
                "═══════════════════════════════════════",
                "About System",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame();
            }
        });

        // Load dashboard data initially
        loadDashboardData();

        setVisible(true);
    }

    // ===== Helper method to create statistic cards =====
    private JLabel createCard(String title, String value, Color bgColor) {
        JLabel label = new JLabel(
            "<html><center>" +
            "<div style='padding: 20px;'>" +
            "<h2 style='margin: 5px; color: #2c3e50;'>" + title + "</h2>" +
            "<h1 style='margin: 10px; color: #34495e; font-size: 28px;'>" + value + "</h1>" +
            "</div>" +
            "</center></html>"
        );
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    // ===== Helper method to create styled buttons =====
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    // ===== Load dashboard data =====
    private void loadDashboardData() {
        // Total products
        List<Product> products = productDao.getAllProducts();
        updateCard(productCountLabel, "Total Products", String.valueOf(products.size()));

        // Suppliers count
        updateCard(supplierCountLabel, "Total Suppliers", String.valueOf(supplierDao.getAllSupplier().size()));

        // Transactions
        List<Transaction> transactions = transactionDao.getAllTransactions();

        double sales = 0;
        double purchases = 0;
        int lowStockCount = 0;

        for (Transaction t : transactions) {
            if (t.getType().equalsIgnoreCase("SALE")) {
                sales += t.getTotal();
            } else if (t.getType().equalsIgnoreCase("PURCHASE")) {
                purchases += t.getTotal();
            }
        }

        // Low stock calculation
        for (Product p : products) {
            if (p.getQuantity() < 10) {
                lowStockCount++;
            }
        }
        
        // Calculate net profit
        double profit = sales - purchases;

        updateCard(salesTodayLabel, "Total Sales", String.format("%.0f RWF", sales));
        updateCard(purchasesTodayLabel, "Total Purchases", String.format("%.0f RWF", purchases));
        updateCard(lowStockLabel, "Low Stock Items", String.valueOf(lowStockCount));
        
        // Update the 6th card (net profit)
        Component[] components = ((JPanel)getContentPane().getComponent(1)).getComponents();
        if (components.length > 5) {
            JLabel profitLabel = (JLabel) components[5];
            updateCard(profitLabel, "Net Profit", String.format("%.0f RWF", profit));
        }
    }
    
    // ===== Helper method to update card values =====
    private void updateCard(JLabel card, String title, String value) {
        card.setText(
            "<html><center>" +
            "<div style='padding: 20px;'>" +
            "<h2 style='margin: 5px; color: #2c3e50;'>" + title + "</h2>" +
            "<h1 style='margin: 10px; color: #34495e; font-size: 28px;'>" + value + "</h1>" +
            "</div>" +
            "</center></html>"
        );
    }
}