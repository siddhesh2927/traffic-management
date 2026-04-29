package com.traffic.backend.controller;

import com.traffic.backend.model.Violation;
import com.traffic.backend.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ViolationController - Handles violation queries and retrieval
 */
@RestController
@RequestMapping("/api/violations")
public class ViolationController {

    @Autowired
    private TrafficService trafficService;

    /**
     * GET /api/violations - Get all violations
     * 
     * @return List of all violation records
     */
    @GetMapping
    public ResponseEntity<?> getAllViolations() {
        try {
            List<Violation> violations = trafficService.getAllViolations();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", violations.size());
            response.put("data", violations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error retrieving violations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/violations/{id} - Get violation by ID
     * 
     * @param id Violation ID
     * @return Specific violation record
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getViolationById(@PathVariable Long id) {
        try {
            Optional<Violation> violation = trafficService.getViolationById(id);
            if (violation.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("data", violation.get());
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("status", "not_found");
                error.put("message", "Violation not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error retrieving violation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/violations/plate/{licensePlate} - Get violations by license plate
     * 
     * @param licensePlate Vehicle license plate
     * @return List of violations for the plate
     */
    @GetMapping("/plate/{licensePlate}")
    public ResponseEntity<?> getViolationsByPlate(@PathVariable String licensePlate) {
        try {
            List<Violation> violations = trafficService.getViolationsByLicensePlate(licensePlate);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("licensePlate", licensePlate);
            response.put("count", violations.size());
            response.put("data", violations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error retrieving violations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/violations/location/{location} - Get violations by location
     * 
     * @param location Location name
     * @return List of violations at the location
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<?> getViolationsByLocation(@PathVariable String location) {
        try {
            List<Violation> violations = trafficService.getViolationsByLocation(location);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("location", location);
            response.put("count", violations.size());
            response.put("data", violations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error retrieving violations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/violations/excessive/{threshold} - Get excessive speed violations
     * 
     * @param threshold Minimum excess speed (km/h)
     * @return List of violations exceeding threshold
     */
    @GetMapping("/excessive/{threshold}")
    public ResponseEntity<?> getExcessiveSpeedViolations(@PathVariable Double threshold) {
        try {
            List<Violation> violations = trafficService.getExcessiveSpeedViolations(threshold);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("threshold", threshold + " km/h");
            response.put("count", violations.size());
            response.put("data", violations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error retrieving violations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/violations/fine/{amount} - Get violations with minimum fine
     * 
     * @param amount Minimum fine amount
     * @return List of violations with fine >= amount
     */
    @GetMapping("/fine/{amount}")
    public ResponseEntity<?> getViolationsByMinimumFine(@PathVariable Double amount) {
        try {
            List<Violation> violations = trafficService.getViolationsByMinimumFine(amount);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("minimumFine", amount);
            response.put("count", violations.size());
            response.put("data", violations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error retrieving violations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/violations/recent/{limit} - Get recent violations
     * 
     * @param limit Number of recent violations to retrieve
     * @return List of recent violations
     */
    @GetMapping("/recent/{limit}")
    public ResponseEntity<?> getRecentViolations(@PathVariable int limit) {
        try {
            List<Violation> violations = trafficService.getRecentViolations(limit);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("limit", limit);
            response.put("count", violations.size());
            response.put("data", violations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error retrieving violations: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/violations/stats - Get violation statistics
     * 
     * @return Statistics about all violations
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getViolationStats() {
        try {
            TrafficService.ViolationStats stats = trafficService.getViolationStats();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error retrieving statistics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * DELETE /api/violations/{id} - Delete a violation
     * 
     * @param id Violation ID to delete
     * @return Success or error message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteViolation(@PathVariable Long id) {
        try {
            Optional<Violation> violation = trafficService.getViolationById(id);
            if (violation.isPresent()) {
                trafficService.deleteViolation(id);
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Violation deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("status", "not_found");
                error.put("message", "Violation not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Error deleting violation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
