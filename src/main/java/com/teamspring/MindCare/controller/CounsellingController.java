package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.BookingRequest;
import com.teamspring.MindCare.service.CounsellingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mindcare/counselling")
public class CounsellingController {

    private final CounsellingService counsellingService;

    public CounsellingController(CounsellingService counsellingService) {
        this.counsellingService = counsellingService;
    }

    // ===== Show Counselling Home Page =====
    @GetMapping
    public String counsellingHome(Model model) {
        model.addAttribute("upcomingSessions", counsellingService.getUpcomingSessions());
        return "redirect:/mindcare/counselling/booking";
    }

    // ===== Show Booking Page =====
    @GetMapping("/booking")
    public String counsellingPage(Model model) {

        model.addAttribute("userRole", "student"); // or professional
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
        model.addAttribute("counselors", counsellingService.getAllCounselors());
        model.addAttribute("timeSlots", counsellingService.getTimeSlots());
        return "counselling/HP-setavailability";
    }

    // ===== Show HP My Schedule Page =====
    @GetMapping("/my-schedule")
    public String mySchedule(Model model) {
        Long counselorId = 1L; // In real app, get from session/auth
        model.addAttribute("upcomingSessions", counsellingService.getCounselorSessions(counselorId));
        return "counselling/HP-myschedule";
    }

    // ===== Show My Sessions Page =====
    @GetMapping("/my-sessions")
    public String mySessions(Model model) {
        model.addAttribute("upcomingSessions", counsellingService.getUpcomingSessions());
        return "counselling/mysession";
    }

    // ===== Handle Booking Submission =====
    @PostMapping("/confirm")
    public String confirmBooking(
            @ModelAttribute BookingRequest bookingRequest) {

        counsellingService.bookSession(bookingRequest);

        return "redirect:/mindcare/counselling?success";
    }

    // ===== Handle Set Availability Submission =====
    @PostMapping("/set-availability")
    public String saveAvailability(
            @RequestParam String selectedDate,
            @RequestParam(required = false) String[] timeSlots) {
        // TODO: Save availability to database
        System.out.println("✓ Availability saved for date: " + selectedDate);
        if (timeSlots != null) {
            for (String slot : timeSlots) {
                System.out.println("  - " + slot);
            }
        }
        return "redirect:/mindcare/counselling/set-availability?success";
    }

    // ===== Reschedule Session =====
    @GetMapping("/reschedule/{sessionId}")
    public String rescheduleSession(@PathVariable Long sessionId, Model model) {
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("counselors", counsellingService.getAllCounselors());
        model.addAttribute("timeSlots", counsellingService.getTimeSlots());
        model.addAttribute("availabilityDates", counsellingService.getAvailabilityDates());
        return "counselling/reschedule";
    }

    // ===== Handle Reschedule Submission =====
    @PostMapping("/reschedule/{sessionId}")
    public String submitReschedule(
            @PathVariable Long sessionId,
            @RequestParam String newDate,
            @RequestParam String newTime) {
        System.out.println("✓ Session " + sessionId + " rescheduled to " + newDate + " at " + newTime);
        // TODO: Update session in database
        return "redirect:/mindcare/counselling/my-sessions?rescheduled";
    }

    // ===== Cancel Session =====
    @GetMapping("/cancel/{sessionId}")
    public String cancelSession(@PathVariable Long sessionId) {
        System.out.println("✓ Session " + sessionId + " cancelled");
        // TODO: Delete session from database
        return "redirect:/mindcare/counselling/my-sessions?cancelled";
    }
}
