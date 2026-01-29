package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.Role;
import com.SilverCare.SilverCare_Backend.dbaccess.RoleDAO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @RequestMapping(
            path = "/getRole/{id}",
            method = RequestMethod.GET)
    public Role getRole(@PathVariable int id) {
        Role role = null;
        try {
            RoleDAO roleDAO = new RoleDAO();
            role = roleDAO.getRoleById(id);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return role;
    }
}
