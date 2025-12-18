package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.MoodEntry;
import com.teamspring.MindCare.service.MoodService;
import com.teamspring.MindCare.service.MoodService.MoodStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/mindcare/mood")
public class MoodController {
    
    @Autowired
    private MoodService moodService;
    
    // Main mood tracker page
    @GetMapping("/tracker")
    public String showMoodTracker(Model model) {
        // Check if user already logged mood today
        List<MoodEntry> todayEntries = moodService.getRecentMoodEntries().stream()
            .filter(entry -> entry.getEntryDate().equals(LocalDate.now()))
            .toList();
        
        if (!todayEntries.isEmpty()) {
            model.addAttribute("todayEntry", todayEntries.get(0));
        }
        
        // Get recent entries for sidebar (last 3 days)
        List<MoodEntry> recentEntries = moodService.getRecentMoodEntries().stream()
            .limit(3)
            .toList();
        model.addAttribute("recentEntries", recentEntries);
        
        // Get all recent entries for chart (7 days)
        List<MoodEntry> weekEntries = moodService.getRecentMoodEntries();
        model.addAttribute("weekEntries", weekEntries);
        
        // Get insights
        List<String> insights = moodService.getMoodInsights();
        model.addAttribute("insights", insights);
        
        // Get statistics
        MoodStatistics stats = moodService.getMoodStatistics();
        model.addAttribute("stats", stats);
        
        model.addAttribute("activePage", "moodtracker");
        return "mood/mood-tracker";
    }
    
    // Submit mood entry
    @PostMapping("/save")
    public String saveMoodEntry(
            @RequestParam("moodLevel") Integer moodLevel,
            @RequestParam(value = "notes", required = false) String notes) {
        
        moodService.saveMoodEntry(moodLevel, notes);
        return "redirect:/mindcare/mood/tracker";
    }
    
    // View mood history
    @GetMapping("/history")
    public String showMoodHistory(Model model) {
        List<MoodEntry> allEntries = moodService.getUserMoodHistory();
        model.addAttribute("entries", allEntries);
        model.addAttribute("activePage", "moodtracker");
        return "mood/history";
    }
    
    // View specific entry
    @GetMapping("/entry/{id}")
    public String viewMoodEntry(@PathVariable Long id, Model model) {
        MoodEntry entry = moodService.getMoodEntryById(id);
        model.addAttribute("entry", entry);
        model.addAttribute("activePage", "moodtracker");
        return "mood/entry-detail";
    }
    
    // Delete entry
    @PostMapping("/delete/{id}")
    public String deleteMoodEntry(@PathVariable Long id) {
        moodService.deleteMoodEntry(id);
        return "redirect:/mindcare/mood/history";
    }
    
    // Mood calendar view
    @GetMapping("/calendar")
    public String showMoodCalendar(
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "yyyy-MM") LocalDate month,
            Model model) {
        
        if (month == null) {
            month = LocalDate.now().withDayOfMonth(1);
        }
        
        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());
        
        List<MoodEntry> monthEntries = moodService.getUserMoodHistory().stream()
            .filter(entry -> !entry.getEntryDate().isBefore(startDate) && !entry.getEntryDate().isAfter(endDate))
            .toList();
        
        model.addAttribute("month", month);
        model.addAttribute("monthEntries", monthEntries);
        model.addAttribute("activePage", "moodtracker");
        
        return "mood/calendar";
    }
}