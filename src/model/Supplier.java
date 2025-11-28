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
public class Supplier {
    private int supplierId;
    private String name;
    private String contact;
    private String address;
    private String email;
    
    public Supplier(int supplierId, String name, String contact, String address, String email){
    this.supplierId = supplierId;
    this.name = name;
    this.contact = contact;
    this.address = address;
    this.email = email;
    }
    
    public Supplier(){}
    
    
    public int getSupplierId() { return supplierId;}
    public void setSupplierId(int supplierId) {this.supplierId = supplierId;}
    
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    
    
    public String getContact() {return contact;}
    public void setContact(String contact) {this.contact = contact;}
    
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    
}


