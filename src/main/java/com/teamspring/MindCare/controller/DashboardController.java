package com.teamspring.MindCare.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamspring.MindCare.dto.DashboardStatsDTO;
import com.teamspring.MindCare.dto.RecentStudentDTO;
import com.teamspring.MindCare.dto.SessionDTO;
import com.teamspring.MindCare.model.CounsellingSession;
import com.teamspring.MindCare.model.MoodEntry;
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.service.DashboardService;
import com.teamspring.MindCare.service.UserService;
import com.teamspring.MindCare.viewmodel.QuickAction;

import jakarta.servlet.http.HttpSession;

/**
 * Dashboard Controller
 * Handles role-specific dashboard access with Spring Security integration
 */
@Controller
@RequestMapping("/mindcare")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
    
    @Autowired
    private UserService userService;

    /**
     * Route to appropriate dashboard based on user role
     */
    @GetMapping("/dashboard")
    public String routeDashboard(Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        
        // Get authenticated user's email
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        
        // Store user in session for quick access
        session.setAttribute("user", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("userRole", user.getRole());
        
        // Route based on role
        switch (user.getRole()) {
            case STUDENT:
                return "redirect:/mindcare/dashboard/student";
            case PROFESSIONAL:
                return "redirect:/mindcare/dashboard/professional";
            case ADMIN:
                return "redirect:/mindcare/admin/dashboard";
            default:
                return "redirect:/auth/login";
        }
    }

    /**
     * Student Dashboard
     */
    @GetMapping("/dashboard/student")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentDashboard(Principal principal, HttpSession session, Model model) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        
        // Get current authenticated user
        User user = getCurrentUser(principal, session);
        Long currentUserId = user.getId();
        
        // Fetch student-specific data
        List<CounsellingSession> sessions = dashboardService.getStudentSessions(currentUserId);
        MoodEntry todayMood = dashboardService.getTodayMood(currentUserId);
        Double weeklyAvg = dashboardService.getWeeklyMoodAverage(currentUserId);
        long assessmentCount = dashboardService.getAssessmentCount(currentUserId);
        
        // Quick actions for students
        List<QuickAction> actions = List.of(
            new QuickAction("Mood Tracker", "icon-mood-2", "/mindcare/mood/tracker"),
            new QuickAction("Self Care", "icon-self-care", "/mindcare/selfcare"),
            new QuickAction("Book Session", "icon-book-session", "/mindcare/counselling/booking"),
            new QuickAction("Peer Support", "icon-peer-support", "/mindcare/peer-support")
        );
        
        int assessmentGoal = 5; // Static goal: "Take 5 assessments this semester"
        
        // Add attributes to model
        model.addAttribute("user", user);
        model.addAttribute("username", user.getFullName());
        model.addAttribute("quickActions", actions);
        model.addAttribute("counsellingSessions", sessions);
        model.addAttribute("todayMood", todayMood);
        model.addAttribute("weeklyMoodAvg", weeklyAvg != null ? String.format("%.1f", weeklyAvg) : "N/A");
        model.addAttribute("assessmentCount", assessmentCount);
        model.addAttribute("assessmentGoal", assessmentGoal);
        
        return "dashboard/student/dashboard";
    }
    
    /**
     * Professional Dashboard
     */
    @GetMapping("/dashboard/professional")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public String professionalDashboard(Principal principal, HttpSession session, Model model) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        
        User user = getCurrentUser(principal, session);
        Long currentUserId = user.getId();
        
        List<QuickAction> actions = List.of(
            new QuickAction("Add Availability", "icon-calendar", "/mindcare/counselling/set-availability"),
            new QuickAction("Create Resource", "icon-book", "/mindcare/professional/resources"),
            new QuickAction("Manage Schedule", "icon-clock", "/mindcare/counselling/my-schedule")
        );
        
        List<SessionDTO> todaysSchedule = dashboardService.getProfessionalSessions(currentUserId);
        
        List<RecentStudentDTO> recentStudents = dashboardService.getRecentStudents(currentUserId);

        DashboardStatsDTO stats = dashboardService.getProfessionalStats(currentUserId);
        
        model.addAttribute("user", user);
        model.addAttribute("username", user.getFullName());
        model.addAttribute("quickActions", actions);
        model.addAttribute("todaysSchedule", todaysSchedule);
        
        // Add the new attributes
        model.addAttribute("recentStudents", recentStudents);
        model.addAttribute("stats", stats);
        
        return "dashboard/professional/dashboard";
    }

    /*
    * Admin Dashboard
    */
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard(Principal principal, HttpSession session, Model model) {
        if (principal == null) {
            return "redirect:/auth/login";
        }
        
        // Get current authenticated user
        User user = getCurrentUser(principal, session);
        
        // Fetch admin statistics
        long totalUsers = userService.getTotalUsers();
        long totalStudents = (long) userService.getStudentsCount();
        long totalProfessionals = (long) userService.getProfessionalsCount();
        long activeUsers = userService.getActiveUsersCount();
        
        // Quick actions for admins
        List<QuickAction> actions = List.of(
            new QuickAction("User Management", "icon-users", "/mindcare/admin/usermanagement"),
            new QuickAction("Analytics", "icon-chart", "/mindcare/admin/analytics"),
            new QuickAction("System Settings", "icon-settings", "/mindcare/admin/settings")
        );
        
        // Add attributes to model
        model.addAttribute("user", user);
        model.addAttribute("username", user.getFullName());
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalProfessionals", totalProfessionals);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("quickActions", actions);
        
        return "redirect:/mindcare/admin/analytics";
    }
    
    /**
     * Helper method to get current authenticated user
     * Checks session first, then loads from database if needed
     */
    private User getCurrentUser(Principal principal, HttpSession session) {
        // Try to get from session first
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // Load from database using authenticated email
            String email = principal.getName();
            user = userService.getUserByEmail(email);
            
            // Store in session for future requests
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("userRole", user.getRole());
        }
        
        return user;
    }
    
    /**
     * Alternative method to get current user using SecurityContextHolder
     * Useful when Principal is not available
     */
    private User getCurrentUserFromContext(HttpSession session) {
        // Try session first
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // Get from SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                user = userService.getUserByEmail(email);
                
                // Store in session
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getFullName());
                session.setAttribute("userRole", user.getRole());
            }
        }
        
        return user;
    }
}