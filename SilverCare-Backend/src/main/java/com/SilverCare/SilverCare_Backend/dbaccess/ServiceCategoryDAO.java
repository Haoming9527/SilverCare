package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryDAO {

    public List<ServiceCategory> getAllCategories() {
        List<ServiceCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM silvercare.service_category ORDER BY category_name ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                ServiceCategory cat = new ServiceCategory();
                cat.setId(rs.getInt("id"));
                cat.setCategoryName(rs.getString("category_name"));
                categories.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public ServiceCategory getCategoryById(int id) {
        String sql = "SELECT * FROM silvercare.service_category WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ServiceCategory(rs.getInt("id"), rs.getString("category_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
