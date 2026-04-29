package com.traffic.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> status() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Traffic Management Backend is running");
        response.put("api_endpoints", "/api/events, /api/violations");
        return response;
    }
}
