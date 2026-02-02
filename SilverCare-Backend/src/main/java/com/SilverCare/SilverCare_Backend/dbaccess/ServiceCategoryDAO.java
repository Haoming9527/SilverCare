package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryDAO {

    public List<ServiceCategory> getAllCategories() {
        List<ServiceCategory> categories = new ArrayList<>();
        Connection conn = null;
        String sql = "SELECT * FROM silvercare.service_category ORDER BY category_name ASC";
        
        try {
            conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ServiceCategory cat = new ServiceCategory();
                cat.setId(rs.getInt("id"));
                cat.setCategoryName(rs.getString("category_name"));
                categories.add(cat);

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
        return categories;
    }

    public ServiceCategory getCategoryById(int id) {
        ServiceCategory cat = null;
        Connection conn = null;
        String sql = "SELECT * FROM silvercare.service_category WHERE id = ?";
        try {
            conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                cat = new ServiceCategory(rs.getInt("id"), rs.getString("category_name"));

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
        return cat;
    }
}
