package com.traffic.backend.controller;

import com.traffic.backend.model.VehicleEvent;
import com.traffic.backend.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * TrafficController - Handles traffic event processing endpoints
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class TrafficController {

    @Autowired
    private TrafficService service;

    /**
     * POST /api/events - Process a vehicle event
     */
    @PostMapping("/events")
    public ResponseEntity<?> processEvent(@Valid @RequestBody VehicleEvent event) {
        return service.processEvent(event)
            .map(v -> {
                Map<String, Object> response = new HashMap<>();
                response.put("violation", true);
                response.put("data", v);
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                Map<String, Object> response = new HashMap<>();
                response.put("violation", false);
                response.put("message", "No violation - speed within limit");
                return ResponseEntity.ok(response);
            });
    }

}