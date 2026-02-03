package com.SilverCare.SilverCare_Backend.controller;

import org.springframework.web.bind.annotation.*;
import com.SilverCare.SilverCare_Backend.dbaccess.User;
import com.SilverCare.SilverCare_Backend.dbaccess.UserDAO;

@RestController
@RequestMapping("/users")
public class UserController {

    @RequestMapping(
            path = "/login",
            consumes = "application/json",
            method = RequestMethod.POST)
    public User login(@RequestBody User loginRequest) {
        User user = null;
        try {
            UserDAO userDAO = new UserDAO();
            user = userDAO.login(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (Exception e) {
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
                rec = userDAO.registerUser(user);
            } else {
                rec = -1; // Indicate email exists
            }
        } catch (Exception e) {
        }
        return rec;
    }
}
