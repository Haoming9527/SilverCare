package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.ActivityLogDAO;
import com.SilverCare.SilverCare_Backend.dbaccess.Service;
import com.SilverCare.SilverCare_Backend.dbaccess.ServiceDAO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceDAO serviceDAO = new ServiceDAO();

    @GetMapping("/all")
    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }

    @GetMapping("/category/{categoryId}")
    public List<Service> getServicesByCategoryId(@PathVariable int categoryId) {
        return serviceDAO.getServicesByCategoryId(categoryId);
    }

    @GetMapping("/{id}")
    public Service getServiceById(@PathVariable int id) {
        return serviceDAO.getServiceById(id);
    }

    @PostMapping("/add")
    public org.springframework.http.ResponseEntity<String> addService(@RequestBody Service service) {
        if (serviceDAO.addService(service)) {
            ActivityLogDAO.log(0, "ADD_SERVICE", "Admin added new service: " + service.getServiceName());
            return org.springframework.http.ResponseEntity.ok("Service added successfully");
        }
        return org.springframework.http.ResponseEntity.status(500).body("Failed to add service");
    }

    @PutMapping("/update")
    public org.springframework.http.ResponseEntity<String> updateService(@RequestBody Service service) {
        if (serviceDAO.updateService(service)) {
            ActivityLogDAO.log(0, "UPDATE_SERVICE", "Admin updated service ID: " + service.getId());
            return org.springframework.http.ResponseEntity.ok("Service updated successfully");
        }
        return org.springframework.http.ResponseEntity.status(500).body("Failed to update service");
    }

    @DeleteMapping("/delete/{id}")
    public org.springframework.http.ResponseEntity<String> deleteService(@PathVariable int id) {
        if (serviceDAO.deleteService(id)) {
            ActivityLogDAO.log(0, "DELETE_SERVICE", "Admin deleted service ID: " + id);
            return org.springframework.http.ResponseEntity.ok("Service deleted successfully");
        }
        return org.springframework.http.ResponseEntity.status(500).body("Failed to delete service");
    }

    @GetMapping("/search/{query}")
    public List<Service> searchServices(@PathVariable String query) {
        return serviceDAO.searchServices(query);
    }

    @GetMapping("/demand")
    public List<java.util.Map<String, Object>> getServiceDemand() {
        return serviceDAO.getServiceDemand();
    }
}
