package com.SilverCare.SilverCare_Backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.SilverCare.SilverCare_Backend.dbaccess.ActivityLogDAO;
import com.SilverCare.SilverCare_Backend.dbaccess.User;
import com.SilverCare.SilverCare_Backend.dbaccess.UserDAO;
import com.SilverCare.SilverCare_Backend.service.PasswordService;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody User user) {
        try {
            UserDAO userDAO = new UserDAO();
            user.setId(id);
            
            User existingUser = userDAO.getUserByEmail(user.getEmail()); 
            
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordService.hashPassword(user.getPassword()));
            } else if (existingUser != null) {
                user.setPassword(existingUser.getPassword());
            }
            
            int result = userDAO.updateUser(user);
            if (result > 0) {
                ActivityLogDAO.log(id, "UPDATE_PROFILE", "User updated profile details");
                return ResponseEntity.ok().body("{\"message\": \"User updated successfully\"}");
            } else {
                return ResponseEntity.status(500).body("{\"message\": \"Failed to update user\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\": \"Server error\"}");
        }
    }
}
