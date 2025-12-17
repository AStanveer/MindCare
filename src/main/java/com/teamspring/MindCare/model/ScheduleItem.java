package com.teamspring.MindCare.model;

import java.time.LocalDateTime;

public class ScheduleItem {
    private String studentName;
    private LocalDateTime date;
    private String type;
    private String status;     // "Confirmed" or "Pending"
    private String studentEmail;
    private String studentPhone;
    private String reason;
    private String notes;

        // Constructor, Getters, and Setters...
    public ScheduleItem(String name, LocalDateTime date, String type, String status, String email, String phone, String reason, String notes) {
        this.studentName = name;
        this.date = date;
        this.type = type;
        this.status = status;
        this.studentEmail = email;
        this.studentPhone = phone;
        this.reason = reason;
        this.notes = notes;
    }
        // ... generate getters for all fields
    public String getStudentName() {
        return studentName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStudentPhone() {
        return studentPhone;
    }

    public String getReason() {
        return reason;
    }

    public String getNotes() {
        return notes;
    }
}
