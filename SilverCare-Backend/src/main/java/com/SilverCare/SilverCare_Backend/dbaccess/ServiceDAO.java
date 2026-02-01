package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public List<Service> getServicesByCategoryId(int categoryId) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM silvercare.service WHERE category_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Service svc = new Service();
                    svc.setId(rs.getInt("id"));
                    svc.setServiceName(rs.getString("service_name"));
                    svc.setDescription(rs.getString("description"));
                    svc.setPrice(rs.getDouble("price"));
                    svc.setCategoryId(rs.getInt("category_id"));
                    svc.setImageUrl(rs.getString("image_url"));
                    services.add(svc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM silvercare.service";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Service svc = new Service();
                svc.setId(rs.getInt("id"));
                svc.setServiceName(rs.getString("service_name"));
                svc.setDescription(rs.getString("description"));
                svc.setPrice(rs.getDouble("price"));
                svc.setCategoryId(rs.getInt("category_id"));
                svc.setImageUrl(rs.getString("image_url"));
                services.add(svc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
}
