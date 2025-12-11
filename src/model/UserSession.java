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
public class UserSession {
    private static User currentUser;

    /**
     * Sets the current logged-in user.
     * @param user The User object returned after successful login.
     */
    public static void login(User user) {
        currentUser = user;
    }

    /**
     * Clears the current user session upon logout.
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Returns the currently logged-in user.
     * @return The User object, or null if no user is logged in.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if the current user has the specified role.
     * @param role The role to check (e.g., "ADMIN").
     * @return true if the user has the role, false otherwise.
     */
    public static boolean hasRole(String role) {
        if (currentUser == null || currentUser.getRole() == null) {
            return false;
        }
        return currentUser.getRole().equalsIgnoreCase(role);
    }
    
    /**
     * Checks if the current user has AT LEAST one of the specified roles.
     * @param roles A variable number of roles to check (e.g., "ADMIN", "MANAGER").
     * @return true if the user has any of the roles, false otherwise.
     */
    public static boolean hasAnyRole(String... roles) {
        if (currentUser == null || currentUser.getRole() == null) {
            return false;
        }
        String userRole = currentUser.getRole();
        for (String role : roles) {
            if (userRole.equalsIgnoreCase(role)) {
                return true;
            }
        }
        return false;
    }
}
