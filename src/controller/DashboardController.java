/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.ProductDao;
import dao.SupplierDao;
import dao.TransactionDao;
import model.Product;
import model.Transaction;
import model.DashboardMetrics; // <-- New Import
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author FRANK
 */
public class DashboardController {

    private final ProductDao productDao;
    private final SupplierDao supplierDao;
    private final TransactionDao transactionDao;
    
    // Define the threshold here, as it's a business logic constant
    private static final int LOW_STOCK_THRESHOLD = 10; 

    public DashboardController() {
        this.productDao = new ProductDao();
        this.supplierDao = new SupplierDao();
        this.transactionDao = new TransactionDao();
    }
    
    /**
     * Aggregates all necessary data into a single DTO for the Dashboard view.
     * @return DashboardMetrics DTO
     */
    public DashboardMetrics getMetrics() {
        // Fetch raw data
        List<Product> products = productDao.getAllProducts();
        List<Transaction> transactions = transactionDao.getAllTransactions();
        
        // 1. Counts
        int productCount = products.size();
        int supplierCount = supplierDao.getAllSupplier().size();
        
        // 2. Financials
        double totalSales = transactions.stream()
                .filter(t -> "SALE".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getTotal)
                .sum();
        
        double totalPurchases = transactions.stream()
                .filter(t -> "PURCHASE".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getTotal)
                .sum();
        
        // 3. Low Stock
        long lowStockCount = products.stream()
                .filter(p -> p.getQuantity() < LOW_STOCK_THRESHOLD)
                .count();
        
        // 4. Net Profit
        double netProfit = totalSales - totalPurchases;
        
        // Return the DTO
        return new DashboardMetrics(
            productCount, 
            supplierCount, 
            totalSales, 
            totalPurchases, 
            (int)lowStockCount, 
            netProfit
        );
    }
    
    // Helper method to get the actual list of low stock products for a table/list on the dashboard
    public List<Product> getLowStockProducts() {
        return productDao.getAllProducts().stream()
                .filter(p -> p.getQuantity() < LOW_STOCK_THRESHOLD)
                .collect(Collectors.toList());
    }
    
    // Method to fetch recent transactions if the dashboard needs a table of them
    public List<Transaction> getRecentTransactions(int limit) {
        List<Transaction> transactions = transactionDao.getAllTransactions(); 
        return transactions.stream()
            .limit(limit)
            .collect(Collectors.toList());
    }
}
