package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.ContactMessage;
import com.SilverCare.SilverCare_Backend.dbaccess.ContactDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactDAO contactDAO = new ContactDAO();

    @PostMapping("/save")
    public ResponseEntity<String> saveContactMessage(@RequestBody ContactMessage message) {
        try {
            boolean success = contactDAO.saveMessage(message);
            if (success) {
                return ResponseEntity.ok("Message sent successfully");
            } else {
                return ResponseEntity.status(500).body("Failed to send message");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public java.util.List<ContactMessage> getAllMessages() {
        return contactDAO.getAllMessages();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable int id) {
        if (contactDAO.deleteMessage(id)) {
            return ResponseEntity.ok("Message deleted successfully");
        }
        return ResponseEntity.status(500).body("Failed to delete message");
    }
}
