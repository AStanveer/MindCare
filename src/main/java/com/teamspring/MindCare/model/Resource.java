package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    
    @Column(nullable = false)
    private String category; // Anxiety, Stress, Depression, Sleep, etc.
    
    @Column(name = "read_time")
    private String readTime; // "5 min read"
    
    private String author;
    
    @Column(name = "author_role")
    private String authorRole;
    
    @Column(name = "publish_date")
    private LocalDate publishDate;
    
    @Column(name = "views_count")
    private int viewsCount = 0;
    
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    
    private boolean featured = false;
    
    // Constructors
    public Resource() {
        this.createdAt = java.time.LocalDateTime.now();
    }
    
    public Resource(String title, String description, String content, String category, 
                   String readTime, String author, String authorRole, LocalDate publishDate) {
        this();
        this.title = title;
        this.description = description;
        this.content = content;
        this.category = category;
        this.readTime = readTime;
        this.author = author;
        this.authorRole = authorRole;
        this.publishDate = publishDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getReadTime() { return readTime; }
    public void setReadTime(String readTime) { this.readTime = readTime; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getAuthorRole() { return authorRole; }
    public void setAuthorRole(String authorRole) { this.authorRole = authorRole; }
    
    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }
    
    public int getViewsCount() { return viewsCount; }
    public void incrementViews() { this.viewsCount++; }
    
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    
    // Helper methods for Thymeleaf
    public String getFormattedDate() {
        return publishDate != null ? publishDate.toString() : "Recently";
    }
    
    public String getCategoryColor() {
        return switch (category.toLowerCase()) {
            case "anxiety" -> "#f59e0b"; // Amber
            case "stress" -> "#ef4444";  // Red
            case "depression" -> "#8c7ae6"; // Purple
            case "sleep" -> "#3b82f6";   // Blue
            default -> "#64748b";        // Slate
        };
    }
}