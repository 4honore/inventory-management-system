/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author FRANK
 */
public class DashboardMetrics {
    
    private final int productCount;
    private final int supplierCount;
    private final double totalSales;
    private final double totalPurchases;
    private final int lowStockCount;
    private final double netProfit;

    public DashboardMetrics(int productCount, int supplierCount, double totalSales, double totalPurchases, int lowStockCount, double netProfit) {
        this.productCount = productCount;
        this.supplierCount = supplierCount;
        this.totalSales = totalSales;
        this.totalPurchases = totalPurchases;
        this.lowStockCount = lowStockCount;
        this.netProfit = netProfit;
    }

    // Getters
    public int getProductCount() { return productCount; }
    public int getSupplierCount() { return supplierCount; }
    public double getTotalSales() { return totalSales; }
    public double getTotalPurchases() { return totalPurchases; }
    public int getLowStockCount() { return lowStockCount; }
    public double getNetProfit() { return netProfit; }
}
