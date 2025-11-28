/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author FRANK
 */
public class Transaction {
    
    private int transactionId;
    private int productId;
    private int quantity;
    private Timestamp date;
    private String type;
    private double total;
    
    public Transaction(int transactionId, int productId, int quantity, Timestamp date, String type, double total) {
    this.transactionId = transactionId;
    this.productId = productId;
    this.quantity = quantity;
    this.date = date;
    this.type = type;
    this.total = total;
    }
    
    public Transaction() {}
    
    public int getTransactionId() { return transactionId;}
    public void setTransactionId(int transactionId) {this.transactionId = transactionId;}
    
    public int getProductId(){ return productId;}
    public void setProductId(int productId) {this.productId = productId;}
    
    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
    
    public Timestamp getDate() {return date;}
    public void setDate(Timestamp date) {this.date =date;}
    
    public String getType(){return type;}
    public void setType(String type) {this.type = type;}
    
    public double getTotal() {return total;}
    public void setTotal( double total){ this.total = total;}
}
