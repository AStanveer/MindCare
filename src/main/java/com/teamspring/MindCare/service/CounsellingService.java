package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.*;
import com.teamspring.MindCare.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CounsellingService {

    private final CounsellingSessionRepository sessionRepository;
    private final CounselorRepository counselorRepository;
    private final BookingRequestRepository bookingRequestRepository;
    private final AvailabilityRepository availabilityRepository;

    public CounsellingService(
            CounsellingSessionRepository sessionRepository,
            CounselorRepository counselorRepository,
            BookingRequestRepository bookingRequestRepository,
            AvailabilityRepository availabilityRepository) {
        this.sessionRepository = sessionRepository;
        this.counselorRepository = counselorRepository;
        this.bookingRequestRepository = bookingRequestRepository;
        this.availabilityRepository = availabilityRepository;
        
        // Initialize default data if empty
        initializeDefaultData();
    }

    private void initializeDefaultData() {
        // Initialize counselors if none exist
        if (counselorRepository.count() == 0) {
            counselorRepository.saveAll(List.of(
                new Counselor(null, "Dr. Sarah Johnson", "Anxiety & Stress", 4.9, true),
                new Counselor(null, "Dr. Michael Chen", "Depression & Mood", 4.8, true),
                new Counselor(null, "Dr. Emily Roberts", "Academic Pressure", 4.9, true)
            ));
            System.out.println("✓ Initialized default counselors");
        }
        
        // Initialize some test sessions if none exist
        if (sessionRepository.count() == 0) {
            CounsellingSession session1 = new CounsellingSession();
            session1.setCounselorId(1L);
            session1.setCounselorName("Dr. Sarah Johnson");
            session1.setStudentId(1L);
            session1.setSessionDate(LocalDate.now().plusDays(1));
            session1.setSessionTime(LocalTime.of(10, 0));
            session1.setSessionType("Individual");
            session1.setStatus("Scheduled");
            session1.setNotes("Initial consultation");
            
            CounsellingSession session2 = new CounsellingSession();
            session2.setCounselorId(1L);
            session2.setCounselorName("Dr. Sarah Johnson");
            session2.setStudentId(2L);
            session2.setSessionDate(LocalDate.now().plusDays(2));
            session2.setSessionTime(LocalTime.of(14, 0));
            session2.setSessionType("Follow-up");
            session2.setStatus("Scheduled");
            session2.setNotes("Progress check");
            
            sessionRepository.saveAll(List.of(session1, session2));
            System.out.println("✓ Initialized test counselling sessions");
        }
    }

    // ===== COUNSELOR METHODS =====
    
    public List<Counselor> getAllCounselors() {
        return counselorRepository.findAll();
    }

    // ===== AVAILABILITY METHODS =====
    
    /**
     * Save availability for a counselor on a specific date
     */
    @Transactional
    public void saveAvailability(Long counselorId, String selectedDate, String[] timeSlots) {
        LocalDate date = LocalDate.parse(selectedDate);
        
        // Delete existing availability for this counselor on this date
        availabilityRepository.deleteByCounselorIdAndAvailableDate(counselorId, date);
        
        // Save new availability slots
        if (timeSlots != null && timeSlots.length > 0) {
            for (String timeSlotStr : timeSlots) {
                LocalTime time = LocalTime.parse(timeSlotStr);
                Availability availability = new Availability(counselorId, date, time);
                availabilityRepository.save(availability);
            }
            System.out.println("✓ Saved " + timeSlots.length + " time slots for date: " + selectedDate);
        } else {
            System.out.println("✓ Cleared availability for date: " + selectedDate);
        }
    }

    /**
     * Get available dates for a counselor (dates with unbooked slots)
     */
    public List<String> getAvailableDatesForCounselor(Long counselorId) {
        List<LocalDate> dates = availabilityRepository.findDistinctDatesByCounselorId(counselorId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dates.stream()
                .map(date -> date.format(formatter))
                .collect(Collectors.toList());
    }

    /**
     * Get available time slots for a counselor on a specific date
     */
    public List<String> getAvailableTimeSlotsForDate(Long counselorId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        List<Availability> availabilities = availabilityRepository
                .findByCounselorIdAndAvailableDateAndIsBookedFalse(counselorId, date);
        
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return availabilities.stream()
                .map(a -> a.getTimeSlot().format(timeFormatter))
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get availability dates for all counselors (for booking page)
     */
    public Map<Long, AvailabilityDates> getAvailabilityDates() {
        Map<Long, AvailabilityDates> availabilityMap = new HashMap<>();
        
        // Get all counselors
        List<Counselor> counselors = counselorRepository.findAll();
        
        for (Counselor counselor : counselors) {
            List<String> dates = getAvailableDatesForCounselor(counselor.getId());
            if (!dates.isEmpty()) {
                AvailabilityDates availability = new AvailabilityDates(
                    counselor.getId(),
                    counselor.getName(),
                    dates
                );
                availabilityMap.put(counselor.getId(), availability);
            }
        }
        
        return availabilityMap;
    }

    /**
     * Get all time slots (for availability setting UI)
     */
    public List<String> getTimeSlots() {
        return List.of(
            "09:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00"
        );
    }

    // ===== BOOKING METHODS =====
    
    /**
     * Book a counselling session
     */
    @Transactional
    public void bookSession(BookingRequest request) {
        // Validate request
        if (request == null || request.getCounselorId() == null) {
            throw new IllegalArgumentException("Invalid booking request - missing counselor ID");
        }
        
        if (request.getDate() == null || request.getDate().isEmpty()) {
            throw new IllegalArgumentException("Invalid booking request - missing date");
        }
        
        if (request.getTime() == null || request.getTime().isEmpty()) {
            throw new IllegalArgumentException("Invalid booking request - missing time");
        }
        
        try {
            LocalDate date = LocalDate.parse(request.getDate());
            LocalTime time = LocalTime.parse(request.getTime());
            
            // Find and mark availability as booked
            Optional<Availability> availability = availabilityRepository
                    .findByCounselorIdAndAvailableDateAndTimeSlotAndIsBookedFalse(
                            request.getCounselorId(), date, time);
            
            if (availability.isEmpty()) {
                throw new RuntimeException("Selected time slot is no longer available");
            }
            
            Availability avail = availability.get();
            avail.setBooked(true);
            availabilityRepository.save(avail);
            
            // Get counselor details
            Counselor counselor = counselorRepository.findById(request.getCounselorId())
                    .orElseThrow(() -> new RuntimeException("Counselor not found"));
            
            // Save booking request
            BookingRequest savedRequest = bookingRequestRepository.save(request);
            
            // Create session
            CounsellingSession session = new CounsellingSession();
            session.setStudentId(request.getStudentId() != null ? request.getStudentId() : 1L); // Default student ID
            session.setCounselorId(request.getCounselorId());
            session.setCounselorName(counselor.getName());
            session.setSessionDate(date);
            session.setSessionTime(time);
            session.setSessionType(request.getSessionType());
            session.setNotes(request.getNotes());
            session.setStatus("Scheduled");
            
            CounsellingSession savedSession = sessionRepository.save(session);
            
            // Log booking details
            System.out.println("\n" + "=".repeat(50));
            System.out.println("✓ BOOKING CONFIRMED");
            System.out.println("=".repeat(50));
            System.out.println("Session ID: " + savedSession.getId());
            System.out.println("Counselor: " + counselor.getName());
            System.out.println("Date: " + date);
            System.out.println("Time: " + time);
            System.out.println("Type: " + request.getSessionType());
            System.out.println("=".repeat(50) + "\n");
            
        } catch (Exception e) {
            System.err.println("✗ ERROR: Failed to book session - " + e.getMessage());
            throw new RuntimeException("Failed to book session: " + e.getMessage(), e);
        }
    }

    // ===== SESSION METHODS =====
    
    /**
     * Get all sessions for a counselor
     */
    public List<CounsellingSession> getCounselorSessions(Long counselorId) {
        List<CounsellingSession> sessions = sessionRepository.findByCounselorIdOrderBySessionDateAscSessionTimeAsc(counselorId);
        System.out.println("✓ Retrieved " + sessions.size() + " sessions for counselor ID: " + counselorId);
        return sessions;
    }

    /**
     * Get upcoming sessions for a student
     */
    public List<CounsellingSession> getUpcomingSessions() {
        // TODO: Filter by student ID from authenticated user
        return sessionRepository.findBySessionDateGreaterThanEqualOrderBySessionDateAscSessionTimeAsc(
                LocalDate.now());
    }
    
    /**
     * Get upcoming sessions for a specific student
     */
    public List<CounsellingSession> getUpcomingSessionsForStudent(Long studentId) {
        return sessionRepository.findByStudentIdOrderBySessionDateAscSessionTimeAsc(studentId)
                .stream()
                .filter(s -> !s.getSessionDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    /**
     * Reschedule a session
     */
    @Transactional
    public void rescheduleSession(Long sessionId, String newDate, String newTime) {
        CounsellingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        LocalDate date = LocalDate.parse(newDate);
        LocalTime time = LocalTime.parse(newTime);
        
        // Release old slot
        Optional<Availability> oldSlot = availabilityRepository
                .findByCounselorIdAndAvailableDateAndTimeSlotAndIsBookedFalse(
                        session.getCounselorId(), session.getSessionDate(), session.getSessionTime());
        oldSlot.ifPresent(avail -> {
            avail.setBooked(false);
            availabilityRepository.save(avail);
        });
        
        // Book new slot
        Optional<Availability> newSlot = availabilityRepository
                .findByCounselorIdAndAvailableDateAndTimeSlotAndIsBookedFalse(
                        session.getCounselorId(), date, time);
        
        if (newSlot.isEmpty()) {
            throw new RuntimeException("New time slot is not available");
        }
        
        Availability avail = newSlot.get();
        avail.setBooked(true);
        availabilityRepository.save(avail);
        
        // Update session
        session.setSessionDate(date);
        session.setSessionTime(time);
        sessionRepository.save(session);
        
        System.out.println("✓ Session " + sessionId + " rescheduled to " + newDate + " at " + newTime);
    }

    /**
     * Cancel a session
     */
    @Transactional
    public void cancelSession(Long sessionId) {
        CounsellingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        
        // Release the slot
        Optional<Availability> slot = availabilityRepository
                .findByCounselorIdAndAvailableDateAndTimeSlotAndIsBookedFalse(
                        session.getCounselorId(), session.getSessionDate(), session.getSessionTime());
        slot.ifPresent(avail -> {
            avail.setBooked(false);
            availabilityRepository.save(avail);
        });
        
        // Delete session
        sessionRepository.delete(session);
        
        System.out.println("✓ Session " + sessionId + " cancelled");
    }
}
