package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.ActivityLogDAO;
import com.SilverCare.SilverCare_Backend.dbaccess.Feedback;
import com.SilverCare.SilverCare_Backend.dbaccess.FeedbackDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    @PostMapping("/save")
    public ResponseEntity<String> saveFeedback(@RequestBody Feedback feedback) {
        try {
            boolean success = feedbackDAO.saveFeedback(feedback);
            if (success) {
                ActivityLogDAO.log(feedback.getUserId(), "SAVE_FEEDBACK", "User submitted feedback for Booking ID: " + feedback.getBookingId());
                return ResponseEntity.ok("Feedback saved successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to save feedback");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public java.util.List<Feedback> getFeedbackByUser(@PathVariable int userId) {
        try {
            return feedbackDAO.getFeedbackByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }

    @GetMapping("/all")
    public java.util.List<Feedback> getAllFeedback() {
        return feedbackDAO.getAllFeedback();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable int id) {
        Feedback f = feedbackDAO.getFeedbackById(id);
        if (f != null) {
            return ResponseEntity.ok(f);
        }
        return ResponseEntity.status(404).body(null);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateFeedback(@RequestBody Feedback feedback) {
        if (feedbackDAO.saveFeedback(feedback)) {
            ActivityLogDAO.log(0, "UPDATE_FEEDBACK", "Admin updated feedback ID: " + feedback.getId());
            return ResponseEntity.ok("Feedback updated successfully");
        }
        return ResponseEntity.status(500).body("Failed to update feedback");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable int id) {
        if (feedbackDAO.deleteFeedback(id)) {
            ActivityLogDAO.log(0, "DELETE_FEEDBACK", "Admin deleted feedback ID: " + id);
            return ResponseEntity.ok("Feedback deleted successfully");
        }
        return ResponseEntity.status(500).body("Failed to delete feedback");
    }

    @GetMapping("/analytics/service-ratings")
    public java.util.List<java.util.Map<String, Object>> getServiceRatings() {
        return feedbackDAO.getServiceRatings();
    }

    @GetMapping("/analytics/caregiver-ratings")
    public java.util.List<java.util.Map<String, Object>> getCaregiverRatings() {
        return feedbackDAO.getCaregiverRatings();
    }
}
