package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.Service;
import com.SilverCare.SilverCare_Backend.dbaccess.ServiceDAO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}
