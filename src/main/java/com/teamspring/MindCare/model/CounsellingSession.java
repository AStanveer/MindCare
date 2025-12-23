package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "counselling_sessions")
public class CounsellingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "counselor_id")
    private Long counselorId;
    
    @Column(name = "counselor_name")
    private String counselorName;
    
    @Column(name = "student_id")
    private Long studentId;
    
    @Column(name = "session_date")
    private LocalDate sessionDate;
    
    @Column(name = "session_time")
    private LocalTime sessionTime;
    
    @Column(name = "session_type")
    private String sessionType;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "notes")
    private String notes;

    public CounsellingSession() {
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
    
    public String getCounselorName() { 
        return counselorName; 
    }
    
    public void setCounselorName(String counselorName) { 
        this.counselorName = counselorName; 
    }
    
    public Long getStudentId() { 
        return studentId; 
    }
    
    public void setStudentId(Long studentId) { 
        this.studentId = studentId; 
    }
    
    public LocalDate getSessionDate() { 
        return sessionDate; 
    }
    
    public void setSessionDate(LocalDate sessionDate) { 
        this.sessionDate = sessionDate; 
    }
    
    public LocalTime getSessionTime() { 
        return sessionTime; 
    }
    
    public void setSessionTime(LocalTime sessionTime) { 
        this.sessionTime = sessionTime; 
    }
    
    public String getSessionType() { 
        return sessionType; 
    }
    
    public void setSessionType(String sessionType) { 
        this.sessionType = sessionType; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public String getNotes() { 
        return notes; 
    }
    
    public void setNotes(String notes) { 
        this.notes = notes; 
    }
}

