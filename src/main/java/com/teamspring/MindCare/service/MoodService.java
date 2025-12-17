package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.MoodEntry;
import com.teamspring.MindCare.repository.MoodEntryRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MoodService {
    
    @Autowired
    private MoodEntryRepository moodEntryRepository;
    
    private static final Long DUMMY_USER_ID = 1L;
    
    // Save or update today's mood
    public MoodEntry saveMoodEntry(Integer moodLevel, String notes) {
        // Check if user already has an entry for today
        List<MoodEntry> todayEntries = moodEntryRepository.findTodayEntriesByUserId(DUMMY_USER_ID);
        
        MoodEntry moodEntry;
        if (!todayEntries.isEmpty()) {
            // Update existing entry
            moodEntry = todayEntries.get(0);
            moodEntry.setMoodLevel(moodLevel);
            moodEntry.setNotes(notes);
            moodEntry.setUpdatedAt(LocalDateTime.now());
        } else {
            // Create new entry
            moodEntry = new MoodEntry(moodLevel, notes);
            moodEntry.setUserId(DUMMY_USER_ID);
        }
        
        return moodEntryRepository.save(moodEntry);
    }
    
    // Get user's mood history
    public List<MoodEntry> getUserMoodHistory() {
        return moodEntryRepository.findByUserIdOrderByEntryDateDesc(DUMMY_USER_ID);
    }
    
    // Get recent entries (last 7 days)
    public List<MoodEntry> getRecentMoodEntries() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6); // Last 7 days including today
        
        return moodEntryRepository.findByUserIdAndEntryDateBetween(DUMMY_USER_ID, startDate, endDate);
    }
    
    // Get entry by ID
    public MoodEntry getMoodEntryById(Long id) {
        return moodEntryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mood entry not found"));
    }
    
    // Delete entry
    public void deleteMoodEntry(Long id) {
        moodEntryRepository.deleteById(id);
    }
    
    // Get mood statistics
    public MoodStatistics getMoodStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6);
        LocalDate monthAgo = today.minusDays(29);
        
        List<MoodEntry> weekEntries = moodEntryRepository.findByUserIdAndEntryDateBetween(DUMMY_USER_ID, weekAgo, today);
        List<MoodEntry> monthEntries = moodEntryRepository.findByUserIdAndEntryDateBetween(DUMMY_USER_ID, monthAgo, today);
        
        MoodStatistics stats = new MoodStatistics();
        
        // Weekly stats
        if (!weekEntries.isEmpty()) {
            double weekAvg = weekEntries.stream()
                .mapToInt(MoodEntry::getMoodLevel)
                .average()
                .orElse(0.0);
            stats.setWeeklyAverage(Math.round(weekAvg * 10.0) / 10.0); // Round to 1 decimal
            
            // Count entries per mood level
            stats.setWeeklyEntries(weekEntries.size());
            
            // Streak calculation
            stats.setCurrentStreak(calculateCurrentStreak());
        }
        
        // Monthly stats
        if (!monthEntries.isEmpty()) {
            double monthAvg = monthEntries.stream()
                .mapToInt(MoodEntry::getMoodLevel)
                .average()
                .orElse(0.0);
            stats.setMonthlyAverage(Math.round(monthAvg * 10.0) / 10.0);
            
            stats.setMonthlyEntries(monthEntries.size());
        }
        
        return stats;
    }
    
    // Calculate current streak of consecutive days with entries
    private int calculateCurrentStreak() {
        List<MoodEntry> allEntries = moodEntryRepository.findByUserIdOrderByEntryDateDesc(DUMMY_USER_ID);
        if (allEntries.isEmpty()) return 0;
        
        LocalDate currentDate = LocalDate.now();
        int streak = 0;
        
        for (MoodEntry entry : allEntries) {
            long daysBetween = ChronoUnit.DAYS.between(entry.getEntryDate(), currentDate);
            
            if (daysBetween == streak) {
                streak++;
            } else {
                break;
            }
        }
        
        return streak;
    }
    
    // Get mood insights
    public List<String> getMoodInsights() {
        List<String> insights = new ArrayList<>();
        MoodStatistics stats = getMoodStatistics();
        
        if (stats.getWeeklyEntries() > 0) {
            insights.add("Your mood has been " + getMoodDescription(stats.getWeeklyAverage()) + " this week with an average score of " + stats.getWeeklyAverage());
        }
        
        if (stats.getCurrentStreak() > 0) {
            insights.add("You've logged your mood " + stats.getCurrentStreak() + " day" + (stats.getCurrentStreak() > 1 ? "s" : "") + " in a row - keep up the streak!");
        }
        
        // Add some generic insights
        insights.add("Tracking your mood regularly helps identify patterns and triggers");
        insights.add("Morning entries tend to be most consistent for establishing baseline mood");
        
        return insights;
    }
    
    private String getMoodDescription(double average) {
        if (average >= 4.0) return "positive";
        if (average >= 3.0) return "stable";
        if (average >= 2.0) return "challenging";
        return "difficult";
    }
    
    // Statistics DTO
    public static class MoodStatistics {
        private double weeklyAverage;
        private double monthlyAverage;
        private int weeklyEntries;
        private int monthlyEntries;
        private int currentStreak;
        
        // Getters and setters
        public double getWeeklyAverage() { return weeklyAverage; }
        public void setWeeklyAverage(double weeklyAverage) { this.weeklyAverage = weeklyAverage; }
        
        public double getMonthlyAverage() { return monthlyAverage; }
        public void setMonthlyAverage(double monthlyAverage) { this.monthlyAverage = monthlyAverage; }
        
        public int getWeeklyEntries() { return weeklyEntries; }
        public void setWeeklyEntries(int weeklyEntries) { this.weeklyEntries = weeklyEntries; }
        
        public int getMonthlyEntries() { return monthlyEntries; }
        public void setMonthlyEntries(int monthlyEntries) { this.monthlyEntries = monthlyEntries; }
        
        public int getCurrentStreak() { return currentStreak; }
        public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
    }
}