/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.SupplierDao;
import model.Supplier;
import java.util.List;

/**
 *
 * @author FRANK
 */
public class SupplierController {
    
    private final SupplierDao supplierDao;
    
    public SupplierController(){
    supplierDao = new SupplierDao();
    }
    
    public void addSupplier(String name, String contact, String address, String email){
    if (name == null || name.trim().isEmpty()){
    System.out.println("Supplier name cannot be empty.");
    return;
    }
    
    Supplier supplier = new Supplier();
    supplier.setName(name);
    supplier.setContact(contact);
    supplier.setAddress(address);
    supplier.setEmail(email);
    
    supplierDao.addSupplier(supplier);
    System.out.println("Supplier updated successfully!");
    }
    
    public void updateSupplier(int id, String name,String contact, String address, String email){
    if (id <= 0){
    System.out.println("Invalid supplier Id.");
    return;
    }
    
    Supplier supplier = new Supplier();
    supplier.setSupplierId(id);
    supplier.setName(name);
    supplier.setContact(contact);
    supplier.setAddress(address);
    supplier.setEmail(email);
    
    supplierDao.updateSupplier(supplier);
    System.out.println("Supplier updated successfully!");
    }
    
    public void deleteSupplier(int id){
    if (id <= 0){
    System.out.println("Invalid supplier Id.");
    return;
    }
    supplierDao.deleteSupplier(id);
    System.out.println("Supplier deleted Successfully!");
    }
    
    public List<Supplier> getAllSupplier(){
    return supplierDao.getAllSupplier();
    }
    
    //find supplier by id (optional)
    //public SupplierController(SupplierDao supplierDao) {
      //  this.supplierDao = supplierDao;
    //}
    public Supplier getSupplierById(int id){
        if (id <= 0){
        System.out.println("Invalid supplier ID");
        return null;
        }
        return supplierDao.getSupplierById(id);
    
    }
    
    
}
