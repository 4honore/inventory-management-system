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
public class Product {
    private int productId;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private int supplierId;
    
    public Product(int productId, String name, String category, int quantity, double price, int supplierId){
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.supplierId = supplierId;
    }
    
    public Product(){}
    
    public int getProductId(){ return productId;}
    public void setProductId(int productId ){this.productId = productId;}
    
    public String getName(){return name;}
    public void setName(String name){this.name = name;}
    
    public String getCategory(){return category;}
    public void setCategory( String category){this.category = category;}
    
    public int getQuantity(){ return quantity;}
    public void setQuantity(int quantity) { this.quantity = quantity;}
    
    public double getPrice(){ return price;}
    public void setPrice(double price){this.price = price;}
    
    public int getSupplierId(){ return supplierId;}
    public void setSupplierId(int supplierId){this.supplierId =supplierId;}

}