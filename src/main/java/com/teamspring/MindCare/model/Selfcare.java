package com.teamspring.MindCare.model;

public class Selfcare {
    private String title;
    private String description;
    private String category; 
    private String duration; 
    private String author; 
    private String difficulty; 
    
    public Selfcare(String title, String description, String category, 
                           String duration, String author, String difficulty) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.duration = duration;
        this.author = author;
        this.difficulty = difficulty;
    }
    
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
}