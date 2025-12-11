/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Supplier;

/**
 *
 * @author FRANK
 */
public class SupplierDao {
    
    public void addSupplier(Supplier supplier){
        String sql = "INSERT INTO suppliers (name, contact, address, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql)){
        
            pst.setString(1, supplier.getName());
            pst.setString(2, supplier.getContact());
            pst.setString(3, supplier.getAddress());
            pst.setString(4, supplier.getEmail());
            
            int rows = pst.executeUpdate();
            if (rows > 0){
                JOptionPane.showMessageDialog(null, "Supplier added successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to add supplier: " + e.getMessage());
        }
    }
    
    public List<Supplier> getAllSupplier(){
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers";
        
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)){
        
            while (rs.next()){
                Supplier s = new Supplier();
                s.setSupplierId(rs.getInt("supplier_id"));
                s.setName(rs.getString("name"));
                s.setContact(rs.getString("contact"));
                s.setAddress(rs.getString("address"));
                s.setEmail(rs.getString("email"));
                suppliers.add(s);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to retrieve suppliers: " + e.getMessage());
        }
        
        return suppliers;
    }
    
    public Supplier getSupplierById(int id){
        Supplier supplier = null;
        String query = "SELECT * FROM suppliers WHERE supplier_id = ?";
        
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()){
                supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("address"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to retrieve supplier: " + e.getMessage());
        }
        return supplier;
    }
    
    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE suppliers SET name=?, contact=?, address=?, email=? WHERE supplier_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, supplier.getName());
            pst.setString(2, supplier.getContact());
            pst.setString(3, supplier.getAddress());
            pst.setString(4, supplier.getEmail());
            pst.setInt(5, supplier.getSupplierId());
            
            int rows = pst.executeUpdate();
            if(rows > 0){
               JOptionPane.showMessageDialog(null, "Supplier updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Supplier not found");
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to update supplier: " + e.getMessage());
        }
    }
    
    public void deleteSupplier(int supplierId){
        String sql = "DELETE FROM suppliers WHERE supplier_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql)){
        
            pst.setInt(1, supplierId);
            int rows = pst.executeUpdate();
            
            if(rows > 0){
                JOptionPane.showMessageDialog(null, "Supplier deleted successfully!");    
            } else {
                JOptionPane.showMessageDialog(null, "Supplier not found!");
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to delete supplier: " + e.getMessage());
        }
    }
}