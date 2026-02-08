package com.SilverCare.SilverCare_Backend.controller;

import com.SilverCare.SilverCare_Backend.dbaccess.ActivityLogDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityLogController {

    private final ActivityLogDAO logDAO = new ActivityLogDAO();

    @GetMapping("/all")
    public ResponseEntity<?> getAllActivityLogs() {
        try {
            List<Map<String, Object>> logs = logDAO.getAllLogs();
            return ResponseEntity.ok(logs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching logs: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLog(@PathVariable int id) {
        if (logDAO.deleteLog(id)) {
            return ResponseEntity.ok("Log deleted successfully");
        }
        return ResponseEntity.status(500).body("Failed to delete log");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearLogs() {
        if (logDAO.deleteAllLogs()) {
            return ResponseEntity.ok("All logs cleared successfully");
        }
        return ResponseEntity.status(500).body("Failed to clear logs");
    }
}
