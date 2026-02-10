package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.ServiceCategory;
import com.SilverCare.SilverCare_Backend.dbaccess.ServiceCategoryDAO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/add")
    public org.springframework.http.ResponseEntity<String> addCategory(@org.springframework.web.bind.annotation.RequestBody ServiceCategory category) {
        if (categoryDAO.addCategory(category)) {
            return org.springframework.http.ResponseEntity.ok("Category added successfully");
        }
        return org.springframework.http.ResponseEntity.status(500).body("Failed to add category");
    }

    @org.springframework.web.bind.annotation.PutMapping("/update")
    public org.springframework.http.ResponseEntity<String> updateCategory(@org.springframework.web.bind.annotation.RequestBody ServiceCategory category) {
        if (categoryDAO.updateCategory(category)) {
            return org.springframework.http.ResponseEntity.ok("Category updated successfully");
        }
        return org.springframework.http.ResponseEntity.status(500).body("Failed to update category");
    }

    @DeleteMapping("/delete/{id}")
    public org.springframework.http.ResponseEntity<String> deleteCategory(@PathVariable int id) {
        if (categoryDAO.deleteCategory(id)) {
            return org.springframework.http.ResponseEntity.ok("Category deleted successfully");
        }
        return org.springframework.http.ResponseEntity.status(500).body("Failed to delete category");
    }
}
