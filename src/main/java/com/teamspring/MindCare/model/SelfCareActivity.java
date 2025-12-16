package com.teamspring.MindCare.model;

import jakarta.persistence.*;

@Entity
@Table(name = "selfcare_activities")
public class SelfCareActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "content_type")
    private String contentType; // "VIDEO", "EXERCISE", "GUIDED_MEDITATION", "AUDIO"
    
    @Column(nullable = false)
    private String category; // breathing, meditation, yoga, journaling, exercise
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    private String difficulty; // "Beginner", "Intermediate", "Advanced"
    
    @Column(name = "video_url", length = 500)
    private String videoUrl;
    
    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;
    
    @Column(columnDefinition = "TEXT")
    private String instructions;
    
    @Column(columnDefinition = "TEXT")
    private String benefits;
    
    @Column(name = "created_by")
    private String createdBy = "Healthcare Professional";
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    private boolean featured = false;
    
    // Constructors
    public SelfCareActivity() {
        this.createdAt = java.time.LocalDateTime.now();
    }
    
    public SelfCareActivity(String title, String description, String contentType, 
                           String category, Integer durationMinutes, String difficulty) {
        this();
        this.title = title;
        this.description = description;
        this.contentType = contentType;
        this.category = category;
        this.durationMinutes = durationMinutes;
        this.difficulty = difficulty;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    
    // Helper methods for Thymeleaf
    public String getFormattedDuration() {
        if (durationMinutes == null) return "Flexible";
        if (durationMinutes < 60) return durationMinutes + " min";
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        return hours + " hr" + (minutes > 0 ? " " + minutes + " min" : "");
    }
    
    public String getDifficultyColor() {
        return switch (difficulty.toLowerCase()) {
            case "beginner" -> "#10b981"; // Green
            case "intermediate" -> "#f59e0b"; // Amber
            case "advanced" -> "#ef4444"; // Red
            default -> "#64748b";
        };
    }
    
    public String getContentTypeIcon() {
        return switch (contentType.toUpperCase()) {
            case "VIDEO" -> "🎬";
            case "AUDIO" -> "🎧";
            case "EXERCISE" -> "🏃‍♀️";
            case "GUIDED_MEDITATION" -> "🧘";
            default -> "📋";
        };
    }
}