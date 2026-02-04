package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public List<Service> getServicesByCategoryId(int categoryId) {
        List<Service> services = new ArrayList<>();
        Connection conn = null;
        String sql = "SELECT * FROM silvercare.service WHERE category_id = ?";
        
        try {
            conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            
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
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return services;
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        Connection conn = null;
        String sql = "SELECT s.*, c.category_name FROM silvercare.service s " +
                     "LEFT JOIN silvercare.service_category c ON s.category_id = c.id";
        
        try {
            conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
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
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return services;
    }

    public Service getServiceById(int serviceId) {
        Connection conn = null;
        String sql = "SELECT * FROM silvercare.service WHERE id = ?";
        
        try {
            conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Service svc = new Service();
                svc.setId(rs.getInt("id"));
                svc.setServiceName(rs.getString("service_name"));
                svc.setDescription(rs.getString("description"));
                svc.setPrice(rs.getDouble("price"));
                svc.setCategoryId(rs.getInt("category_id"));
                svc.setImageUrl(rs.getString("image_url"));
                return svc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean addService(Service service) {
        Connection conn = null;
        String sql = "INSERT INTO silvercare.service (service_name, description, price, category_id, image_url) VALUES (?, ?, ?, ?, ?)";
        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setDouble(3, service.getPrice());
            pstmt.setInt(4, service.getCategoryId());
            pstmt.setString(5, service.getImageUrl());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean updateService(Service service) {
        Connection conn = null;
        String sql = "UPDATE silvercare.service SET service_name = ?, description = ?, price = ?, category_id = ?, image_url = ? WHERE id = ?";
        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setDouble(3, service.getPrice());
            pstmt.setInt(4, service.getCategoryId());
            pstmt.setString(5, service.getImageUrl());
            pstmt.setInt(6, service.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public boolean deleteService(int serviceId) {
        Connection conn = null;
        String sql = "DELETE FROM silvercare.service WHERE id = ?";
        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, serviceId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
