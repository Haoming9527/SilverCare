package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.ServiceCategory;
import com.SilverCare.SilverCare_Backend.dbaccess.ServiceCategoryDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/serviceCategories")
public class ServiceCategoryController {

    private final ServiceCategoryDAO categoryDAO = new ServiceCategoryDAO();

    @GetMapping("/all")
    public List<ServiceCategory> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    @GetMapping("/{id}")
    public ServiceCategory getCategoryById(@PathVariable int id) {
        return categoryDAO.getCategoryById(id);
    }
}
