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
        return "counselling/HP-setavailability";
    }

    // ===== Show HP My Schedule Page =====
    @GetMapping("/my-schedule")
    public String mySchedule(Model model) {
        model.addAttribute("upcomingSessions", counsellingService.getUpcomingSessions());
        return "counselling/hp-myschedule";
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
}
