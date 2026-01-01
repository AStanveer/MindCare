package com.teamspring.MindCare.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SessionDTO {
    // Session Info
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String type;
    private String status;
    private String notes; 
    private String reason;

    // Student Info
    private String studentName;
    private String studentEmail;
    private String studentPhone;

    public SessionDTO(Long id, LocalDate date, LocalTime time, String type, String status, String notes,
                             String studentName, String studentEmail) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.status = status;
        this.notes = notes;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentPhone = "(555) 123-4567"; // Placeholder
        this.reason = "General Check-up";     // Placeholder
    }

    // Getters
    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
    public String getStudentName() { return studentName; }
    public String getStudentEmail() { return studentEmail; }
    public String getStudentPhone() { return studentPhone; }
    public String getReason() { return reason; }
    
    // Helper for Thymeleaf
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time);
    }
}