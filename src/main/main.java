/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import view.ProductFrame;
import view.SupplierFrame;
import view.TransactionFrame;
import view.DashboardFrame;
import view.LoginFrame;

/**
 *
 * @author FRANK
 */
public class main {
     public static void main(String[] args) {
        //java.awt.EventQueue.invokeLater(() -> new ProductFrame().setVisible(true));
        //java.awt.EventQueue.invokeLater(() -> new SupplierFrame().setVisible(true));
        //java.awt.EventQueue.invokeLater(() -> new TransactionFrame().setVisible(true));
        //java.awt.EventQueue.invokeLater(() -> new DashboardFrame().setVisible(true));
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
