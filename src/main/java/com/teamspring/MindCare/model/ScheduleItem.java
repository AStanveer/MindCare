package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule_items")
public class ScheduleItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "student_name")
    private String studentName;
    
    @Column(name = "schedule_date")
    private LocalDateTime date;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "status")
    private String status;     // "Confirmed" or "Pending"
    
    @Column(name = "student_email")
    private String studentEmail;
    
    @Column(name = "student_phone")
    private String studentPhone;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ScheduleItem() {
        this.createdAt = LocalDateTime.now();
    }

    public ScheduleItem(String name, LocalDateTime date, String type, String status, String email, String phone, String reason, String notes) {
        this();
        this.studentName = name;
        this.date = date;
        this.type = type;
        this.status = status;
        this.studentEmail = email;
        this.studentPhone = phone;
        this.reason = reason;
        this.notes = notes;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }

    public String getStudentPhone() { return studentPhone; }
    public void setStudentPhone(String studentPhone) { this.studentPhone = studentPhone; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
