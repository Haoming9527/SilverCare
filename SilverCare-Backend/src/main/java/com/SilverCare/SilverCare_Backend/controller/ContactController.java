package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.ActivityLogDAO;
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
                ActivityLogDAO.log(0, "SEND_CONTACT_MESSAGE", "New contact message from: " + message.getEmail());
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

    @GetMapping("/{id}")
    public ResponseEntity<ContactMessage> getMessageById(@PathVariable int id) {
        ContactMessage msg = contactDAO.getMessageById(id);
        if (msg != null) {
            return ResponseEntity.ok(msg);
        }
        return ResponseEntity.status(404).body(null);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMessage(@RequestBody ContactMessage message) {
        if (contactDAO.updateMessage(message)) {
            ActivityLogDAO.log(0, "UPDATE_CONTACT_MESSAGE", "Admin updated contact message ID: " + message.getId());
            return ResponseEntity.ok("Message updated successfully");
        }
        return ResponseEntity.status(500).body("Failed to update message");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable int id) {
        if (contactDAO.deleteMessage(id)) {
            ActivityLogDAO.log(0, "DELETE_CONTACT_MESSAGE", "Admin deleted contact message ID: " + id);
            return ResponseEntity.ok("Message deleted successfully");
        }
        return ResponseEntity.status(500).body("Failed to delete message");
    }
}
