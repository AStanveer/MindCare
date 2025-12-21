package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.Counselor;
import com.teamspring.MindCare.model.CounsellingSession;
import com.teamspring.MindCare.model.BookingRequest;
import com.teamspring.MindCare.model.AvailabilityDates;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class CounsellingService {

    public List<Counselor> getAllCounselors() {
        return List.of(
            new Counselor(1L, "Dr. Sarah Johnson", "Anxiety & Stress", 4.9, true),
            new Counselor(2L, "Dr. Michael Chen", "Depression & Mood", 4.8, true),
            new Counselor(3L, "Dr. Emily Roberts", "Academic Pressure", 4.9, false)
        );
    }

    public List<String> getTimeSlots() {
        return List.of(
            "9:00 AM", "10:00 AM", "11:00 AM",
            "1:00 PM", "2:00 PM", "3:00 PM",
            "4:00 PM", "5:00 PM"
        );
    }

    public List<CounsellingSession> getUpcomingSessions() {
        return List.of(
            new CounsellingSession(1L, "Dr. Sarah Johnson",
                LocalDateTime.of(2025, 12, 15, 14, 0), "Video", "Confirmed"),
            new CounsellingSession(2L, "Dr. Michael Chen",
                LocalDateTime.of(2025, 12, 18, 10, 0), "In-Person", "Confirmed"),
            new CounsellingSession(3L, "Dr. Emily Roberts",
                LocalDateTime.of(2025, 12, 20, 15, 30), "Video", "Pending"),
            new CounsellingSession(4L, "Dr. Sarah Johnson",
                LocalDateTime.of(2025, 12, 22, 11, 0), "In-Person", "Confirmed"),
            new CounsellingSession(5L, "Dr. Michael Chen",
                LocalDateTime.of(2025, 12, 25, 13, 0), "Video", "Confirmed")
        );
    }

    public Map<Long, AvailabilityDates> getAvailabilityDates() {
        Map<Long, AvailabilityDates> availabilityMap = new HashMap<>();
        
        availabilityMap.put(1L, new AvailabilityDates(1L, "Dr. Sarah Johnson",
            List.of("2025-12-15", "2025-12-17", "2025-12-19", "2025-12-22", "2025-12-24", "2025-12-26")));
        
        availabilityMap.put(2L, new AvailabilityDates(2L, "Dr. Michael Chen",
            List.of("2025-12-16", "2025-12-18", "2025-12-20", "2025-12-23", "2025-12-25", "2025-12-27")));
        
        availabilityMap.put(3L, new AvailabilityDates(3L, "Dr. Emily Roberts",
            List.of("2025-12-17", "2025-12-19", "2025-12-21", "2025-12-24", "2025-12-26", "2025-12-28")));
        
        return availabilityMap;
    }

    public void bookSession(BookingRequest request) {
        // In real app:
        // - Validate
        // - Save to DB
        // - Send notification
        System.out.println("Booking confirmed for counselor ID: " + request.getCounselorId());
    }
}
