package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.Resource;
import com.teamspring.MindCare.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mindcare/resources")
public class ResourceController {
    
    @Autowired
    private ResourceService resourceService;
    
    // Main resources page with filtering
    @GetMapping
    public String showResources(
            @RequestParam(value = "category", defaultValue = "All") String category,
            @RequestParam(value = "search", required = false) String search,
            Model model) {
        
        model.addAttribute("activePage", "resources");
        model.addAttribute("categories", resourceService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        
        List<Resource> resources;
        if (search != null && !search.trim().isEmpty()) {
            // Simple search implementation (you can enhance this)
            resources = resourceService.getAllResources().stream()
                .filter(r -> r.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                            r.getDescription().toLowerCase().contains(search.toLowerCase()))
                .toList();
            model.addAttribute("searchQuery", search);
        } else {
            resources = resourceService.getResourcesByCategory(category);
        }
        
        model.addAttribute("resources", resources);
        model.addAttribute("featuredResources", resourceService.getFeaturedResources());
        
        return "resources/resources";
    }
    
    // View single resource
    @GetMapping("/{id}")
    public String viewResource(@PathVariable Long id, Model model) {
        Resource resource = resourceService.getResourceById(id);
        model.addAttribute("resource", resource);
        model.addAttribute("activePage", "resources");
        
        // You could also fetch related resources by same category
        List<Resource> relatedResources = resourceService.getResourcesByCategory(resource.getCategory())
            .stream()
            .filter(r -> !r.getId().equals(id))
            .limit(3)
            .toList();
        model.addAttribute("relatedResources", relatedResources);
        
        return "resources/resource-detail";
    }
    
    // Category-specific page (optional)
    @GetMapping("/category/{category}")
    public String showResourcesByCategory(@PathVariable String category, Model model) {
        model.addAttribute("activePage", "resources");
        model.addAttribute("categories", resourceService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("resources", resourceService.getResourcesByCategory(category));
        return "resources/resources";
    }
}