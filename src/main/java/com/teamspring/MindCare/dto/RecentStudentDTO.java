package com.teamspring.MindCare.dto;

import java.time.LocalDate;

public class RecentStudentDTO {
    private String name;
    private LocalDate lastSessionDate;
    private String status; 
    private String trend;

    public RecentStudentDTO(String name, LocalDate lastSessionDate, String status, String trend) {
        this.name = name;
        this.lastSessionDate = lastSessionDate;
        this.status = status;
        this.trend = trend;
    }

    // Getters
    public String getName() { return name; }
    public LocalDate getLastSessionDate() { return lastSessionDate; }
    public String getStatus() { return status; }
    public String getTrend() { return trend; }
}