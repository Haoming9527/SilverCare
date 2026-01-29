package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO {

    public Role getRoleById(int id) {
        Role rBean = null;
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM silvercare.role WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rBean = new Role();
                rBean.setId(rs.getInt("id"));
                rBean.setRoleName(rs.getString("role_name"));
            }
        } catch (SQLException e) {
            System.out.println("RoleDAO - Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rBean;
    }
}
