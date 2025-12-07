package com.teamspring.MindCare.controller;

import com.teamspring.MindCare.model.AssessmentResult;
import com.teamspring.MindCare.service.AssessmentService;
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
    public String showForm(Model model) {

        model.addAttribute("questions", new String[] {
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
        });

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

        AssessmentResult result = AssessmentService.evaluate(answers);

        model.addAttribute("result", result);
        return "assessment/assessment-results";
    }

    @GetMapping("/moodtracker")
    public String showMoodTracker(Model model) {
        model.addAttribute("activePage", "moodtracker");
        return "assessment/mood-tracker";
    }
}