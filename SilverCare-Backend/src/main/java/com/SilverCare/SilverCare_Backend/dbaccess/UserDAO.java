package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // ================= REGISTER =================

    public boolean emailExists(String email) {
        Connection conn = null;
        boolean exists = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT 1 FROM silvercare.users WHERE email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            exists = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exists;
    }

    public int registerUser(User user) {
        Connection conn = null;
        int rowsAffected = 0;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO silvercare.users (username, email, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getRole());

            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowsAffected;
    }

    // ================= LOGIN =================

    public User login(String email, String password) {
        User uBean = null;
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT * FROM silvercare.users WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                uBean = new User();
                uBean.setId(rs.getInt("id"));
                uBean.setUsername(rs.getString("username"));
                uBean.setEmail(rs.getString("email"));
                uBean.setPassword(rs.getString("password"));
                uBean.setRole(rs.getInt("role"));
                System.out.print(".....done writing to bean!......");
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
        return uBean;
    }
}