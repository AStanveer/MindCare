package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.SelfCareActivity;
import com.teamspring.MindCare.service.SelfCareActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("mindcare/professional/selfcare")
public class ProfessionalSelfCareController {

    private final SelfCareActivityService selfCareService;

    @Autowired
    public ProfessionalSelfCareController(SelfCareActivityService selfCareService) {
        this.selfCareService = selfCareService;
    }

    // LIST with search + category filter
    @GetMapping
    public String listActivities(@RequestParam(required = false) String search,
                                 @RequestParam(required = false) String category,
                                 Model model) {

        List<SelfCareActivity> activities;
        boolean hasSearch   = search != null && !search.trim().isEmpty();
        boolean hasCategory = category != null && !"all".equalsIgnoreCase(category);

        if (hasSearch && hasCategory) {
            activities = selfCareService.getActivitiesByCategory(category)
                    .stream()
                    .filter(a -> a.getTitle() != null &&
                                 a.getTitle().toLowerCase().contains(search.trim().toLowerCase()))
                    .toList();
        } else if (hasSearch) {
            activities = selfCareService.searchActivities(search.trim());
        } else if (hasCategory) {
            activities = selfCareService.getActivitiesByCategory(category);
        } else {
            activities = selfCareService.getAllActivities();
        }

        List<String> categories = selfCareService.getAllCategories();

        model.addAttribute("activities", activities);
        model.addAttribute("categories", categories);
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedCategory", category != null ? category : "all");
        model.addAttribute("activity", new SelfCareActivity()); // for create modal binding
        model.addAttribute("activePage", "selfcare");           // for shared navbar

        return "professional/selfcare/list";
    }

    // CREATE (modal)
    @PostMapping("/create")
    public String createActivity(@ModelAttribute SelfCareActivity activity,
                                 RedirectAttributes redirectAttributes) {

        try {
            if (activity.getCreatedBy() == null || activity.getCreatedBy().trim().isEmpty()) {
                activity.setCreatedBy("Healthcare Professional");
            }
            if (activity.getContentType() == null || activity.getContentType().trim().isEmpty()) {
                activity.setContentType("VIDEO");   // default type
            }
            selfCareService.saveActivity(activity);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Activity '" + activity.getTitle() + "' created successfully!"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Failed to create activity: " + e.getMessage()
            );
        }

        // important: redirect to mindcare/professional/selfcare
        return "redirect:/mindcare/professional/selfcare";
    }

    // UPDATE (modal)
    @PostMapping("/{id}/update")
    public String updateActivity(@PathVariable Long id,
                                 @ModelAttribute SelfCareActivity activity,
                                 RedirectAttributes redirectAttributes) {

        try {
            SelfCareActivity existing = selfCareService.getActivityById(id);
            if (existing == null || existing.getId() == 0L) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found");
            }

            existing.setTitle(activity.getTitle());
            existing.setDescription(activity.getDescription());
            existing.setCategory(activity.getCategory());
            existing.setDurationMinutes(activity.getDurationMinutes());
            existing.setDifficulty(activity.getDifficulty());
            existing.setContentType(activity.getContentType());
            existing.setVideoUrl(activity.getVideoUrl());
            existing.setThumbnailUrl(activity.getThumbnailUrl());
            existing.setInstructions(activity.getInstructions());
            existing.setBenefits(activity.getBenefits());

            if (activity.getCreatedBy() != null && !activity.getCreatedBy().trim().isEmpty()) {
                existing.setCreatedBy(activity.getCreatedBy());
            }

            selfCareService.saveActivity(existing);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Activity updated successfully!"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Failed to update activity: " + e.getMessage()
            );
        }

        return "redirect:/mindcare/professional/selfcare";
    }

    // DELETE (modal)
    @PostMapping("/{id}/delete")
    public String deleteActivity(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {

        try {
            SelfCareActivity activity = selfCareService.getActivityById(id);
            String title = activity != null ? activity.getTitle() : "Activity";

            selfCareService.deleteActivity(id);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Activity '" + title + "' deleted successfully!"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Failed to delete activity: " + e.getMessage()
            );
        }

        return "redirect:/mindcare/professional/selfcare";
    }
}
