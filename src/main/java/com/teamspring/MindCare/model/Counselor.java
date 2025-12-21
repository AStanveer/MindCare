package com.teamspring.MindCare.model;
public class Counselor {

    private Long id;
    private String name;
    private String specialty;
    private double rating;
    private boolean available;

    public Counselor(Long id, String name, String specialty, double rating, boolean available) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
        this.available = available;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public double getRating() { return rating; }
    public boolean isAvailable() { return available; }
}
