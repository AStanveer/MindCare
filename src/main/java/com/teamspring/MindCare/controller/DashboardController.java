package com.teamspring.MindCare.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamspring.MindCare.model.CounsellingSession;
import com.teamspring.MindCare.model.ScheduleItem;
import com.teamspring.MindCare.viewmodel.QuickAction;

@Controller
@RequestMapping("/mindcare")
public class DashboardController {

    @GetMapping("/dashboard")
    public String routeDashboard() {
        String role = "professional";

        if (role.contains("admin")) {
            return "redirect:/mindcare/admin/dashboard";
        } else if (role.contains("student")) {
            return "redirect:/mindcare/student/dashboard";
        } else if (role.contains("professional")) {
            return "redirect:/mindcare/professional/dashboard";
        } else {
            return "redirect:/mindcare/error";
        }
    }

    @GetMapping("/student/dashboard")
    public String home(Model model) {

        List<QuickAction> actions = List.of(
            new QuickAction("Quick Assessment", "icon-assessment", "assessment/dass21"),
            new QuickAction("Self Care", "icon-self-care", "self-care"),
            new QuickAction("Book Session", "icon-book-session", "booking"),
            new QuickAction("Peer Support", "icon-peer-support", "/mindcare/peer-support")
        );

        List<CounsellingSession> sessions = List.of(
            new CounsellingSession("Dr. Sarah Johnson", LocalDateTime.of(2025, 11, 5, 14, 0), "Individual Counselling"),
            new CounsellingSession("Dr. Michael Chen", LocalDateTime.of(2025, 11, 8, 10, 0), "Group Counselling")
        );

        model.addAttribute("message", "MindCare Frontend Works!");
        model.addAttribute("quickActions", actions);
        model.addAttribute("counsellingSessions", sessions);
        return "dashboard/student/dashboard";
    }
    
    @GetMapping("/professional/dashboard")
    public String professionalDashboard(Model model) {
        List<QuickAction> actions = List.of(
            new QuickAction("Add Availability", "icon-calendar", "assessment/dass21"),
            new QuickAction("Create Resource", "icon-book", "self-care"),
            new QuickAction("Manage Schedule", "icon-clock", "booking")
        );

        List<ScheduleItem> todaysSchedule = List.of(
            new ScheduleItem("Alex M.", LocalDateTime.of(2025, 11, 2, 10, 0), "Individual", "Confirmed", "alex@uni.edu", "(555) 123-4567", "Anxiety", "Making good progress."),
            new ScheduleItem("Jordan P.", LocalDateTime.of(2025, 11, 2, 14, 0), "Individual", "Confirmed", "jordan@uni.edu", "(555) 987-6543", "Academic Stress", "Review study plan."),
            new ScheduleItem("Sam K.", LocalDateTime.of(2025, 11, 2, 16, 0), "Follow-up", "Pending", "sam@uni.edu", "(555) 555-5555", "Check-in", "Needs approval.")
        );

        model.addAttribute("quickActions", actions);
        model.addAttribute("todaysSchedule", todaysSchedule);
        model.addAttribute("username", "Emily Carter");
        return "dashboard/professional/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        return "dashboard/admin/dashboard";
    }
}