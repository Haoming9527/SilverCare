package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityLogDAO {

    public static void log(int userId, String action, String details) {
        String sql = "INSERT INTO silvercare.activity_logs (user_id, action, details) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn == null) return;
            
            if (userId > 0) {
                ps.setInt(1, userId);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, action);
            ps.setString(3, details);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, Object>> getAllLogs() {
        List<Map<String, Object>> logs = new ArrayList<>();
        String sql = "SELECT al.*, u.username FROM silvercare.activity_logs al " +
                "LEFT JOIN silvercare.users u ON al.user_id = u.id " +
                "ORDER BY al.timestamp DESC";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (conn == null) return logs;
            
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("userId", rs.getInt("user_id"));
                map.put("username", rs.getString("username") != null ? rs.getString("username") : "System/Guest");
                map.put("action", rs.getString("action"));
                map.put("details", rs.getString("details"));
                Timestamp ts = rs.getTimestamp("timestamp");
                map.put("timestamp", ts != null ? ts.toString() : "");
                logs.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
    public boolean deleteLog(int id) {
        String sql = "DELETE FROM silvercare.activity_logs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAllLogs() {
        String sql = "DELETE FROM silvercare.activity_logs";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn == null) return false;
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
