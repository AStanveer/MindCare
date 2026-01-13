package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "author_role")
    private String authorRole;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private String category; // Mental Health, Self-Care, Wellness, Personal Growth, Mindfulness, Academic

    @Column(name = "read_time")
    private String readTime; // "5 min read"

    private String author;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    // Comma-separated tags like "Stress,Coping,Techniques"
    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- Constructors ---

    public Resource() {
        this.createdAt = LocalDateTime.now();
    }

    public Resource(String title,
                    String description,
                    String content,
                    String category,
                    String readTime,
                    String author,
                    LocalDate publishDate,
                    String tags) {
        this();
        this.title = title;
        this.description = description;
        this.content = content;
        this.category = category;
        this.readTime = readTime;
        this.author = author;
        this.publishDate = publishDate;
        this.tags = tags;
    }

    // --- Lifecycle callbacks ---

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (publishDate == null) {
            publishDate = LocalDate.now();
        }
        if (author == null || author.trim().isEmpty()) {
            author = "Healthcare Professional";
        }
        if (authorRole == null || authorRole.trim().isEmpty()) {
            authorRole = "Professional";
        }
        if (readTime == null || readTime.trim().isEmpty()) {
            readTime = "5 min read";
        }
    }

    // --- Getters and setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorRole() {
        return authorRole;
    }

    public void setAuthorRole(String authorRole) {
        this.authorRole = authorRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getTags() {
        return tags;
    }

    // accepts a single String (e.g. "Stress,Coping,Techniques")
    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // --- Helper methods for Thymeleaf ---

    public String getFormattedDate() {
        return publishDate != null ? publishDate.toString() : "Recently";
    }

    /**
     * Color mapping aligned with new category chips:
     * Mental Health, Self-Care, Wellness, Personal Growth, Mindfulness, Academic.
     */
    public String getCategoryColor() {
        if (category == null) {
            return "#64748b"; // slate fallback
        }

        return switch (category.toLowerCase()) {
            case "mental health"   -> "#8c7ae6"; // purple (matches Content / chips)
            case "self-care"       -> "#f97316"; // orange
            case "wellness"        -> "#10b981"; // green
            case "personal growth" -> "#6366f1"; // indigo
            case "mindfulness"     -> "#8c7ae6"; // purple
            case "academic"        -> "#0ea5e9"; // blue
            default                -> "#64748b"; // slate fallback
        };
    }
}
