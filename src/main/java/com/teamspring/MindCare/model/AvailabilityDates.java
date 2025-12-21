package com.teamspring.MindCare.model;

import java.util.List;

public class AvailabilityDates {
    private Long counselorId;
    private String counselorName;
    private List<String> availableDates;

    public AvailabilityDates(Long counselorId, String counselorName, List<String> availableDates) {
        this.counselorId = counselorId;
        this.counselorName = counselorName;
        this.availableDates = availableDates;
    }

    public Long getCounselorId() { return counselorId; }
    public String getCounselorName() { return counselorName; }
    public List<String> getAvailableDates() { return availableDates; }
}
