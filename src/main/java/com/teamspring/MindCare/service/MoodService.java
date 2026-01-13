package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.MoodEntry;
import com.teamspring.MindCare.repository.MoodEntryRepository;
import com.teamspring.MindCare.security.CurrentUserService;
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

    @Autowired
    private CurrentUserService currentUserService;

    public MoodEntry saveMoodEntry(Integer moodLevel, String notes) {
        Long userId = currentUserService.getUserId();

        List<MoodEntry> todayEntries =
                moodEntryRepository.findTodayEntriesByUserId(userId);

        MoodEntry moodEntry;

        if (!todayEntries.isEmpty()) {
            moodEntry = todayEntries.get(0);
            moodEntry.setMoodLevel(moodLevel);
            moodEntry.setNotes(notes);
            moodEntry.setUpdatedAt(LocalDateTime.now());
        } else {
            moodEntry = new MoodEntry(moodLevel, notes);
            moodEntry.setUserId(userId);
        }

        return moodEntryRepository.save(moodEntry);
    }

    public List<MoodEntry> getUserMoodHistory() {
        Long userId = currentUserService.getUserId();
        return moodEntryRepository.findByUserIdOrderByEntryDateDesc(userId);
    }

    public List<MoodEntry> getRecentMoodEntries() {
        Long userId = currentUserService.getUserId();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);

        return moodEntryRepository
                .findByUserIdAndEntryDateBetween(userId, startDate, endDate);
    }

    public MoodEntry getMoodEntryById(Long id) {
        return moodEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mood entry not found"));
    }

    public void deleteMoodEntry(Long id) {
        moodEntryRepository.deleteById(id);
    }

    public MoodStatistics getMoodStatistics() {
        Long userId = currentUserService.getUserId();

        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6);
        LocalDate monthAgo = today.minusDays(29);

        List<MoodEntry> weekEntries =
                moodEntryRepository.findByUserIdAndEntryDateBetween(userId, weekAgo, today);

        List<MoodEntry> monthEntries =
                moodEntryRepository.findByUserIdAndEntryDateBetween(userId, monthAgo, today);

        MoodStatistics stats = new MoodStatistics();

        if (!weekEntries.isEmpty()) {
            double weekAvg = weekEntries.stream()
                    .mapToInt(MoodEntry::getMoodLevel)
                    .average()
                    .orElse(0.0);

            stats.setWeeklyAverage(Math.round(weekAvg * 10.0) / 10.0);
            stats.setWeeklyEntries(weekEntries.size());
            stats.setCurrentStreak(calculateCurrentStreak(userId));
        }

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

    private int calculateCurrentStreak(Long userId) {
        List<MoodEntry> allEntries =
                moodEntryRepository.findByUserIdOrderByEntryDateDesc(userId);

        if (allEntries.isEmpty()) return 0;

        LocalDate currentDate = LocalDate.now();
        int streak = 0;

        for (MoodEntry entry : allEntries) {
            long daysBetween =
                    ChronoUnit.DAYS.between(entry.getEntryDate(), currentDate);

            if (daysBetween == streak) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

    public List<String> getMoodInsights() {
        List<String> insights = new ArrayList<>();
        MoodStatistics stats = getMoodStatistics();

        if (stats.getWeeklyEntries() > 0) {
            insights.add(
                "Your mood has been " +
                getMoodDescription(stats.getWeeklyAverage()) +
                " this week with an average score of " +
                stats.getWeeklyAverage()
            );
        }

        if (stats.getCurrentStreak() > 0) {
            insights.add(
                "You've logged your mood " +
                stats.getCurrentStreak() +
                " day" +
                (stats.getCurrentStreak() > 1 ? "s" : "") +
                " in a row"
            );
        }

        insights.add("Tracking your mood regularly helps identify patterns and triggers");
        insights.add("Morning entries tend to be most consistent for baseline mood");

        return insights;
    }

    private String getMoodDescription(double average) {
        if (average >= 4.0) return "positive";
        if (average >= 3.0) return "stable";
        if (average >= 2.0) return "challenging";
        return "difficult";
    }

    public static class MoodStatistics {
        private double weeklyAverage;
        private double monthlyAverage;
        private int weeklyEntries;
        private int monthlyEntries;
        private int currentStreak;

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