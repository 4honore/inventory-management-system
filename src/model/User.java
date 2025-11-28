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
public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    
    public User(int userId, String username, String password, String role) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.role = role;
    }
    
    public User() {}
    
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    
    public String getUsername() {return username;}
    public void  setUsername(String username) {this.username = username;}
    
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    
    public String getRole() {return role;}
    public void setRole(String role) {this.role = role;}
}
