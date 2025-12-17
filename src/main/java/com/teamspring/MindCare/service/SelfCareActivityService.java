package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.SelfCareActivity;
import com.teamspring.MindCare.repository.SelfCareActivityRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelfCareActivityService {
    
    @Autowired
    private SelfCareActivityRepository selfCareActivityRepository;
    
    // Safe method - returns empty list if anything goes wrong
    public List<SelfCareActivity> getAllActivities() {
        try {
            return selfCareActivityRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error fetching self-care activities: " + e.getMessage());
            return createFallbackActivities(); // Return fallback data
        }
    }
    
    public List<SelfCareActivity> getActivitiesByCategory(String category) {
        try {
            if (category == null || "all".equalsIgnoreCase(category)) {
                return getAllActivities();
            }
            return selfCareActivityRepository.findByCategory(category);
        } catch (Exception e) {
            System.err.println("Error fetching activities by category: " + e.getMessage());
            return createFallbackActivities();
        }
    }
    
    public SelfCareActivity getActivityById(Long id) {
        try {
            return selfCareActivityRepository.findById(id)
                .orElseGet(this::createFallbackActivity);
        } catch (Exception e) {
            System.err.println("Error fetching activity by ID: " + e.getMessage());
            return createFallbackActivity();
        }
    }
    
    public List<String> getAllCategories() {
        try {
            List<String> categories = selfCareActivityRepository.findAllDistinctCategories();
            categories.add(0, "all");
            return categories;
        } catch (Exception e) {
            System.err.println("Error fetching self-care categories: " + e.getMessage());
            return List.of("all", "breathing", "meditation", "yoga", "journaling"); // Default
        }
    }
    
    public List<SelfCareActivity> searchActivities(String query) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return getAllActivities();
            }
            return selfCareActivityRepository.findByTitleContainingIgnoreCase(query);
        } catch (Exception e) {
            System.err.println("Error searching activities: " + e.getMessage());
            return createFallbackActivities();
        }
    }
    
    // Fallback data in case database fails
    private List<SelfCareActivity> createFallbackActivities() {
        List<SelfCareActivity> fallback = new ArrayList<>();
        
        SelfCareActivity activity1 = new SelfCareActivity();
        activity1.setId(9991L);
        activity1.setTitle("Breathing Exercise");
        activity1.setDescription("Simple breathing technique to reduce stress");
        activity1.setCategory("breathing");
        activity1.setDurationMinutes(5);
        activity1.setDifficulty("Beginner");
        activity1.setInstructions("Inhale for 4 seconds, hold for 4, exhale for 6");
        activity1.setBenefits("Reduces anxiety, promotes relaxation");
        
        SelfCareActivity activity2 = new SelfCareActivity();
        activity2.setId(9992L);
        activity2.setTitle("Guided Meditation");
        activity2.setDescription("Short meditation for focus");
        activity2.setCategory("meditation");
        activity2.setDurationMinutes(10);
        activity2.setDifficulty("Beginner");
        activity2.setInstructions("Find a quiet space and follow the audio");
        activity2.setBenefits("Improves concentration, reduces stress");
        
        fallback.add(activity1);
        fallback.add(activity2);
        
        return fallback;
    }
    
    private SelfCareActivity createFallbackActivity() {
        SelfCareActivity activity = new SelfCareActivity();
        activity.setId(0L);
        activity.setTitle("Activity Temporarily Unavailable");
        activity.setDescription("This self-care activity is currently not available. Please try another one.");
        activity.setCategory("general");
        activity.setDurationMinutes(5);
        activity.setDifficulty("Beginner");
        activity.setInstructions("Please select another activity from the list.");
        activity.setBenefits("Wellbeing maintenance");
        return activity;
    }
}