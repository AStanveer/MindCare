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

        long assessmentCount = dashboardService.getAssessmentCount(currentUserId);
        int assessmentGoal = 5; // Static goal: "Take 5 assessments this semester"

        model.addAttribute("username", user != null ? user.getFullName() : "Student");
        model.addAttribute("quickActions", actions);
        model.addAttribute("counsellingSessions", sessions);

        model.addAttribute("todayMood", todayMood);
        model.addAttribute("weeklyMoodAvg", String.format("%.1f", weeklyAvg));
        model.addAttribute("assessmentCount", assessmentCount);
        model.addAttribute("assessmentGoal", assessmentGoal);
        
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