/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author FRANK
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/inventory_management_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("database connected successfully");
        } catch (SQLException e){
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
        return conn;
    }
    
    public static void main(String[] args) {
getConnection();
}
}


