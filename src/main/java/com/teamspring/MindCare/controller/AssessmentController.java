package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.AssessmentResult;
import com.teamspring.MindCare.service.AssessmentService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mindcare/assessment")
public class AssessmentController {
    
    @Autowired
    private AssessmentService assessmentService;
    
    // Dummy user ID (until authentication is added)
    private static final Long DUMMY_USER_ID = 1L;
    
    // Static DASS-21 questions
    private static final List<String> DASS21_QUESTIONS = Arrays.asList(
        "I found it hard to wind down",
        "I was aware of dryness of my mouth",
        "I could not seem to experience any positive feeling at all",
        "I experienced breathing difficulty",
        "I found it difficult to work up the initiative to do things",
        "I tended to over react to situations",
        "I experienced trembling",
        "I felt that I was using a lot of nervous energy",
        "I was worried about situations in which I might panic",
        "I felt that I had nothing to look forward to",
        "I found myself getting agitated",
        "I found it difficult to relax",
        "I felt down hearted and blue",
        "I was intolerant of anything that kept me from getting on",
        "I felt I was close to panic",
        "I was unable to become enthusiastic about anything",
        "I felt I was not worth much as a person",
        "I felt that I was rather sensitive",
        "I was aware of the action of my heart in the absence of physical exertion",
        "I felt scared without any good reason",
        "I felt that life was meaningless"
    );
    
    @GetMapping("/dass21")
    public String showForm(Model model) {
        model.addAttribute("questions", DASS21_QUESTIONS);
        return "assessment/dass21";
    }
    
    @PostMapping("/dass21")
    public String submit(
        @RequestParam int q1,  @RequestParam int q2,  @RequestParam int q3,
        @RequestParam int q4,  @RequestParam int q5,  @RequestParam int q6,
        @RequestParam int q7,  @RequestParam int q8,  @RequestParam int q9,
        @RequestParam int q10, @RequestParam int q11, @RequestParam int q12,
        @RequestParam int q13, @RequestParam int q14, @RequestParam int q15,
        @RequestParam int q16, @RequestParam int q17, @RequestParam int q18,
        @RequestParam int q19, @RequestParam int q20, @RequestParam int q21,
        Model model) {
        
        int[] answers = {
            q1,q2,q3,q4,q5,q6,q7,q8,q9,q10,
            q11,q12,q13,q14,q15,q16,q17,q18,q19,q20,q21
        };
        
        // Save to database
        AssessmentResult result = assessmentService.submitAssessment(answers);
        
        // Create DTO for Thymeleaf
        model.addAttribute("result", new AssessmentResultDTO(result));
        return "assessment/assessment-results";
    }
    
    @GetMapping("/history")
    public String showHistory(Model model) {
        List<AssessmentResult> results = assessmentService.getUserResults(DUMMY_USER_ID);
        model.addAttribute("results", results);
        model.addAttribute("activePage", "assessment");
        return "assessment/history";
    }
    
    @GetMapping("/result/{id}")
    public String viewResult(@PathVariable Long id, Model model) {
        AssessmentResult result = assessmentService.getResultById(id);
        model.addAttribute("result", new AssessmentResultDTO(result));
        return "assessment/assessment-results";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteResult(@PathVariable Long id) {
        assessmentService.deleteResult(id);
        return "redirect:/mindcare/assessment/history";
    }
    
    @GetMapping("/moodtracker")
    public String showMoodTracker(Model model) {
        model.addAttribute("activePage", "moodtracker");
        return "assessment/mood-tracker";
    }
    
    // DTO for Thymeleaf compatibility
    public static class AssessmentResultDTO {
        private final int depressionScore;
        private final int anxietyScore;
        private final int stressScore;
        private final String depressionLevel;
        private final String anxietyLevel;
        private final String stressLevel;
        private final Long id;
        
        public AssessmentResultDTO(AssessmentResult result) {
            this.depressionScore = result.getDepressionScore();
            this.anxietyScore = result.getAnxietyScore();
            this.stressScore = result.getStressScore();
            this.depressionLevel = result.getDepressionLevel();
            this.anxietyLevel = result.getAnxietyLevel();
            this.stressLevel = result.getStressLevel();
            this.id = result.getId();
        }
        
        // Getters
        public int getDepressionScore() { return depressionScore; }
        public int getAnxietyScore() { return anxietyScore; }
        public int getStressScore() { return stressScore; }
        public String getDepressionLevel() { return depressionLevel; }
        public String getAnxietyLevel() { return anxietyLevel; }
        public String getStressLevel() { return stressLevel; }
        public Long getId() { return id; }
    }
}