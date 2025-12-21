package com.teamspring.MindCare.model;

import jakarta.persistence.*;

@Entity
@Table(name = "counselors")
public class Counselor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String specialty;
    
    @Column(nullable = false)
    private double rating;
    
    @Column(nullable = false)
    private boolean available;

    public Counselor() {
    }

    public Counselor(Long id, String name, String specialty, double rating, boolean available) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
        this.available = available;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
