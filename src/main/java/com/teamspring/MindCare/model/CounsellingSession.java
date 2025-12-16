package com.teamspring.MindCare.model;

import java.time.LocalDateTime;

public class CounsellingSession {
    private String doctorName;
    private LocalDateTime date;
    private String type;


    public CounsellingSession(String doctorName, LocalDateTime date, String type) {
        this.doctorName = doctorName;
        this.date = date;
        this.type = type;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
