package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    
    // Find all unbooked availability for a counselor
    List<Availability> findByCounselorIdAndIsBookedFalse(Long counselorId);
    
    // Find unbooked availability for a counselor on a specific date
    List<Availability> findByCounselorIdAndAvailableDateAndIsBookedFalse(Long counselorId, LocalDate date);
    
    // Get distinct available dates for a counselor
    @Query("SELECT DISTINCT a.availableDate FROM Availability a WHERE a.counselorId = ?1 AND a.isBooked = false ORDER BY a.availableDate")
    List<LocalDate> findDistinctDatesByCounselorId(Long counselorId);
    
    // Find specific unbooked slot
    Optional<Availability> findByCounselorIdAndAvailableDateAndTimeSlotAndIsBookedFalse(
            Long counselorId, LocalDate date, LocalTime timeSlot);
    
    // Delete all availability for a counselor on a specific date
    void deleteByCounselorIdAndAvailableDate(Long counselorId, LocalDate date);
    
    // Find all availability by counselor (booked and unbooked)
    List<Availability> findByCounselorId(Long counselorId);
}
