package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.Resource;
import com.teamspring.MindCare.repository.ResourceRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {
    
    @Autowired
    private ResourceRepository resourceRepository;
    
    // Safe method - returns empty list if anything goes wrong
    public List<Resource> getAllResources() {
        try {
            return resourceRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error fetching resources: " + e.getMessage());
            return new ArrayList<>(); // Return empty list instead of crashing
        }
    }
    
    public List<Resource> getResourcesByCategory(String category) {
        try {
            if (category == null || "All".equalsIgnoreCase(category)) {
                return getAllResources();
            }
            return resourceRepository.findByCategory(category);
        } catch (Exception e) {
            System.err.println("Error fetching resources by category: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Resource getResourceById(Long id) {
        try {
            return resourceRepository.findById(id)
                .orElseGet(() -> {
                    // Return a dummy resource if not found (won't break page)
                    Resource dummy = new Resource();
                    dummy.setTitle("Resource Not Available");
                    dummy.setDescription("This resource is currently unavailable.");
                    dummy.setCategory("General");
                    return dummy;
                });
        } catch (Exception e) {
            System.err.println("Error fetching resource by ID: " + e.getMessage());
            Resource dummy = new Resource();
            dummy.setTitle("Error Loading Resource");
            dummy.setDescription("Please try again later.");
            return dummy;
        }
    }
    
    public List<String> getAllCategories() {
        try {
            List<String> categories = resourceRepository.findAllDistinctCategories();
            categories.add(0, "All");
            return categories;
        } catch (Exception e) {
            System.err.println("Error fetching categories: " + e.getMessage());
            return List.of("All", "Anxiety", "Stress", "Sleep"); // Default fallback
        }
    }
    
}