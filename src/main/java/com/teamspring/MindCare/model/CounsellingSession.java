package com.teamspring.MindCare.model;

import java.time.LocalDateTime;

public class CounsellingSession {

    private Long id;
    private String counselor;
    private LocalDateTime sessionDateTime;
    private String sessionType;
    private String status;

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

