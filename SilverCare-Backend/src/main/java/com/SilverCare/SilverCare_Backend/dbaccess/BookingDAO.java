package com.SilverCare.SilverCare_Backend.dbaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public int createBooking(Booking booking) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        int bookingId = 0;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) throw new SQLException("Failed to establish database connection.");
            conn.setAutoCommit(false); // Start transaction

            // Insert into booking
            String sql = "INSERT INTO silvercare.booking (user_id, scheduled_date, specific_caregiver, special_request, status) VALUES (?, ?, ?, ?, ?) RETURNING id";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, booking.getUserId());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(booking.getScheduledDate()));
            ps.setString(3, booking.getSpecificCaregiver());
            ps.setString(4, booking.getSpecialRequest());
            ps.setString(5, "Pending");

            rs = ps.executeQuery();
            if (rs.next()) {
                bookingId = rs.getInt("id");
            }

            // Insert into booking_details
            String sql2 = "INSERT INTO silvercare.booking_details (booking_id, service_id) VALUES (?, ?)";
            ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, bookingId);
            ps2.setInt(2, booking.getServiceId());
            ps2.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (ps2 != null) ps2.close();
            if (conn != null) conn.close();
        }
        return bookingId;
    }

    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = null;
        String sql = "SELECT b.*, s.service_name, s.price " +
                     "FROM silvercare.booking b " +
                     "JOIN silvercare.booking_details bd ON b.id = bd.booking_id " +
                     "JOIN silvercare.service s ON bd.service_id = s.id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.scheduled_date DESC";

        try {
            conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setUserId(rs.getInt("user_id"));
                java.sql.Timestamp ts = rs.getTimestamp("scheduled_date");
                b.setScheduledDate(ts != null ? ts.toString() : null);
                b.setSpecificCaregiver(rs.getString("specific_caregiver"));
                b.setSpecialRequest(rs.getString("special_request"));
                b.setStatus(rs.getString("status"));
                b.setServiceName(rs.getString("service_name"));
                b.setPrice(rs.getDouble("price"));
                bookings.add(b);

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
        return bookings;
    }

    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE silvercare.booking SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
