package com.SilverCare.SilverCare_Backend.controller;

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
        } catch (Exception e) {
            System.out.println("Error updating booking status: " + e.toString());
        }
        return success;
    }
}
