package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "availability_dates")
public class AvailabilityDates {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "counselor_id")
    private Long counselorId;
    
    @Column(name = "counselor_name")
    private String counselorName;
    
    @ElementCollection
    @CollectionTable(name = "counselor_available_dates", joinColumns = @JoinColumn(name = "availability_id"))
    @Column(name = "available_date")
    private List<String> availableDates;

    public AvailabilityDates() {
    }

    public AvailabilityDates(Long counselorId, String counselorName, List<String> availableDates) {
        this.counselorId = counselorId;
        this.counselorName = counselorName;
        this.availableDates = availableDates;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCounselorId() { return counselorId; }
    public void setCounselorId(Long counselorId) { this.counselorId = counselorId; }
    
    public String getCounselorName() { return counselorName; }
    public void setCounselorName(String counselorName) { this.counselorName = counselorName; }
    
    public List<String> getAvailableDates() { return availableDates; }
    public void setAvailableDates(List<String> availableDates) { this.availableDates = availableDates; }
}
