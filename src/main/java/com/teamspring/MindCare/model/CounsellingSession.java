package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "counselling_sessions")
public class CounsellingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "counselor")
    private String counselor;
    
    @Column(name = "session_date_time")
    private LocalDateTime sessionDateTime;
    
    @Column(name = "session_type")
    private String sessionType;
    
    @Column(name = "status")
    private String status;

    public CounsellingSession() {
    }

    public CounsellingSession(Long id, String counselor, LocalDateTime sessionDateTime, String sessionType, String status) {
        this.id = id;
        this.counselor = counselor;
        this.sessionDateTime = sessionDateTime;
        this.sessionType = sessionType;
        this.status = status;
    }

    public LocalDateTime getSessionDateTime() {
        return sessionDateTime;
    }

    public void setSessionDateTime(LocalDateTime sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }

    public Long getId() { return id; }
    public String getCounselor() { return counselor; }
    public String getSessionType() { return sessionType; }
    public String getStatus() { return status; }
}

