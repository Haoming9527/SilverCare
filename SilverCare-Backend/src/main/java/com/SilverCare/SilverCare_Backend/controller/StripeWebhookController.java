package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.Booking;
import com.SilverCare.SilverCare_Backend.dbaccess.BookingDAO;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final BookingDAO bookingDAO = new BookingDAO();

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e) {
            System.out.println("Webhook signature verification failed: " + e.getMessage());
            return ResponseEntity.status(400).body("Webhook error: " + e.getMessage());
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            
            if (session != null) {
                Map<String, String> metadata = session.getMetadata();
                
                try {
                    if (metadata != null && 
                        metadata.containsKey("user_id") && 
                        metadata.containsKey("service_id") && 
                        metadata.containsKey("scheduled_date")) {
                        Booking booking = new Booking();
                        booking.setUserId(Integer.parseInt(metadata.get("user_id")));
                        booking.setServiceId(Integer.parseInt(metadata.get("service_id")));
                        booking.setScheduledDate(metadata.get("scheduled_date"));
                        booking.setSpecificCaregiver(metadata.get("specific_caregiver"));
                        booking.setSpecialRequest(metadata.get("special_request"));
                        booking.setStatus("Pending");
                        booking.setStripeSessionId(session.getId());
                        
                        bookingDAO.createBooking(booking);
                    }
                } catch (Exception e) {
                    System.out.println("Error saving booking from webhook: " + e.getMessage());
                    e.printStackTrace();
                    return ResponseEntity.status(500).body("Error saving booking: " + e.getMessage());
                }
            }
        }

        return ResponseEntity.ok("Success");
    }
}
