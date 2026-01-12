package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.AssessmentResult;
import com.teamspring.MindCare.model.CounsellingSession;
import com.teamspring.MindCare.model.FeatureUsage;
import com.teamspring.MindCare.model.MoodEntry;
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.repository.AssessmentResultRepository;
import com.teamspring.MindCare.repository.CounsellingSessionRepository;
import com.teamspring.MindCare.repository.FeatureUsageRepository;
import com.teamspring.MindCare.repository.MoodEntryRepository;
import com.teamspring.MindCare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mindcare/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private AssessmentResultRepository assessmentResultRepository;
    
    @Autowired
    private CounsellingSessionRepository counsellingSessionRepository;
    
    @Autowired
    private FeatureUsageRepository featureUsageRepository;
    
    @Autowired
    private MoodEntryRepository moodEntryRepository;
    
    @GetMapping("/analytics")
    public String viewAnalytics(Model model) {
        // Stats
        model.addAttribute("totalStudents", userService.getStudentsCount());
        model.addAttribute("activeProfessionals", userService.getProfessionalsCount());
        
        // Sessions this month
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        long sessionsThisMonth = counsellingSessionRepository
            .findBySessionDateGreaterThanEqualOrderBySessionDateAscSessionTimeAsc(startOfMonth)
            .size();
        model.addAttribute("sessionsThisMonth", sessionsThisMonth);
        
        // Average wellbeing score (from DASS-21 assessments, simplified)
        List<AssessmentResult> allAssessments = assessmentResultRepository.findAll();
        double avgWellbeing = 7.2; // Default
        if (!allAssessments.isEmpty()) {
            double totalScore = allAssessments.stream()
                .mapToDouble(a -> Math.max(0, 10 - (a.getDepressionScore() + a.getAnxietyScore() + a.getStressScore()) / 30.0))
                .sum();
            avgWellbeing = totalScore / allAssessments.size();
        }
        model.addAttribute("avgWellbeing", String.format("%.1f", avgWellbeing));
        
        // Chart data - Wellbeing Trends
        Map<String, Object> wellbeingTrends = getWellbeingTrendsData();
        model.addAttribute("wellbeingLabels", wellbeingTrends.get("labels"));
        model.addAttribute("wellbeingData", wellbeingTrends.get("data"));
        
        // Chart data - Mood Distribution
        Map<String, Object> moodDist = getMoodDistributionData();
        model.addAttribute("moodLabels", moodDist.get("labels"));
        model.addAttribute("moodData", moodDist.get("data"));
        
        // Chart data - Sessions by Type
        Map<String, Object> sessionTypes = getSessionsByTypeData();
        model.addAttribute("sessionLabels", sessionTypes.get("labels"));
        model.addAttribute("sessionData", sessionTypes.get("data"));
        
        // Chart data - Feature Usage
        Map<String, Object> featureUsage = getFeatureUsageData();
        model.addAttribute("featureLabels", featureUsage.get("labels"));
        model.addAttribute("featureData", featureUsage.get("data"));
        
        return "admin/Analytics-report";
    }

    @GetMapping("/usermanagement")
    public String viewUserManagement(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("studentsCount", userService.getStudentsCount());
        model.addAttribute("professionalsCount", userService.getProfessionalsCount());
        model.addAttribute("adminsCount", userService.getAdminsCount());
        model.addAttribute("activeCount", userService.getActiveUsersCount());
        model.addAttribute("inactiveCount", userService.getInactiveUsersCount());
        
        return "admin/user-management";
    }
    
    @GetMapping("/api/user/{id}")
@ResponseBody
public Map<String, Object> getUserDetails(@PathVariable Long id) {
    Map<String, Object> response = new HashMap<>();
    
    try {
        User user = userService.getUserById(id); // ✅ NO orElse
        response.put("user", user);

        List<AssessmentResult> assessments =
                assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(user.getId());
        response.put("assessments", assessments);

        response.put("success", true);
    } catch (RuntimeException e) {
        response.put("success", false);
        response.put("message", e.getMessage());
    }
    
    return response;
}

    
    @GetMapping("/api/user/{id}/deactivate")
    @ResponseBody
    public Map<String, Object> deactivateUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.deactivateUser(id);
            response.put("success", true);
            response.put("message", "User deactivated successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to deactivate user: " + e.getMessage());
        }
        return response;
    }
    
    @GetMapping("/api/user/{id}/activate")
    @ResponseBody
    public Map<String, Object> activateUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.activateUser(id);
            response.put("success", true);
            response.put("message", "User activated successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to activate user: " + e.getMessage());
        }
        return response;
    }
    
    private Map<String, Object> getWellbeingTrendsData() {
        Map<String, Object> response = new HashMap<>();
        
        // Get assessments from last 6 months grouped by month
        List<AssessmentResult> allAssessments = assessmentResultRepository.findAll();
        
        Map<String, Double> monthlyScores = new HashMap<>();
        Map<String, Integer> monthlyCounts = new HashMap<>();
        
        LocalDate now = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.from(now.minusMonths(i));
            String monthKey = month.format(java.time.format.DateTimeFormatter.ofPattern("MMM"));
            monthlyScores.put(monthKey, 0.0);
            monthlyCounts.put(monthKey, 0);
        }
        
        for (AssessmentResult assessment : allAssessments) {
            if (assessment.getCompletedAt() != null) {
                YearMonth month = YearMonth.from(assessment.getCompletedAt());
                String monthKey = month.format(java.time.format.DateTimeFormatter.ofPattern("MMM"));
                
                if (monthlyScores.containsKey(monthKey)) {
                    double totalScore = assessment.getDepressionScore() + 
                                      assessment.getAnxietyScore() + 
                                      assessment.getStressScore();
                    double wellbeingScore = Math.max(0, 10 - (totalScore / 30.0));
                    
                    monthlyScores.put(monthKey, monthlyScores.get(monthKey) + wellbeingScore);
                    monthlyCounts.put(monthKey, monthlyCounts.get(monthKey) + 1);
                }
            }
        }
        
        // Calculate averages
        List<String> labels = new java.util.ArrayList<>();
        List<Double> data = new java.util.ArrayList<>();
        
        for (int i = 5; i >= 0; i--) {
            YearMonth month = YearMonth.from(now.minusMonths(i));
            String monthKey = month.format(java.time.format.DateTimeFormatter.ofPattern("MMM"));
            labels.add(monthKey);
            
            int count = monthlyCounts.get(monthKey);
            double avg = count > 0 ? monthlyScores.get(monthKey) / count : 7.0;
            data.add(Math.round(avg * 10.0) / 10.0);
        }
        
        response.put("labels", labels);
        response.put("data", data);
        return response;
    }
    
    private Map<String, Object> getMoodDistributionData() {
        Map<String, Object> response = new HashMap<>();
        
        // Get mood entries from last 7 days
        LocalDate weekAgo = LocalDate.now().minusDays(7);
        List<com.teamspring.MindCare.model.MoodEntry> recentMoods = moodEntryRepository.findAll().stream()
            .filter(m -> m.getEntryDate() != null && m.getEntryDate().isAfter(weekAgo))
            .toList();
        
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("Excellent", 0L);
        distribution.put("Good", 0L);
        distribution.put("Okay", 0L);
        distribution.put("Fair", 0L);
        
        for (com.teamspring.MindCare.model.MoodEntry mood : recentMoods) {
            if (mood.getMoodLevel() != null) {
                switch (mood.getMoodLevel()) {
                    case 5 -> distribution.put("Excellent", distribution.get("Excellent") + 1);
                    case 4 -> distribution.put("Good", distribution.get("Good") + 1);
                    case 3 -> distribution.put("Okay", distribution.get("Okay") + 1);
                    case 1, 2 -> distribution.put("Fair", distribution.get("Fair") + 1);
                }
            }
        }
        
        response.put("labels", new String[]{"Excellent", "Good", "Okay", "Fair"});
        response.put("data", new Long[]{
            distribution.get("Excellent"),
            distribution.get("Good"),
            distribution.get("Okay"),
            distribution.get("Fair")
        });
        
        return response;
    }
    
    private Map<String, Object> getSessionsByTypeData() {
        Map<String, Object> response = new HashMap<>();
        
        // Get sessions from this month
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        List<com.teamspring.MindCare.model.CounsellingSession> sessions = counsellingSessionRepository
            .findBySessionDateGreaterThanEqualOrderBySessionDateAscSessionTimeAsc(startOfMonth);
        
        Map<String, Long> typeCounts = new HashMap<>();
        typeCounts.put("In Person", 0L);
        typeCounts.put("Online", 0L);

        for (com.teamspring.MindCare.model.CounsellingSession session : sessions) {
            String type = session.getSessionType();
            if (type == null) continue;

            String normalized = type.trim().toLowerCase();
            if (normalized.contains("video") || normalized.contains("online")) {
                typeCounts.put("Online", typeCounts.get("Online") + 1);
            } else {
                // Treat any other type (e.g., InPerson, Individual, Follow-up) as in-person
                typeCounts.put("In Person", typeCounts.get("In Person") + 1);
            }
        }

        response.put("labels", new String[]{"In Person", "Online"});
        response.put("data", new Long[]{
            typeCounts.get("In Person"),
            typeCounts.get("Online")
        });
        
        return response;
    }
    
    private Map<String, Object> getFeatureUsageData() {
        Map<String, Object> response = new HashMap<>();
        
        List<com.teamspring.MindCare.model.FeatureUsage> allUsage = featureUsageRepository.findAll();
        
        if (allUsage.isEmpty()) {
            response.put("labels", new String[]{"Self-Care", "Assessments", "Peer Support", "Counselling", "Resources"});
            response.put("data", new Integer[]{0, 0, 0, 0, 0});
            return response;
        }
        
        // Calculate totals
        int totalSelfCare = allUsage.stream().mapToInt(com.teamspring.MindCare.model.FeatureUsage::getSelfCare).sum();
        int totalAssessments = allUsage.stream().mapToInt(com.teamspring.MindCare.model.FeatureUsage::getAssessments).sum();
        int totalPeerSupport = allUsage.stream().mapToInt(com.teamspring.MindCare.model.FeatureUsage::getPeerSupport).sum();
        int totalCounselling = allUsage.stream().mapToInt(com.teamspring.MindCare.model.FeatureUsage::getCounselling).sum();
        int totalResources = allUsage.stream().mapToInt(com.teamspring.MindCare.model.FeatureUsage::getResources).sum();
        
        int totalUsage = totalSelfCare + totalAssessments + totalPeerSupport + totalCounselling + totalResources;
        
        // Calculate percentages
        int selfCarePercent = totalUsage > 0 ? (totalSelfCare * 100) / totalUsage : 0;
        int assessmentsPercent = totalUsage > 0 ? (totalAssessments * 100) / totalUsage : 0;
        int peerSupportPercent = totalUsage > 0 ? (totalPeerSupport * 100) / totalUsage : 0;
        int counsellingPercent = totalUsage > 0 ? (totalCounselling * 100) / totalUsage : 0;
        int resourcesPercent = totalUsage > 0 ? (totalResources * 100) / totalUsage : 0;
        
        response.put("labels", new String[]{"Self-Care", "Assessments", "Peer Support", "Counselling", "Resources"});
        response.put("data", new Integer[]{selfCarePercent, assessmentsPercent, peerSupportPercent, counsellingPercent, resourcesPercent});
        
        return response;
    }
}
