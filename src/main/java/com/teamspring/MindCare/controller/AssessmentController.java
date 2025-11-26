package com.teamspring.MindCare.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mindcare/assessment")
public class AssessmentController {

    @GetMapping("/dass21")
    public String showDass21(Model model) {
        model.addAttribute("activePage", "assessment"); // Add this line
        return "dass21"; 
    }

    @PostMapping("/results")
    public String showResults(@RequestParam("responses") List<Integer> responses, 
                            Model model) {
        
        model.addAttribute("activePage", "assessment"); // Add this line
        
        // Calculate DASS-21 scores
        int depressionScore = calculateDepressionScore(responses);
        int anxietyScore = calculateAnxietyScore(responses);
        int stressScore = calculateStressScore(responses);
        
        // Add scores to model
        model.addAttribute("depressionScore", depressionScore);
        model.addAttribute("anxietyScore", anxietyScore);
        model.addAttribute("stressScore", stressScore);
        
        // Add interpretation levels
        model.addAttribute("depressionLevel", interpretScore(depressionScore, "depression"));
        model.addAttribute("anxietyLevel", interpretScore(anxietyScore, "anxiety"));
        model.addAttribute("stressLevel", interpretScore(stressScore, "stress"));
        
        return "assessment-results";
    }

    @GetMapping("/moodtracker")
    public String showMoodTracker(Model model) {
        model.addAttribute("activePage", "moodtracker"); // Add this line
        return "mood-tracker";
    }

    private int calculateDepressionScore(List<Integer> responses) {
        int[] indices = {2, 4, 9, 12, 15, 16, 20}; // DASS-21 depression questions
        return calculateSubscale(responses, indices);
    }

    private int calculateAnxietyScore(List<Integer> responses) {
        int[] indices = {1, 3, 6, 8, 14, 18, 19}; // DASS-21 anxiety questions
        return calculateSubscale(responses, indices);
    }

    private int calculateStressScore(List<Integer> responses) {
        int[] indices = {0, 5, 7, 10, 11, 13, 17}; // DASS-21 stress questions
        return calculateSubscale(responses, indices);
    }

    private int calculateSubscale(List<Integer> responses, int[] indices) {
        int sum = 0;
        for (int index : indices) {
            sum += responses.get(index);
        }
        return sum * 2; // Convert to DASS-42 equivalent
    }

    private String interpretScore(int score, String type) {
        if (type.equals("depression")) {
            if (score <= 9) return "Normal";
            else if (score <= 13) return "Mild";
            else if (score <= 20) return "Moderate";
            else if (score <= 27) return "Severe";
            else return "Extremely Severe";
        } else if (type.equals("anxiety")) {
            if (score <= 7) return "Normal";
            else if (score <= 9) return "Mild";
            else if (score <= 14) return "Moderate";
            else if (score <= 19) return "Severe";
            else return "Extremely Severe";
        } else { // stress
            if (score <= 14) return "Normal";
            else if (score <= 18) return "Mild";
            else if (score <= 25) return "Moderate";
            else if (score <= 33) return "Severe";
            else return "Extremely Severe";
        }
    }
}