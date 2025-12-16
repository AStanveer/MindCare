package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.SelfCareActivity;
import com.teamspring.MindCare.service.SelfCareActivityService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/mindcare/selfcare")
public class SelfCareController {
    
    @Autowired
    private SelfCareActivityService selfCareService;
    
    @GetMapping
    public String showSelfCare(
            @RequestParam(value = "category", defaultValue = "all") String category, // Change DEFAULT to "all"
            @RequestParam(value = "search", required = false) String search,
            Model model) {
        
        model.addAttribute("activePage", "selfcare");
        model.addAttribute("categories", selfCareService.getAllCategories());
        model.addAttribute("selectedCategory", category); // This should be "all" when clicked
        
        List<SelfCareActivity> activities;
        if (search != null && !search.trim().isEmpty()) {
            activities = selfCareService.searchActivities(search);
            model.addAttribute("searchQuery", search);
        } else if ("all".equalsIgnoreCase(category)) { // Make sure this check is correct
            activities = selfCareService.getAllActivities();
        } else {
            activities = selfCareService.getActivitiesByCategory(category);
        }
        
        model.addAttribute("activities", activities);
        return "selfcare/self-care";
    }
    
    // View single activity
    @GetMapping("/{id}")
    public String viewActivity(@PathVariable Long id, Model model) {
        SelfCareActivity activity = selfCareService.getActivityById(id);
        model.addAttribute("activity", activity);
        model.addAttribute("activePage", "selfcare");
        
        // Related activities
        List<SelfCareActivity> relatedActivities = selfCareService.getActivitiesByCategory(activity.getCategory())
            .stream()
            .filter(a -> !a.getId().equals(id))
            .limit(3)
            .toList();
        model.addAttribute("relatedActivities", relatedActivities);
        
        return "selfcare/activity-detail";
    }
    
    // Videos page
    @GetMapping("/videos")
    public String showVideos(
            @RequestParam(value = "category", required = false) String category,
            Model model) {
        
        model.addAttribute("categories", selfCareService.getAllCategories());
        model.addAttribute("selectedCategory", category != null ? category : "all");
        model.addAttribute("activePage", "selfcare");
        
        return "selfcare/videos";
    }
    
    // Meditation page
    @GetMapping("/meditation")
    public String showMeditation(Model model) {
        List<SelfCareActivity> meditations = selfCareService.getAllActivities().stream()
            .filter(a -> "meditation".equalsIgnoreCase(a.getCategory()) || 
                        "GUIDED_MEDITATION".equalsIgnoreCase(a.getContentType()))
            .toList();
        
        model.addAttribute("meditations", meditations);
        model.addAttribute("activePage", "selfcare");
        return "selfcare/meditation";
    }
    
    // Exercises page
    @GetMapping("/exercises")
    public String showExercises(Model model) {
        List<SelfCareActivity> exercises = selfCareService.getAllActivities().stream()
            .filter(a -> "EXERCISE".equalsIgnoreCase(a.getContentType()))
            .toList();
        
        model.addAttribute("exercises", exercises);
        model.addAttribute("activePage", "selfcare");
        return "selfcare/exercises";
    }
    
    // Quick access - 5 minute activities
    @GetMapping("/quick")
    public String showQuickActivities(Model model) {
        List<SelfCareActivity> quickActivities = selfCareService.getAllActivities().stream()
            .filter(a -> a.getDurationMinutes() != null && a.getDurationMinutes() <= 10)
            .toList();
        
        model.addAttribute("quickActivities", quickActivities);
        model.addAttribute("activePage", "selfcare");
        return "selfcare/quick-activities";
    }
}