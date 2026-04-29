package com.traffic.backend.repository;

import com.traffic.backend.model.Violation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ViolationRepository - Spring Data JPA repository for Violation entity
 */
@Repository
public interface ViolationRepository extends JpaRepository<Violation, Long> {

    /**
     * Find all violations for a specific license plate
     */
    List<Violation> findByLicensePlate(String licensePlate);

    /**
     * Find violations at a specific location
     */
    List<Violation> findByLocation(String location);

    /**
     * Find violations with excessive speed
     */
    @Query("SELECT v FROM Violation v WHERE v.excessSpeed > :threshold ORDER BY v.excessSpeed DESC")
    List<Violation> findExcessiveSpeedViolations(@Param("threshold") Double threshold);

    /**
     * Find violations above a certain fine amount
     */
    @Query("SELECT v FROM Violation v WHERE v.fineAmount >= :amount ORDER BY v.fineAmount DESC")
    List<Violation> findViolationsByMinimumFine(@Param("amount") Double amount);

    /**
     * Count violations for a specific license plate
     */
    long countByLicensePlate(String licensePlate);

    /**
     * Check if violation exists for license plate
     */
    boolean existsByLicensePlate(String licensePlate);

    /**
     * Find recent violations ordered by creation time (calling service should limit results)
     */
    @Query("SELECT v FROM Violation v ORDER BY v.createdAt DESC")
    List<Violation> findRecentViolations();
}
