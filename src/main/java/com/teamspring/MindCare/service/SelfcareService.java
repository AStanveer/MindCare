package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.Selfcare;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SelfcareService {
    
    public List<Selfcare> getAllExercises() {
        return Arrays.asList(
            new Selfcare(
                "Box Breathing",
                "A simple technique to reduce stress and improve focus. Inhale for 4 counts, hold for 4, exhale for 4, hold for 4.",
                "breathing",
                "5 mins",
                "Dr. Sarah Chen",
                "beginner"
            ),
            new Selfcare(
                "4-7-8 Breathing",
                "A natural calming method. Inhale for 4 seconds, hold for 7, exhale for 8. Effective for anxiety and sleep problems.",
                "breathing",
                "5 mins",
                "Dr. Michael Rodriguez",
                "beginner"
            ),
            new Selfcare(
                "Body Scan Meditation",
                "A progressive relaxation exercise bringing awareness to each part of the body from toes to head without judgment.",
                "meditation",
                "10 mins",
                "Dr. Emily Watson",
                "beginner"
            ),
            new Selfcare(
                "Progressive Muscle Relaxation",
                "Systematically tense and relax muscle groups to release physical tension. Effective for stress relief and improved sleep.",
                "physical",
                "8 mins",
                "Dr. James Kim",
                "intermediate"
            ),
            new Selfcare(
                "Gratitude Journaling",
                "Write down three things you're grateful for each day to shift focus to positive aspects of life.",
                "journaling",
                "5 mins",
                "Dr. Lisa Park",
                "beginner"
            ),
            new Selfcare(
                "Guided Sleep Meditation",
                "A calming voice guides you into deep relaxation for better sleep quality and insomnia relief.",
                "meditation",
                "15 mins",
                "Dr. Robert Davis",
                "beginner"
            ),
            new Selfcare(
                "Mindful Walking",
                "Practice walking meditation by focusing on each step and your connection to the ground.",
                "physical",
                "10 mins",
                "Dr. Emma Wilson",
                "intermediate"
            ),
            new Selfcare(
                "Calming Sound Bath",
                "Use Tibetan singing bowls and gentle tones to relax the nervous system and reduce stress.",
                "music",
                "12 mins",
                "Sound Therapy Center",
                "beginner"
            )
        );
    }
    
    public List<Selfcare> getExercisesByCategory(String category) {
        return getAllExercises().stream()
                .filter(exercise -> exercise.getCategory().equalsIgnoreCase(category))
                .toList();
    }
    
    public List<String> getAllCategories() {
        return Arrays.asList("breathing", "meditation", "physical", "journaling", "music");
    }
    
    public List<String> getCategoryDisplayNames() {
        return Arrays.asList("Breathing", "Meditation", "Physical", "Journaling", "Music");
    }
    
}