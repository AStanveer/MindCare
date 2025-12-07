package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.Resource;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class ResourceService {
    
    public List<Resource> getAllResources() {
        return Arrays.asList(
            new Resource(
                "Understanding Anxiety",
                "Learn about the signs, symptoms, and coping mechanisms for anxiety disorders.",
                "Mental Health",
                "5 min",
                "Dr. Sarah Johnson",
                "2024-03-15"
            ),
            new Resource(
                "Mindfulness Meditation Guide",
                "A beginner's guide to practicing mindfulness meditation daily.",
                "Self-Care",
                "8 min",
                "Michael Chen",
                "2024-03-10"
            ),
            new Resource(
                "Sleep Hygiene Tips",
                "Improve your sleep quality with these evidence-based recommendations.",
                "Wellness",
                "6 min",
                "Sleep Center Team",
                "2024-03-05"
            ),
            new Resource(
                "Stress Management Techniques",
                "Effective strategies to manage stress in your daily life.",
                "Mental Health",
                "7 min",
                "Dr. Robert Davis",
                "2024-02-28"
            ),
            new Resource(
                "Building Resilience",
                "How to develop emotional resilience during challenging times.",
                "Personal Growth",
                "10 min",
                "Emma Wilson",
                "2024-02-20"
            ),
            new Resource(
                "Digital Detox Benefits",
                "Why and how to take regular breaks from digital devices.",
                "Wellness",
                "4 min",
                "Tech Wellness Group",
                "2024-02-15"
            )
        );
    }
    
    public List<Resource> getResourcesByCategory(String category) {
        return getAllResources().stream()
                .filter(resource -> resource.getCategory().equalsIgnoreCase(category))
                .toList();
    }
    
    public List<String> getAllCategories() {
        return Arrays.asList("All", "Mental Health", "Self-Care", "Wellness", "Personal Growth");
    }
}