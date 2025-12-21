package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.Counselor;
import com.teamspring.MindCare.model.CounsellingSession;
import com.teamspring.MindCare.model.BookingRequest;
import com.teamspring.MindCare.model.AvailabilityDates;
import com.teamspring.MindCare.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class CounsellingService {

    private final CounsellingSessionRepository counsellingSessionRepository;
    private final CounselorRepository counselorRepository;
    private final BookingRequestRepository bookingRequestRepository;
    private final AvailabilityDatesRepository availabilityDatesRepository;
    private final ScheduleItemRepository scheduleItemRepository;

    public CounsellingService(
            CounsellingSessionRepository counsellingSessionRepository,
            CounselorRepository counselorRepository,
            BookingRequestRepository bookingRequestRepository,
            AvailabilityDatesRepository availabilityDatesRepository,
            ScheduleItemRepository scheduleItemRepository) {
        this.counsellingSessionRepository = counsellingSessionRepository;
        this.counselorRepository = counselorRepository;
        this.bookingRequestRepository = bookingRequestRepository;
        this.availabilityDatesRepository = availabilityDatesRepository;
        this.scheduleItemRepository = scheduleItemRepository;
        
        // Initialize default data if empty
        initializeDefaultData();
    }

    private void initializeDefaultData() {
        // Initialize counselors if none exist
        if (counselorRepository.count() == 0) {
            counselorRepository.saveAll(List.of(
                new Counselor(null, "Dr. Sarah Johnson", "Anxiety & Stress", 4.9, true),
                new Counselor(null, "Dr. Michael Chen", "Depression & Mood", 4.8, true),
                new Counselor(null, "Dr. Emily Roberts", "Academic Pressure", 4.9, false)
            ));
            System.out.println("✓ Initialized default counselors");
        }
    }

    public List<Counselor> getAllCounselors() {
        List<Counselor> counselors = counselorRepository.findAll();
        if (counselors.isEmpty()) {
            System.out.println("⚠ No counselors found in database, returning empty list");
        }
        return counselors;
    }

    public List<String> getTimeSlots() {
        return List.of(
            "9:00 AM", "10:00 AM", "11:00 AM",
            "1:00 PM", "2:00 PM", "3:00 PM",
            "4:00 PM", "5:00 PM"
        );
    }

    public List<CounsellingSession> getUpcomingSessions() {
        return counsellingSessionRepository.findAll()
            .stream()
            .filter(s -> s.getSessionDateTime().isAfter(LocalDateTime.now()))
            .sorted((a, b) -> a.getSessionDateTime().compareTo(b.getSessionDateTime()))
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getCounselorSessions(Long counselorId) {
        return counsellingSessionRepository.findAll()
            .stream()
            .filter(s -> s.getCounselor() != null)
            .map(session -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", session.getId());
                map.put("clientName", "Client");
                map.put("date", session.getSessionDateTime().toLocalDate().toString());
                map.put("time", session.getSessionDateTime().toLocalTime().toString());
                map.put("sessionType", session.getSessionType());
                map.put("status", session.getStatus());
                return map;
            })
            .collect(Collectors.toList());
    }

    public Map<Long, AvailabilityDates> getAvailabilityDates() {
        Map<Long, AvailabilityDates> availabilityMap = new HashMap<>();
        
        availabilityDatesRepository.findAll().forEach(availability -> {
            availabilityMap.put(availability.getCounselorId(), availability);
        });
        
        // If no availability dates in database, create defaults
        if (availabilityMap.isEmpty()) {
            availabilityMap.put(1L, new AvailabilityDates(1L, "Dr. Sarah Johnson",
                List.of("2025-12-15", "2025-12-17", "2025-12-19", "2025-12-22", "2025-12-24", "2025-12-26")));
            availabilityMap.put(2L, new AvailabilityDates(2L, "Dr. Michael Chen",
                List.of("2025-12-16", "2025-12-18", "2025-12-20", "2025-12-23", "2025-12-25", "2025-12-27")));
            availabilityMap.put(3L, new AvailabilityDates(3L, "Dr. Emily Roberts",
                List.of("2025-12-17", "2025-12-19", "2025-12-21", "2025-12-24", "2025-12-26", "2025-12-28")));
        }
        
        return availabilityMap;
    }

    public void bookSession(BookingRequest request) {
        // Validate request
        if (request == null || request.getCounselorId() == null) {
            System.err.println("ERROR: Invalid booking request - missing counselor ID");
            return;
        }
        
        if (request.getDate() == null || request.getDate().isEmpty()) {
            System.err.println("ERROR: Invalid booking request - missing date");
            return;
        }
        
        if (request.getTime() == null || request.getTime().isEmpty()) {
            System.err.println("ERROR: Invalid booking request - missing time");
            return;
        }
        
        if (request.getSessionType() == null || request.getSessionType().isEmpty()) {
            System.err.println("ERROR: Invalid booking request - missing session type");
            return;
        }
        
        try {
            // Get counselor from database
            Counselor counselor = counselorRepository.findById(request.getCounselorId())
                .orElse(null);
            
            String counselorName = counselor != null ? counselor.getName() : "Unknown Counselor";
            
            // Save booking request to database
            BookingRequest savedRequest = bookingRequestRepository.save(request);
            
            // Create session from booking
            LocalDateTime sessionDateTime = LocalDateTime.now().plusDays(1);
            CounsellingSession session = new CounsellingSession(
                null,
                counselorName,
                sessionDateTime,
                request.getSessionType(),
                "Confirmed"
            );
            
            CounsellingSession savedSession = counsellingSessionRepository.save(session);
            
            // Log booking details
            System.out.println("\n" + "=".repeat(50));
            System.out.println("✓ BOOKING CONFIRMED & SAVED TO DATABASE");
            System.out.println("=".repeat(50));
            System.out.println("Session ID: " + savedSession.getId());
            System.out.println("Booking ID: " + savedRequest.getId());
            System.out.println("Counselor: " + counselorName + " (ID: " + request.getCounselorId() + ")");
            System.out.println("Date: " + request.getDate());
            System.out.println("Time: " + request.getTime());
            System.out.println("Type: " + request.getSessionType());
            System.out.println("Status: Confirmed");
            System.out.println("=".repeat(50) + "\n");
            
        } catch (Exception e) {
            System.err.println("✗ ERROR: Failed to save booking - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
