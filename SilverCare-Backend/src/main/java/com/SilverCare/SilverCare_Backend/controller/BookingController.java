package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.ActivityLogDAO;
import com.SilverCare.SilverCare_Backend.dbaccess.Booking;
import com.SilverCare.SilverCare_Backend.dbaccess.BookingDAO;
import com.SilverCare.SilverCare_Backend.dbaccess.Service;
import com.SilverCare.SilverCare_Backend.dbaccess.ServiceDAO;
import com.SilverCare.SilverCare_Backend.service.StripeService;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingDAO bookingDAO = new BookingDAO();
    private final ServiceDAO serviceDAO = new ServiceDAO();
    
    @Autowired
    private StripeService stripeService;

    @RequestMapping(
            path = "/create",
            consumes = "application/json",
            method = RequestMethod.POST)
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate date is in the future
            java.time.LocalDateTime scheduledDate = java.time.LocalDateTime.parse(
                booking.getScheduledDate().replace(" ", "T")
            );
            if (scheduledDate.isBefore(java.time.LocalDateTime.now())) {
                return ResponseEntity.status(400).body("Booking date must be in the future");
            }

            Service service = serviceDAO.getServiceById(booking.getServiceId());
            if (service == null) {
                return ResponseEntity.status(404).body("Service not found");
            }
            booking.setServiceName(service.getServiceName());
            booking.setPrice(service.getPrice());

            Session session = stripeService.createCheckoutSession(booking);
            response.clear();
            response.put("paymentUrl", session.getUrl());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating booking: " + e.getMessage());
        }
    }

    @RequestMapping(
            path = "/user/{userId}",
            method = RequestMethod.GET)
    public ResponseEntity<?> getBookingsByUser(@PathVariable int userId) {
        List<Booking> bookings = new ArrayList<>();
        try {
            bookings = bookingDAO.getBookingsByUserId(userId);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching bookings: " + e.getMessage());
        }
    }

    @RequestMapping(
            path = "/{id}/status",
            consumes = "application/json",
            method = RequestMethod.PUT)
    public boolean updateStatus(@PathVariable int id, @RequestBody String status) {
        boolean success = false;
        try {
            String cleanStatus = status.replace("\"", "");
            success = bookingDAO.updateBookingStatus(id, cleanStatus);
            if (success) {
                ActivityLogDAO.log(0, "UPDATE_BOOKING_STATUS", "Booking ID " + id + " status updated to: " + cleanStatus);
            }
        } catch (Exception e) {
            System.out.println("Error updating booking status: " + e.toString());
        }
        return success;
    }

    @RequestMapping(
            path = "/update",
            consumes = "application/json",
            method = RequestMethod.PUT)
    public ResponseEntity<String> updateBooking(@RequestBody Booking booking) {
        if (bookingDAO.updateBooking(booking)) {
            ActivityLogDAO.log(booking.getUserId(), "UPDATE_BOOKING", "Updated details for booking ID: " + booking.getId());
            return ResponseEntity.ok("Booking updated successfully");
        }
        return ResponseEntity.status(500).body("Failed to update booking");
    }

    @RequestMapping(
            path = "/all",
            method = RequestMethod.GET)
    public ResponseEntity<?> getAllBookings() {
        try {
            return ResponseEntity.ok(bookingDAO.getAllBookings());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching all bookings: " + e.getMessage());
        }
    }

    @RequestMapping(
            path = "/{id}",
            method = RequestMethod.GET)
    public ResponseEntity<?> getBookingById(@PathVariable int id) {
        Booking booking = bookingDAO.getBookingById(id);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        }
        return ResponseEntity.status(404).body("Booking not found");
    }

    @RequestMapping(
            path = "/{id}/checkin",
            method = RequestMethod.PUT)
    public ResponseEntity<String> checkIn(@PathVariable int id) {
        if (bookingDAO.checkIn(id)) {
            ActivityLogDAO.log(0, "CHECK_IN", "Caregiver checked in for booking ID: " + id);
            return ResponseEntity.ok("Checked in successfully");
        }
        return ResponseEntity.status(500).body("Failed to check in");
    }

    @RequestMapping(
            path = "/{id}/checkout",
            method = RequestMethod.PUT)
    public ResponseEntity<String> checkOut(@PathVariable int id) {
        if (bookingDAO.checkOut(id)) {
            ActivityLogDAO.log(0, "CHECK_OUT", "Caregiver checked out for booking ID: " + id);
            return ResponseEntity.ok("Checked out successfully");
        }
        return ResponseEntity.status(500).body("Failed to check out");
    }

    @RequestMapping(
            path = "/delete/{id}",
            method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBooking(@PathVariable int id) {
        if (bookingDAO.deleteBooking(id)) {
            ActivityLogDAO.log(0, "DELETE_BOOKING", "Booking ID " + id + " deleted from system");
            return ResponseEntity.ok("Booking deleted successfully");
        }
        return ResponseEntity.status(500).body("Failed to delete booking");
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<?> getMonthlyRevenue() {
        try {
            return ResponseEntity.ok(bookingDAO.getMonthlyRevenueTrend());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching revenue trend: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/top-clients")
    public ResponseEntity<?> getTopClients() {
        try {
            return ResponseEntity.ok(bookingDAO.getTopClients());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching top clients: " + e.getMessage());
        }
    }

    @GetMapping("/analytics/by-service/{serviceId}")
    public ResponseEntity<?> getUsersByService(@PathVariable int serviceId) {
        try {
            return ResponseEntity.ok(bookingDAO.getUsersByService(serviceId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching users by service: " + e.getMessage());
        }
    }
}
