package com.traffic.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VehicleEvent - Request DTO for traffic violation events
 */
public class VehicleEvent {

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Speed is required")
    @Positive(message = "Speed must be positive")
    private Double speed;

    @NotNull(message = "Speed limit is required")
    @Positive(message = "Speed limit must be positive")
    private Double speedLimit;

    @NotBlank(message = "Location is required")
    private String location;

    @JsonProperty("timestamp")
    private Long timestamp;

    // Constructors
    public VehicleEvent() {
        this.timestamp = System.currentTimeMillis();
    }

    public VehicleEvent(String licensePlate, Double speed, Double speedLimit, String location) {
        this.licensePlate = licensePlate;
        this.speed = speed;
        this.speedLimit = speedLimit;
        this.location = location;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VehicleEvent{" +
                "licensePlate='" + licensePlate + '\'' +
                ", speed=" + speed +
                ", speedLimit=" + speedLimit +
                ", location='" + location + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
