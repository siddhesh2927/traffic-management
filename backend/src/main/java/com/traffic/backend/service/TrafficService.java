package com.traffic.backend.service;

import com.traffic.backend.model.Violation;
import com.traffic.backend.model.VehicleEvent;
import com.traffic.backend.repository.ViolationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * TrafficService - Service layer for traffic violation processing
 */
@Service
public class TrafficService {
    private static final double SPEED_LIMIT = 80.0;
    private static final double EXCESSIVE_SPEED_THRESHOLD = 20.0;
    private static final double BASE_FINE = 1000.0;

    @Autowired
    private ViolationRepository violationRepository;

    /**
     * Process incoming vehicle event and create violation if speeding
     */
    public Optional<Violation> processEvent(VehicleEvent event) {
        if (event.getSpeed() <= event.getSpeedLimit()) {
            return Optional.empty();
        }

        double excessSpeed = event.getSpeed() - event.getSpeedLimit();
        double fineAmount = calculateFine(excessSpeed);

        Violation violation = new Violation(
            event.getLicensePlate(),
            event.getSpeed(),
            excessSpeed,
            event.getLocation(),
            fineAmount,
            LocalDateTime.now()
        );
        
        return Optional.of(violationRepository.save(violation));
    }

    /**
     * Calculate fine based on excess speed
     */
    private double calculateFine(double excessSpeed) {
        if (excessSpeed > 40) return BASE_FINE * 5;
        if (excessSpeed > 30) return BASE_FINE * 3;
        if (excessSpeed > 20) return BASE_FINE * 2;
        return BASE_FINE;
    }

    /**
     * Get all violations
     */
    public List<Violation> getAllViolations() {
        return violationRepository.findAll();
    }

    /**
     * Get violation by ID
     */
    public Optional<Violation> getViolationById(Long id) {
        return violationRepository.findById(id);
    }

    /**
     * Get violations by license plate
     */
    public List<Violation> getViolationsByLicensePlate(String licensePlate) {
        return violationRepository.findByLicensePlate(licensePlate);
    }

    /**
     * Get violations by location
     */
    public List<Violation> getViolationsByLocation(String location) {
        return violationRepository.findByLocation(location);
    }

    /**
     * Get excessive speed violations
     */
    public List<Violation> getExcessiveSpeedViolations(Double threshold) {
        return violationRepository.findExcessiveSpeedViolations(threshold);
    }

    /**
     * Get violations by minimum fine
     */
    public List<Violation> getViolationsByMinimumFine(Double amount) {
        return violationRepository.findViolationsByMinimumFine(amount);
    }

    /**
     * Get recent violations
     */
    public List<Violation> getRecentViolations(int limit) {
        return violationRepository.findRecentViolations(PageRequest.of(0, limit));
    }

    /**
     * Delete a violation
     */
    public void deleteViolation(Long id) {
        violationRepository.deleteById(id);
    }

    /**
     * Get violation statistics
     */
    public ViolationStats getViolationStats() {
        List<Violation> allViolations = violationRepository.findAll();
        ViolationStats stats = new ViolationStats();
        
        stats.totalViolations = (long) allViolations.size();
        stats.totalFineCollected = allViolations.stream()
            .filter(v -> v.getFineAmount() != null)
            .mapToDouble(Violation::getFineAmount)
            .sum();
        stats.averageFine = stats.totalViolations > 0 
            ? stats.totalFineCollected / stats.totalViolations 
            : 0;
        stats.maxExcessSpeed = allViolations.stream()
            .filter(v -> v.getExcessSpeed() != null)
            .mapToDouble(Violation::getExcessSpeed)
            .max()
            .orElse(0);
        stats.avgExcessSpeed = allViolations.stream()
            .filter(v -> v.getExcessSpeed() != null)
            .mapToDouble(Violation::getExcessSpeed)
            .average()
            .orElse(0);
        
        return stats;
    }

    /**
     * Statistics class for violation data
     */
    public static class ViolationStats {
        public long totalViolations;
        public double totalFineCollected;
        public double averageFine;
        public double maxExcessSpeed;
        public double avgExcessSpeed;

        public ViolationStats() {
        }

        public long getTotalViolations() { return totalViolations; }
        public double getTotalFineCollected() { return totalFineCollected; }
        public double getAverageFine() { return averageFine; }
        public double getMaxExcessSpeed() { return maxExcessSpeed; }
        public double getAvgExcessSpeed() { return avgExcessSpeed; }
    }
}