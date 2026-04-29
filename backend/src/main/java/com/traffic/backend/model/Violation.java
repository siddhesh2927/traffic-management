package com.traffic.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Violation - Entity representing a traffic violation
 */
@Entity
@Table(name = "violations")
public class Violation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "vehicle_id")
    private String licensePlate;
    
    @Column(name = "speed")
    private Double speed;
    
    @Column(name = "excess_speed")
    private Double excessSpeed;
    
    @Column(name = "zone")
    private String location;
    
    @Column(name = "fine")
    private Double fineAmount;
    
    @Column(name = "violation_time")
    private LocalDateTime createdAt;

    // Constructors
    public Violation() {
    }

    public Violation(String licensePlate, Double speed, Double excessSpeed, String location, Double fineAmount, LocalDateTime createdAt) {
        this.licensePlate = licensePlate;
        this.speed = speed;
        this.excessSpeed = excessSpeed;
        this.location = location;
        this.fineAmount = fineAmount;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getExcessSpeed() {
        return excessSpeed;
    }

    public void setExcessSpeed(Double excessSpeed) {
        this.excessSpeed = excessSpeed;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Violation{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", speed=" + speed +
                ", excessSpeed=" + excessSpeed +
                ", location='" + location + '\'' +
                ", fineAmount=" + fineAmount +
                ", createdAt=" + createdAt +
                '}';
    }
}