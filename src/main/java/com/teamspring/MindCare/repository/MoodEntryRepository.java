package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.MoodEntry;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, Long> {
    
    // Find by user ID, ordered by date (newest first)
    List<MoodEntry> findByUserIdOrderByEntryDateDesc(Long userId);
    
    // Find by user ID and specific date
    List<MoodEntry> findByUserIdAndEntryDate(Long userId, LocalDate entryDate);
    
    // Find entries within date range
    List<MoodEntry> findByUserIdAndEntryDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    // Get all distinct entry dates for a user
    @Query("SELECT DISTINCT e.entryDate FROM MoodEntry e WHERE e.userId = :userId ORDER BY e.entryDate DESC")
    List<LocalDate> findDistinctEntryDatesByUserId(Long userId);
    
    // Get today's entry for a user
    @Query("SELECT e FROM MoodEntry e WHERE e.userId = :userId AND e.entryDate = CURRENT_DATE")
    List<MoodEntry> findTodayEntriesByUserId(Long userId);
    
    // Get average mood for a period
    @Query("SELECT AVG(e.moodLevel) FROM MoodEntry e WHERE e.userId = :userId AND e.entryDate BETWEEN :startDate AND :endDate")
    Double findAverageMoodByUserIdAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
}