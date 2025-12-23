package com.teamspring.MindCare.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            new QuickAction("Mood Tracker", "icon-mood-2", "/mindcare/mood/tracker"),
            new QuickAction("Self Care", "icon-self-care", "/mindcare/selfcare"),
            new QuickAction("Book Session", "icon-book-session", "booking"),
            new QuickAction("Peer Support", "icon-peer-support", "/mindcare/peer-support")
        );

        // Create sample sessions using new structure
        CounsellingSession session1 = new CounsellingSession();
        session1.setId(1L);
        session1.setCounselorId(1L);
        session1.setCounselorName("Dr. Sarah Johnson");
        session1.setStudentId(1L);
        session1.setSessionDate(LocalDate.of(2025, 11, 5));
        session1.setSessionTime(LocalTime.of(14, 0));
        session1.setSessionType("Individual Counselling");
        session1.setStatus("Confirmed");
        
        CounsellingSession session2 = new CounsellingSession();
        session2.setId(2L);
        session2.setCounselorId(2L);
        session2.setCounselorName("Dr. Michael Chen");
        session2.setStudentId(1L);
        session2.setSessionDate(LocalDate.of(2025, 11, 8));
        session2.setSessionTime(LocalTime.of(10, 0));
        session2.setSessionType("Group Counselling");
        session2.setStatus("Confirmed");
        
        List<CounsellingSession> sessions = List.of(session1, session2);

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