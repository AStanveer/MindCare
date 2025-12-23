package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "availability")
public class Availability {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long counselorId;
    
    @Column(nullable = false)
    private LocalDate availableDate;
    
    @Column(nullable = false)
    private LocalTime timeSlot;
    
    @Column(nullable = false)
    private boolean isBooked = false;
    
    // Constructors
    public Availability() {}
    
    public Availability(Long counselorId, LocalDate availableDate, LocalTime timeSlot) {
        this.counselorId = counselorId;
        this.availableDate = availableDate;
        this.timeSlot = timeSlot;
        this.isBooked = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCounselorId() {
        return counselorId;
    }
    
    public void setCounselorId(Long counselorId) {
        this.counselorId = counselorId;
    }
    
    public LocalDate getAvailableDate() {
        return availableDate;
    }
    
    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }
    
    public LocalTime getTimeSlot() {
        return timeSlot;
    }
    
    public void setTimeSlot(LocalTime timeSlot) {
        this.timeSlot = timeSlot;
    }
    
    public boolean isBooked() {
        return isBooked;
    }
    
    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
