package com.teamspring.MindCare.model;

public class Resource {
    private String title;
    private String description;
    private String category;
    private String readTime;
    private String author;
    private String date;
    
    public Resource(String title, String description, String category, 
                   String readTime, String author, String date) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.readTime = readTime;
        this.author = author;
        this.date = date;
    }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getReadTime() { return readTime; }
    public void setReadTime(String readTime) { this.readTime = readTime; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}