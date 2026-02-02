package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.Booking;
import com.SilverCare.SilverCare_Backend.dbaccess.BookingDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingDAO bookingDAO = new BookingDAO();

    @PostMapping("/create")
    public org.springframework.http.ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            int id = bookingDAO.createBooking(booking);
            return org.springframework.http.ResponseEntity.ok(id);
        } catch (Exception e) {
            e.printStackTrace();
            return org.springframework.http.ResponseEntity.status(500).body("Error creating booking: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public org.springframework.http.ResponseEntity<?> getBookingsByUser(@PathVariable int userId) {
        try {
            List<Booking> bookings = bookingDAO.getBookingsByUserId(userId);
            return org.springframework.http.ResponseEntity.ok(bookings);
        } catch (Exception e) {
            e.printStackTrace();
            return org.springframework.http.ResponseEntity.status(500).body("Error fetching bookings: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public boolean updateStatus(@PathVariable int id, @RequestBody String status) {
        return bookingDAO.updateBookingStatus(id, status.replace("\"", ""));
    }
}
