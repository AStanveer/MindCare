package com.teamspring.MindCare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamspring.MindCare.dto.SessionDTO;
import com.teamspring.MindCare.model.CounsellingSession;
import com.teamspring.MindCare.model.MoodEntry;
import com.teamspring.MindCare.model.Role;
import com.teamspring.MindCare.model.UserTemp;
import com.teamspring.MindCare.service.DashboardService;
import com.teamspring.MindCare.viewmodel.QuickAction;



@Controller
@RequestMapping("/mindcare")
public class DashboardController {

    @Autowired DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String routeDashboard() {
        Long currentUserId = 1L;

        UserTemp user = dashboardService.getUser(currentUserId);

        if (user.getRole() == Role.ADMIN) {
            return "redirect:/mindcare/admin/dashboard";
        } else if (user.getRole() == Role.STUDENT) {
            return "redirect:/mindcare/student/dashboard";
        } else if (user.getRole() == Role.PROFESSIONAL) {
            return "redirect:/mindcare/professional/dashboard";
        } else {
            return "redirect:/mindcare/error";
        }
    }

    @GetMapping("/student/dashboard")
    public String home(Model model) {
        Long currentUserId = 8L;

        UserTemp user = dashboardService.getUser(currentUserId);
        
        List<CounsellingSession> sessions = dashboardService.getStudentSessions(currentUserId);

        MoodEntry todayMood = dashboardService.getTodayMood(currentUserId);
        Double weeklyAvg = dashboardService.getWeeklyMoodAverage(currentUserId);

        List<QuickAction> actions = List.of(
            new QuickAction("Mood Tracker", "icon-mood-2", "/mindcare/mood/tracker"),
            new QuickAction("Self Care", "icon-self-care", "/mindcare/selfcare"),
            new QuickAction("Book Session", "icon-book-session", "booking"),
            new QuickAction("Peer Support", "icon-peer-support", "/mindcare/peer-support")
        );

        // // Create sample sessions using new structure
        // CounsellingSession session1 = new CounsellingSession();
        // session1.setId(1L);
        // session1.setCounselorId(1L);
        // session1.setCounselorName("Dr. Sarah Johnson");
        // session1.setStudentId(1L);
        // session1.setSessionDate(LocalDate.of(2025, 11, 5));
        // session1.setSessionTime(LocalTime.of(14, 0));
        // session1.setSessionType("Individual Counselling");
        // session1.setStatus("Confirmed");
        
        // CounsellingSession session2 = new CounsellingSession();
        // session2.setId(2L);
        // session2.setCounselorId(2L);
        // session2.setCounselorName("Dr. Michael Chen");
        // session2.setStudentId(1L);
        // session2.setSessionDate(LocalDate.of(2025, 11, 8));
        // session2.setSessionTime(LocalTime.of(10, 0));
        // session2.setSessionType("Group Counselling");
        // session2.setStatus("Confirmed");
        
        // List<CounsellingSession> sessions = List.of(session1, session2);

        model.addAttribute("username", user != null ? user.getFullName() : "Student");
        model.addAttribute("quickActions", actions);
        model.addAttribute("counsellingSessions", sessions);

        model.addAttribute("todayMood", todayMood);
        model.addAttribute("weeklyMoodAvg", String.format("%.1f", weeklyAvg));
        
        return "dashboard/student/dashboard";
    }
    
    @GetMapping("/professional/dashboard")
    public String professionalDashboard(Model model) {
        Long currentUserId = 2L;

        List<QuickAction> actions = List.of(
            new QuickAction("Add Availability", "icon-calendar", "assessment/dass21"),
            new QuickAction("Create Resource", "icon-book", "self-care"),
            new QuickAction("Manage Schedule", "icon-clock", "booking")
        );

        List<SessionDTO> todaysSchedule = dashboardService.getProfessionalSessions(currentUserId);

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