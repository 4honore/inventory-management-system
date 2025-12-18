package view;

import controller.DashboardController;
import model.DashboardMetrics;
import model.UserSession; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

/**
 * DashboardFrame - System Overview and Main Navigation Hub.
 * Now implements Role-Based Access Control (RBAC) and updated colors.
 */
public class DashboardFrame extends JFrame {

    private final DashboardController controller;

    // Card Panels and Labels
    private JPanel productCard, supplierCard, lowStockCard;
    private JLabel productCountLabel, supplierCountLabel, lowStockLabel; 
    private JPanel salesCard, purchasesCard, profitCard; 
    private JLabel salesTodayLabel, purchasesTodayLabel, netProfitLabel;

    // Define colors (Matching LoginFrame layout)
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);   // Professional Blue
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light Gray (New)
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);   // Dark Gray-Blue (For Card backgrounds)
    private static final Color PROFIT_COLOR = new Color(39, 174, 96);     // Green
    private static final Color LOSS_COLOR = new Color(192, 57, 43);       // Red
    private static final Color NAV_COLOR = new Color(44, 62, 80);         // Navigation Background
    private static final Color CLICK_EFFECT_COLOR = new Color(70, 96, 118); 

    public DashboardFrame() {
        controller = new DashboardController();

        // Security Check: If no user is logged in, redirect to login
        if (UserSession.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(null, "Session expired or unauthorized access. Please log in..", "Authorization Error", JOptionPane.ERROR_MESSAGE);
            new LoginFrame().setVisible(true);
            return; // Exit constructor to prevent frame from showing
        }
        
        // Display logged-in user details in the title
        String userRole = UserSession.getCurrentUser().getRole();
        String username = UserSession.getCurrentUser().getUsername();
        setTitle("Inventory Management - Dashboard & Navigation (" + username + " | " + userRole + ")");
        
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Set the frame's background to the new light color
        getContentPane().setBackground(BACKGROUND_COLOR);

        // ===== NAVIGATION PANEL (WEST) =====
        JPanel navPanel = createNavigationPanel();
        add(navPanel, BorderLayout.WEST);

        // ===== MAIN CONTENT PANEL (CENTER) =====
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBackground(BACKGROUND_COLOR); // Set content panel background
        
        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("System Overview", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        // --- Cards Panel ---
        JPanel cardsPanel = createCardsPanel();
        mainContentPanel.add(cardsPanel, BorderLayout.CENTER);
        
        add(mainContentPanel, BorderLayout.CENTER);
        
        loadDashboardData();
        setVisible(true);
    }
    
    // =================================================================
    //                    NAVIGATION PANEL SETUP (RBAC IMPLEMENTATION)
    // =================================================================

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(NAV_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Title/Logo Placeholder
        JLabel logoLabel = new JLabel("INVENTORY HUB", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE); // White for high contrast on dark nav panel
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(30)); 

        // 1. Dashboard (Always accessible)
        addButton(panel, "📊 Dashboard", e -> {/* Already here */});
        
        // 2. Products, Suppliers, Transactions (Operational Access: ADMIN, MANAGER, STAFF)
        if (UserSession.hasAnyRole("ADMIN", "MANAGER", "STAFF")) {
            addButton(panel, "📦 Products", e -> new ProductFrame().setVisible(true));
            addButton(panel, "👥 Suppliers", e -> new SupplierFrame().setVisible(true));
            addButton(panel, "💵 Transactions", e -> new TransactionFrame().setVisible(true));
        }

        // 3. Reports (Managerial Access: ADMIN, MANAGER)
        if (UserSession.hasAnyRole("ADMIN", "MANAGER")) {
            addButton(panel, "📈 Reports", e -> new ReportFrame().setVisible(true));
        }
        
        // 4. User Management (Admin Exclusive Access)
        if (UserSession.hasRole("ADMIN")) {
            addButton(panel, "⚙️ Users", e -> new UserManagementFrame().setVisible(true));
        }
        
        // Logout button at the bottom (Always accessible)
        panel.add(Box.createVerticalGlue());
        
        // FIX: Update Logout action to call UserSession.logout()
        addButton(panel, "🚪 Logout", e -> {
            UserSession.logout(); // Clear the session
            this.dispose();
            new LoginFrame().setVisible(true); 
        });

        return panel;
    }

    private void addButton(JPanel panel, String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(NAV_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(NAV_COLOR);
            }
        });
        
        button.addActionListener(listener);
        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
    }


    // =================================================================
    //                         CARDS PANEL SETUP (COLOR CHANGE)
    // =================================================================

    private JPanel createCardsPanel() {
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setBackground(BACKGROUND_COLOR); // <-- NEW: Set card container background to light gray
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Initialize Labels
        productCountLabel = new JLabel("0");
        supplierCountLabel = new JLabel("0");
        lowStockLabel = new JLabel("0");
        salesTodayLabel = new JLabel("0 RWF");
        purchasesTodayLabel = new JLabel("0 RWF");
        netProfitLabel = new JLabel("0 RWF");
        
        // 2. Create Clickable Cards (JPanels)
        productCard = createCardPanel("Total Products", productCountLabel, SECONDARY_COLOR);
        supplierCard = createCardPanel("Total Suppliers", supplierCountLabel, SECONDARY_COLOR);
        lowStockCard = createCardPanel("Low Stock Items", lowStockLabel, LOSS_COLOR);
        salesCard = createCardPanel("Total Sales", salesTodayLabel, SECONDARY_COLOR);
        purchasesCard = createCardPanel("Total Purchases", purchasesTodayLabel, SECONDARY_COLOR);
        profitCard = createCardPanel("Net Profit", netProfitLabel, PROFIT_COLOR);

        // 3. Add Mouse Listeners for Navigation (Quick links)
        addProductNavigation(productCard);
        addProductNavigation(lowStockCard); 
        addSupplierNavigation(supplierCard);
        
        // 4. Add generic listeners to financial cards to prevent the error
        addFinancialCardListener(salesCard, SECONDARY_COLOR);
        addFinancialCardListener(purchasesCard, SECONDARY_COLOR);
        addFinancialCardListener(profitCard, PROFIT_COLOR); 

        cardsPanel.add(productCard);
        cardsPanel.add(supplierCard);
        cardsPanel.add(lowStockCard);
        cardsPanel.add(salesCard);
        cardsPanel.add(purchasesCard);
        cardsPanel.add(profitCard);
        
        return cardsPanel;
    }
    
    // Method to create the clickable JPanel card
    private JPanel createCardPanel(String title, JLabel valueLabel, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Indicate clickability

        return card;
    }

    // Method to add navigation to ProductFrame
    private void addProductNavigation(JPanel card) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ensure only authorized users can navigate to product frame
                if (UserSession.hasAnyRole("ADMIN", "MANAGER", "STAFF")) {
                    new ProductFrame().setVisible(true);
                } else {
                     JOptionPane.showMessageDialog(null, "You do not have permission to access the Products module.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Method to add navigation to SupplierFrame
    private void addSupplierNavigation(JPanel card) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Ensure only authorized users can navigate to supplier frame
                 if (UserSession.hasAnyRole("ADMIN", "MANAGER", "STAFF")) {
                    new SupplierFrame().setVisible(true);
                } else {
                     JOptionPane.showMessageDialog(null, "You do not have permission to access the Suppliers module.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    
    /**
     * Adds a generic click effect for non-navigational cards.
     */
    private void addFinancialCardListener(JPanel card, Color defaultColor) {
        // [Existing implementation for card listener remains the same to fix previous error]
         card.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                card.setBackground(CLICK_EFFECT_COLOR); // Change color on press
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                // Restore original color
                Color currentColor = defaultColor;
                // Special handling for profit card as its color changes dynamically
                if (card == profitCard && metrics != null) { 
                    currentColor = metrics.getNetProfit() >= 0 ? PROFIT_COLOR : LOSS_COLOR;
                }
                card.setBackground(currentColor); 
            }
            // Add a simple hover effect for visual consistency
            @Override
            public void mouseEntered(MouseEvent e) {
                if (card.getBackground() != CLICK_EFFECT_COLOR) {
                    card.setBackground(defaultColor.brighter());
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                 if (card.getBackground() != CLICK_EFFECT_COLOR) {
                    card.setBackground(defaultColor);
                }
            }
        });
    }

    // =================================================================
    //                           DATA LOADING (UNCHANGED)
    // =================================================================

    private DashboardMetrics metrics; // Field to hold metrics for use in listeners

    private void loadDashboardData() {
        try {
            metrics = controller.getMetrics(); // Assign to field
            DecimalFormat currencyFormatter = new DecimalFormat("#,##0 RWF");

            productCountLabel.setText(String.valueOf(metrics.getProductCount()));
            supplierCountLabel.setText(String.valueOf(metrics.getSupplierCount()));
            
            // Low Stock
            Color lowStockBg = metrics.getLowStockCount() > 0 ? LOSS_COLOR : SECONDARY_COLOR;
            lowStockLabel.setText(String.valueOf(metrics.getLowStockCount()));
            lowStockCard.setBackground(lowStockBg);
            
            salesTodayLabel.setText(currencyFormatter.format(metrics.getTotalSales()));
            purchasesTodayLabel.setText(currencyFormatter.format(metrics.getTotalPurchases()));
            
            // Net Profit
            Color profitBg = metrics.getNetProfit() >= 0 ? PROFIT_COLOR : LOSS_COLOR;
            netProfitLabel.setText(currencyFormatter.format(metrics.getNetProfit()));
            profitCard.setBackground(profitBg); // Set the correct dynamic color

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "A system error occurred while loading dashboard data: " + e.getMessage(), 
                "System Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}