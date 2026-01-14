package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "mood_entries")
public class MoodEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId = 1L; // Default dummy user
    
    @Column(name = "mood_level", nullable = false)
    private Integer moodLevel; // 1-5 scale
    
    @Column(name = "mood_label")
    private String moodLabel;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;
    
    @Column(name = "entry_time")
    private LocalTime entryTime;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public MoodEntry() {
        this.entryDate = LocalDate.now();
        this.entryTime = LocalTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        setMoodLabelFromLevel();
    }
    
    public MoodEntry(Integer moodLevel, String notes) {
        this();
        this.moodLevel = moodLevel;
        this.notes = notes;
        setMoodLabelFromLevel();
    }
    
    // Helper method to set label based on level
    private void setMoodLabelFromLevel() {
        if (moodLevel == null) return;
        
        switch (moodLevel) {
            case 1 -> this.moodLabel = "Very Low";
            case 2 -> this.moodLabel = "Low";
            case 3 -> this.moodLabel = "Fair";
            case 4 -> this.moodLabel = "Good";
            case 5 -> this.moodLabel = "Excellent";
            default -> this.moodLabel = "Unknown";
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Integer getMoodLevel() { return moodLevel; }
    public void setMoodLevel(Integer moodLevel) { 
        this.moodLevel = moodLevel;
        setMoodLabelFromLevel();
    }
    
    public String getMoodLabel() { return moodLabel; }
    public void setMoodLabel(String moodLabel) { this.moodLabel = moodLabel; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
    
    public LocalTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalTime entryTime) { this.entryTime = entryTime; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Helper methods for Thymeleaf
    public String getFormattedDate() {
        return entryDate.toString();
    }
    
    public String getFormattedTime() {
        return entryTime != null ? entryTime.toString().substring(0, 5) : "";
    }

    public String getDisplayDate() {
        return entryDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
    
    public String getMoodColor() {
        return switch (moodLevel) {
            case 1 -> "#ef4444"; // Red - Very Low
            case 2 -> "#f59e0b"; // Amber - Low
            case 3 -> "#eab308"; // Yellow - Fair
            case 4 -> "#22c55e"; // Green - Good
            case 5 -> "#10b981"; // Emerald - Excellent
            default -> "#64748b"; // Slate
        };
    }
    
    public String getMoodIcon() {
        return switch (moodLevel) {
            case 1 -> "/icons/verylow.png";
            case 2 -> "/icons/low.png";
            case 3 -> "/icons/neutral.png";
            case 4 -> "/icons/good.png";
            case 5 -> "/icons/excellent.png";
            default -> "/icons/neutral.png";
        };
    }
    
}