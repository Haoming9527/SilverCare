package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackDAO {

    public boolean saveFeedback(Feedback feedback) {
        Connection conn = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            
            if (feedback.getId() > 0) {
                String sql = "UPDATE silvercare.feedback SET rating = ?, comment = ? WHERE id = ? AND user_id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, feedback.getRating());
                ps.setString(2, feedback.getComment());
                ps.setInt(3, feedback.getId());
                ps.setInt(4, feedback.getUserId());
                success = ps.executeUpdate() > 0;
            } else {
                // Insert new
                String sql = "INSERT INTO silvercare.feedback (user_id, booking_id, rating, comment) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, feedback.getUserId());
                ps.setInt(2, feedback.getBookingId());
                ps.setInt(3, feedback.getRating());
                ps.setString(4, feedback.getComment());
                success = ps.executeUpdate() > 0;
            }
            if(success) {

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
        return success;
    }

    public java.util.List<Feedback> getFeedbackByUserId(int userId) {
        java.util.List<Feedback> feedbackList = new java.util.ArrayList<>();
        Connection conn = null;
        String sql = "SELECT * FROM silvercare.feedback WHERE user_id = ?";
        
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            java.sql.ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Feedback f = new Feedback();
                f.setId(rs.getInt("id"));
                f.setUserId(rs.getInt("user_id"));
                f.setBookingId(rs.getInt("booking_id"));
                f.setRating(rs.getInt("rating"));
                f.setComment(rs.getString("comment"));
                feedbackList.add(f);

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
        return feedbackList;
    }

    public java.util.List<Feedback> getAllFeedback() {
        java.util.List<Feedback> feedbackList = new java.util.ArrayList<>();
        Connection conn = null;
        String sql = "SELECT f.*, u.username, s.service_name FROM silvercare.feedback f " +
                     "LEFT JOIN silvercare.users u ON f.user_id = u.id " +
                     "LEFT JOIN silvercare.booking b ON f.booking_id = b.id " +
                     "LEFT JOIN silvercare.booking_details bd ON b.id = bd.booking_id " +
                     "LEFT JOIN silvercare.service s ON bd.service_id = s.id " +
                     "ORDER BY f.id DESC";
        
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Feedback f = new Feedback();
                f.setId(rs.getInt("id"));
                f.setUserId(rs.getInt("user_id"));
                f.setBookingId(rs.getInt("booking_id"));
                f.setRating(rs.getInt("rating"));
                f.setComment(rs.getString("comment"));
                f.setUsername(rs.getString("username"));
                f.setServiceName(rs.getString("service_name"));
                feedbackList.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return feedbackList;
    }

    public boolean deleteFeedback(int feedbackId) {
        Connection conn = null;
        String sql = "DELETE FROM silvercare.feedback WHERE id = ?";
        try {
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, feedbackId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
