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
            return resourceRepository.findAllByOrderByPublishDateDesc();
        } catch (Exception e) {
            System.err.println("Error fetching resources: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Resource> getResourcesByCategory(String category) {
        try {
            if (category == null || "All".equalsIgnoreCase(category)) {
                return getAllResources();
            }
            return resourceRepository.findByCategoryOrderByPublishDateDesc(category);
        } catch (Exception e) {
            System.err.println("Error fetching resources by category: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Resource getResourceById(Long id) {
        try {
            return resourceRepository.findById(id)
                .orElseGet(() -> {
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
            return List.of("All", "Anxiety", "Stress", "Sleep");
        }
    }

    // IMPLEMENTED: Save or Update Resource
    public Resource saveResource(Resource resource) {
        try {
            // If no publish date set, use current date
            if (resource.getPublishDate() == null) {
                resource.setPublishDate(java.time.LocalDate.now());
            }
            
            // Save to database
            return resourceRepository.save(resource);
        } catch (Exception e) {
            System.err.println("Error saving resource: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save resource: " + e.getMessage());
        }
    }

    // IMPLEMENTED: Delete Resource
    public void deleteResource(Long id) {
        try {
            if (resourceRepository.existsById(id)) {
                resourceRepository.deleteById(id);
                System.out.println("Resource deleted successfully: ID " + id);
            } else {
                System.err.println("Resource not found for deletion: ID " + id);
                throw new RuntimeException("Resource not found with ID: " + id);
            }
        } catch (Exception e) {
            System.err.println("Error deleting resource: " + e.getMessage());
            throw new RuntimeException("Failed to delete resource: " + e.getMessage());
        }
    }
    
    // ADDITIONAL: Search functionality
    public List<Resource> searchResources(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getAllResources();
            }
            return resourceRepository.searchResources(keyword.trim());
        } catch (Exception e) {
            System.err.println("Error searching resources: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ADDITIONAL: Get resources by author
    public List<Resource> getResourcesByAuthor(String author) {
        try {
            return resourceRepository.findByAuthorContainingIgnoreCase(author);
        } catch (Exception e) {
            System.err.println("Error fetching resources by author: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ========== NEW METHODS FOR ADVANCED SEARCH & FILTER ==========
    
    // Search within specific category
    public List<Resource> searchByCategory(String query, String category) {
        try {
            if (query == null || query.trim().isEmpty()) {
                return getResourcesByCategory(category);
            }
            if (category == null || "All".equalsIgnoreCase(category)) {
                return searchResources(query);
            }
            return resourceRepository.searchByCategory(category, query.trim());
        } catch (Exception e) {
            System.err.println("Error searching by category: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}