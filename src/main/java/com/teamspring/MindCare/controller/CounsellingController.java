package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.BookingRequest;
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.repository.UserRepository;
import com.teamspring.MindCare.service.CounsellingService;
import com.teamspring.MindCare.service.FeatureUsageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Controller
@RequestMapping("/mindcare/counselling")
public class CounsellingController {

    private final CounsellingService counsellingService;
    private final FeatureUsageService featureUsageService;
    private final UserRepository userRepository;
    private static final Long DUMMY_USER_ID = 1L;

    public CounsellingController(CounsellingService counsellingService, FeatureUsageService featureUsageService, UserRepository userRepository) {
        this.counsellingService = counsellingService;
        this.featureUsageService = featureUsageService;
        this.userRepository = userRepository;
    }

    /**
     * Get the current authenticated user's ID
     * Falls back to DUMMY_USER_ID if not authenticated
     */
    private Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    return user.getId();
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error getting current user ID: " + e.getMessage());
        }
        return DUMMY_USER_ID;
    }

    // ===== Show Counselling Home Page =====
    @GetMapping
    public String counsellingHome(Model model) {
        return "redirect:/mindcare/counselling/booking";
    }

    // ===== Show Booking Page =====
    @GetMapping("/booking")
    public String counsellingPage(Model model) {
        Long userId = getCurrentUserId();
        // Track feature usage when user accesses counselling booking
        featureUsageService.incrementCounsellingUsage(userId);
        
        model.addAttribute("userRole", "student");
        model.addAttribute("counselors", counsellingService.getAllCounselors());
        model.addAttribute("timeSlots", counsellingService.getTimeSlots());
        model.addAttribute("upcomingSessions", counsellingService.getUpcomingSessions());
        model.addAttribute("availabilityDates", counsellingService.getAvailabilityDates());
        model.addAttribute("bookingRequest", new BookingRequest());
        
        return "counselling/booking";
    }

    // ===== Show HP Set Availability Page =====
    @GetMapping("/set-availability")
    public String setAvailability(Model model) {
        Long counselorId = getCurrentUserId();
        model.addAttribute("counselorId", counselorId);
        model.addAttribute("timeSlots", counsellingService.getTimeSlots());
        return "counselling/hp-setavailability";
    }

    // ===== Show HP My Schedule Page =====
    @GetMapping("/my-schedule")
    public String mySchedule(Model model) {
        Long counselorId = getCurrentUserId();
        List<com.teamspring.MindCare.model.CounsellingSession> sessions = counsellingService.getCounselorSessions(counselorId);
        
        System.out.println("=== MY SCHEDULE PAGE ===");
        System.out.println("Counselor ID: " + counselorId);
        System.out.println("Sessions found: " + sessions.size());
        sessions.forEach(s -> System.out.println("  - Session " + s.getId() + ": " + s.getSessionDate() + " at " + s.getSessionTime()));
        System.out.println("======================");
        
        // Enrich sessions with student details
        List<Map<String, Object>> enrichedSessions = new ArrayList<>();
        for (com.teamspring.MindCare.model.CounsellingSession session : sessions) {
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("id", session.getId());
            sessionData.put("studentId", session.getStudentId());
            sessionData.put("sessionDate", session.getSessionDate());
            sessionData.put("sessionTime", session.getSessionTime());
            sessionData.put("sessionType", session.getSessionType());
            sessionData.put("status", session.getStatus());
            sessionData.put("notes", session.getNotes());
            
            // Fetch student user details
            com.teamspring.MindCare.model.User student = userRepository.findById(session.getStudentId()).orElse(null);
            if (student != null) {
                sessionData.put("studentName", student.getFullName());
                sessionData.put("studentEmail", student.getEmail());
                sessionData.put("studentPhone", student.getPhone());
            } else {
                sessionData.put("studentName", "Unknown");
                sessionData.put("studentEmail", "N/A");
                sessionData.put("studentPhone", "N/A");
            }
            
            enrichedSessions.add(sessionData);
        }
        
        model.addAttribute("counselorId", counselorId);
        model.addAttribute("sessions", enrichedSessions);
        return "counselling/hp-myschedule";
    }

    // ===== Show My Sessions Page =====
    @GetMapping("/my-sessions")
    public String mySessions(Model model) {
        Long userId = getCurrentUserId();
        // Track feature usage when user accesses their sessions
        featureUsageService.incrementCounsellingUsage(userId);
        
        // Get only sessions for the current student (filtered by student ID)
        List<com.teamspring.MindCare.model.CounsellingSession> sessions = counsellingService.getUpcomingSessionsForStudent(userId);
        
        // Enrich sessions with counselor details
        List<Map<String, Object>> enrichedSessions = new ArrayList<>();
        for (com.teamspring.MindCare.model.CounsellingSession session : sessions) {
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("id", session.getId());
            sessionData.put("counselorId", session.getCounselorId());
            sessionData.put("counselorName", session.getCounselorName());
            sessionData.put("sessionDate", session.getSessionDate());
            sessionData.put("sessionTime", session.getSessionTime());
            sessionData.put("sessionType", session.getSessionType());
            sessionData.put("status", session.getStatus());
            sessionData.put("notes", session.getNotes());
            
            // Fetch counselor user details by counselor ID
            com.teamspring.MindCare.model.User counselor = userRepository.findById(session.getCounselorId()).orElse(null);
            if (counselor != null) {
                sessionData.put("counselorEmail", counselor.getEmail());
                sessionData.put("counselorPhone", counselor.getPhone());
            } else {
                sessionData.put("counselorEmail", "N/A");
                sessionData.put("counselorPhone", "N/A");
            }
            
            enrichedSessions.add(sessionData);
        }
        
        model.addAttribute("upcomingSessions", enrichedSessions);
        return "counselling/mysession";
    }

    // ===== Handle Booking Submission =====
    @PostMapping("/confirm")
    public String confirmBooking(@ModelAttribute BookingRequest bookingRequest) {
        try {
            Long studentId = getCurrentUserId();
            bookingRequest.setStudentId(studentId);
            System.out.println("📝 Booking session for student ID: " + studentId);
            System.out.println("📝 Counselor ID: " + bookingRequest.getCounselorId());
            System.out.println("📝 Date: " + bookingRequest.getDate());
            System.out.println("📝 Time: " + bookingRequest.getTime());
            System.out.println("📝 Session Type: " + bookingRequest.getSessionType());
            
            counsellingService.bookSession(bookingRequest);
            return "redirect:/mindcare/counselling/my-sessions?success";
        } catch (Exception e) {
            System.err.println("✗ Booking failed: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/mindcare/counselling/booking?error=" + e.getMessage();
        }
    }

    // ===== Handle Set Availability Submission =====
    @PostMapping("/set-availability")
    public String saveAvailability(
            @RequestParam String selectedDate,
            @RequestParam(required = false) String[] timeSlots) {
        
        Long counselorId = getCurrentUserId();
        
        try {
            counsellingService.saveAvailability(counselorId, selectedDate, timeSlots);
            return "redirect:/mindcare/counselling/set-availability?success";
        } catch (Exception e) {
            System.err.println("✗ Failed to save availability: " + e.getMessage());
            return "redirect:/mindcare/counselling/set-availability?error";
        }
    }

    // ===== Reschedule Session =====
    @GetMapping("/reschedule/{sessionId}")
    public String rescheduleSession(@PathVariable Long sessionId, Model model) {
        // Get session to retrieve counselor information
        var session = counsellingService.getSessionById(sessionId);
        if (session == null) {
            return "redirect:/mindcare/counselling/my-sessions?error=session_not_found";
        }
        
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("counselorId", session.getCounselorId());
        model.addAttribute("counselors", counsellingService.getAllCounselors());
        model.addAttribute("availabilityDates", counsellingService.getAvailabilityDates());
        return "counselling/reschedule";
    }

    // ===== Handle Reschedule Submission =====
    @PostMapping("/reschedule/{sessionId}")
    public String submitReschedule(
            @PathVariable Long sessionId,
            @RequestParam String newDate,
            @RequestParam String newTime) {
        
        try {
            counsellingService.rescheduleSession(sessionId, newDate, newTime);
            return "redirect:/mindcare/counselling/my-sessions?rescheduled";
        } catch (Exception e) {
            System.err.println("✗ Reschedule failed: " + e.getMessage());
            return "redirect:/mindcare/counselling/my-sessions?error";
        }
    }

    // ===== Cancel Session =====
    @GetMapping("/cancel/{sessionId}")
    public String cancelSession(@PathVariable Long sessionId, @RequestParam(required = false, defaultValue = "student") String source) {
        try {
            counsellingService.cancelSession(sessionId);
            // Redirect based on who cancelled
            if ("counselor".equals(source)) {
                return "redirect:/mindcare/counselling/my-schedule?cancelled";
            } else {
                return "redirect:/mindcare/counselling/my-sessions?cancelled";
            }
        } catch (Exception e) {
            System.err.println("✗ Cancel failed: " + e.getMessage());
            // Redirect based on who cancelled
            if ("counselor".equals(source)) {
                return "redirect:/mindcare/counselling/my-schedule?error";
            } else {
                return "redirect:/mindcare/counselling/my-sessions?error";
            }
        }
    }

    // ===== Confirm Session =====
    @GetMapping("/confirm-session/{sessionId}")
    public String confirmSession(@PathVariable Long sessionId) {
        try {
            counsellingService.confirmSession(sessionId);
            return "redirect:/mindcare/counselling/my-schedule?confirmed";
        } catch (Exception e) {
            System.err.println("✗ Confirmation failed: " + e.getMessage());
            return "redirect:/mindcare/counselling/my-schedule?error";
        }
    }

    // ===== Delete Session =====
    @GetMapping("/delete/{sessionId}")
    public String deleteSession(@PathVariable Long sessionId, @RequestParam(required = false, defaultValue = "student") String source) {
        try {
            counsellingService.deleteSession(sessionId);
            
            // Redirect based on source
            if ("counselor".equalsIgnoreCase(source)) {
                return "redirect:/mindcare/counselling/my-schedule?deleted";
            } else {
                return "redirect:/mindcare/counselling/my-sessions?deleted";
            }
        } catch (Exception e) {
            System.err.println("✗ Delete failed: " + e.getMessage());
            return "redirect:/mindcare/counselling/my-sessions?error";
        }
    }
    
    // ===== API Endpoint: Get Available Time Slots for a Date =====
    @GetMapping("/api/available-slots")
    @ResponseBody
    public java.util.List<String> getAvailableSlots(
            @RequestParam Long counselorId,
            @RequestParam String date) {
        return counsellingService.getAvailableTimeSlotsForDate(counselorId, date);
    }
}
