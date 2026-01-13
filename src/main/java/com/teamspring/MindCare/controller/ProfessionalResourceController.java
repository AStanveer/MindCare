package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.Resource;
import com.teamspring.MindCare.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("mindcare/professional/resources")
public class ProfessionalResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ProfessionalResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    // LIST ALL RESOURCES WITH SEARCH & FILTER
    @GetMapping
    public String listResources(@RequestParam(required = false) String search,
                                @RequestParam(required = false) String category,
                                Model model) {

        List<Resource> resources;

        boolean hasSearch   = search != null && !search.trim().isEmpty();
        boolean hasCategory = category != null && !category.equals("All");

        if (hasSearch && hasCategory) {
            resources = resourceService.searchByCategory(search.trim(), category);
        } else if (hasSearch) {
            resources = resourceService.searchResources(search.trim());
        } else if (hasCategory) {
            resources = resourceService.getResourcesByCategory(category);
        } else {
            resources = resourceService.getAllResources();
        }

        model.addAttribute("resources", resources);
        model.addAttribute("resource", new Resource());   // for create modal binding
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("activePage", "professionalResources");

        return "professional/resources/list";
    }

    // CREATE RESOURCE (modal on list.html)
    @PostMapping("/create")
    public String createResource(@ModelAttribute Resource resource,
                                 RedirectAttributes redirectAttributes) {

        try {
            if (resource.getPublishDate() == null) {
                resource.setPublishDate(LocalDate.now());
            }
            if (resource.getAuthor() == null || resource.getAuthor().trim().isEmpty()) {
                resource.setAuthor("Healthcare Professional");
            }
            if (resource.getAuthorRole() == null || resource.getAuthorRole().trim().isEmpty()) {
                resource.setAuthorRole("Professional");
            }
            if (resource.getReadTime() == null || resource.getReadTime().trim().isEmpty()) {
                resource.setReadTime("5 min read");
            }

            resourceService.saveResource(resource);

            redirectAttributes.addFlashAttribute(
                "successMessage",
                "Resource '" + resource.getTitle() + "' created successfully!"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Failed to create resource: " + e.getMessage()
            );
        }

        return "redirect:/mindcare/professional/resources";
    }

    // UPDATE RESOURCE (modal on list.html)
    @PostMapping("/{id}/update")
    public String updateResource(@PathVariable Long id,
                                 @ModelAttribute Resource resource,
                                 RedirectAttributes redirectAttributes) {

        try {
            Resource existingResource = resourceService.getResourceById(id);
            if (existingResource == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
            }

            existingResource.setTitle(resource.getTitle());
            existingResource.setCategory(resource.getCategory());
            existingResource.setDescription(resource.getDescription());
            existingResource.setContent(resource.getContent());
            existingResource.setReadTime(resource.getReadTime());
            existingResource.setTags(resource.getTags());

            if (resource.getAuthor() != null && !resource.getAuthor().trim().isEmpty()) {
                existingResource.setAuthor(resource.getAuthor());
            }

            resourceService.saveResource(existingResource);

            redirectAttributes.addFlashAttribute(
                "successMessage",
                "Resource updated successfully!"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Failed to update resource: " + e.getMessage()
            );
        }

        return "redirect:/mindcare/professional/resources";
    }

    // DELETE RESOURCE (modal on list.html)
    @PostMapping("/{id}/delete")
    public String deleteResource(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {

        try {
            Resource resource = resourceService.getResourceById(id);
            String title = resource != null ? resource.getTitle() : "Resource";

            resourceService.deleteResource(id);

            redirectAttributes.addFlashAttribute(
                "successMessage",
                "Resource '" + title + "' deleted successfully!"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                "errorMessage",
                "Failed to delete resource: " + e.getMessage()
            );
        }

        return "redirect:/mindcare/professional/resources";
    }

    // AJAX SEARCH
    @GetMapping("/api/search")
    @ResponseBody
    public List<Resource> apiSearch(@RequestParam("q") String q) {
        return resourceService.searchResources(q);
    }
}
