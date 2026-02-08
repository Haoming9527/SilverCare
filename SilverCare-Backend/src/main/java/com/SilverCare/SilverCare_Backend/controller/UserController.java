package com.SilverCare.SilverCare_Backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.SilverCare.SilverCare_Backend.dbaccess.ActivityLogDAO;
import com.SilverCare.SilverCare_Backend.dbaccess.User;
import com.SilverCare.SilverCare_Backend.dbaccess.UserDAO;
import com.SilverCare.SilverCare_Backend.service.PasswordService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PasswordService passwordService;

    @RequestMapping(
            path = "/login",
            consumes = "application/json",
            method = RequestMethod.POST)
    public User login(@RequestBody User loginRequest) {
        User user = null;
        try {
            UserDAO userDAO = new UserDAO();
            user = userDAO.getUserByEmail(loginRequest.getEmail());
            
            if (user != null) {
                if (passwordService.checkPassword(loginRequest.getPassword(), user.getPassword())) {
                    ActivityLogDAO.log(user.getId(), "LOGIN", "User logged in via email: " + user.getEmail());
                    return user;
                } 
                
                if (loginRequest.getPassword().equals(user.getPassword())) {
                    user.setPassword(passwordService.hashPassword(loginRequest.getPassword()));
                    userDAO.updateUser(user);
                    return user;
                }
                
                return null; 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @RequestMapping(
            path = "/register",
            consumes = "application/json",
            method = RequestMethod.POST)
    public int register(@RequestBody User user) {
        int rec = 0;
        try {
            UserDAO userDAO = new UserDAO();
            if (!userDAO.emailExists(user.getEmail())) {
                // Hash the password before saving
                user.setPassword(passwordService.hashPassword(user.getPassword()));
                rec = userDAO.registerUser(user);
                if (rec > 0) {
                    ActivityLogDAO.log(rec, "REGISTER", "New user registered: " + user.getEmail());
                }
            } else {
                rec = -1; // Indicate email exists
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rec;
    }

    @GetMapping("/all")
    public java.util.List<User> getAllUsers() {
        try {
            UserDAO userDAO = new UserDAO();
            return userDAO.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(404).body("{\"message\": \"User not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\": \"Server error\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            UserDAO userDAO = new UserDAO();
            user.setId(id);
            
            User currentData = userDAO.getUserById(id);
            if (currentData == null) {
                return ResponseEntity.status(404).body("{\"message\": \"User not found\"}");
            }
            
            // Handle password hashing if a new password is provided
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordService.hashPassword(user.getPassword()));
            } else {
                user.setPassword(currentData.getPassword());
            }
            
            // If role is 0 (default/unspecified in JSON), keep existing role
            if (user.getRole() == 0) {
                user.setRole(currentData.getRole());
            }
            
            int result = userDAO.updateUser(user);
            if (result > 0) {
                ActivityLogDAO.log(id, "UPDATE_PROFILE", "Admin/User updated user details for ID: " + id);
                return ResponseEntity.ok().body("{\"message\": \"User updated successfully\"}");
            } else {
                return ResponseEntity.status(500).body("{\"message\": \"Failed to update user\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\": \"Server error\"}");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            UserDAO userDAO = new UserDAO();
            int result = userDAO.deleteUser(id);
            if (result > 0) {
                ActivityLogDAO.log(id, "DELETE_USER", "User account deleted ID: " + id);
                return ResponseEntity.ok().body("{\"message\": \"User deleted successfully\"}");
            } else {
                return ResponseEntity.status(404).body("{\"message\": \"User not found or delete failed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\": \"Server error\"}");
        }
    }

    @GetMapping("/analytics/area-distribution")
    public List<java.util.Map<String, Object>> getAreaDistribution() {
        try {
            UserDAO userDAO = new UserDAO();
            return userDAO.getClientsByArea();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
}
