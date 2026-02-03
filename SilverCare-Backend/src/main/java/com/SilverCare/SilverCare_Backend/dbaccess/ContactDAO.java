package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ContactDAO {

    public boolean saveMessage(ContactMessage msg) {
        Connection conn = null;
        boolean success = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO silvercare.contact_messages (name, email, phone, subject, message) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, msg.getName());
            ps.setString(2, msg.getEmail());
            ps.setString(3, msg.getPhone());
            ps.setString(4, msg.getSubject());
            ps.setString(5, msg.getMessage());

            success = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }
}
