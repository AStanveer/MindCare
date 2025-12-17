package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.Resource;
import com.teamspring.MindCare.model.Selfcare;
import com.teamspring.MindCare.service.ResourceService;
import com.teamspring.MindCare.service.SelfcareService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/mindcare")
public class ResourceController {
    
    private final ResourceService resourceService;
    private final SelfcareService selfcareService; 
    
    public ResourceController(ResourceService resourceService, SelfcareService selfcareService) {  // ADD PARAMETER
        this.resourceService = resourceService;
        this.selfcareService = selfcareService; 
    }

    @GetMapping("/selfcare")
    public String showSelfCare(
            @RequestParam(value = "category", defaultValue = "breathing") String category,
            Model model) {
        
        model.addAttribute("activePage", "selfcare");
        
        // Get exercises based on category
        List<Selfcare> exercises;
        if ("all".equalsIgnoreCase(category)) {
            exercises = selfcareService.getAllExercises();
        } else {
            exercises = selfcareService.getExercisesByCategory(category);
        }
        
        model.addAttribute("exercises", exercises);
        model.addAttribute("categories", selfcareService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        
        return "resources/self-care";
    }

    @GetMapping("/resources")
    public String showResources(
            @RequestParam(value = "category", defaultValue = "All") String category,
            Model model) {
        
        model.addAttribute("activePage", "resources");
        model.addAttribute("categories", resourceService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        
        List<Resource> resources;
        if ("All".equalsIgnoreCase(category)) {
            resources = resourceService.getAllResources();
        } else {
            resources = resourceService.getResourcesByCategory(category);
        }
        
        model.addAttribute("resources", resources);
        return "resources/resources"; 
    }
}