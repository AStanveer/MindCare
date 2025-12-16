package com.teamspring.MindCare.config;

import com.teamspring.MindCare.model.Resource;
import com.teamspring.MindCare.model.SelfCareActivity;
import com.teamspring.MindCare.repository.ResourceRepository;
import com.teamspring.MindCare.repository.SelfCareActivityRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;


@Configuration
public class StudentModuleDataInitializer {
    
    @Bean
    @Transactional
    public CommandLineRunner initStudentModules(
            ResourceRepository resourceRepository,
            SelfCareActivityRepository selfCareActivityRepository) {
        
        return args -> {
            System.out.println("=== Initializing Student-Facing Modules ===");
            
            // 1. Initialize RESOURCES (Articles) - Your data is SAFE
            initStudentResources(resourceRepository);
            
            // 2. Initialize SELF-CARE - Your data is SAFE
            initStudentSelfCare(selfCareActivityRepository);
            
            System.out.println("=== Student modules ready! ===");
            System.out.println("Resources count: " + resourceRepository.count());
            System.out.println("Self-care activities: " + selfCareActivityRepository.count());
        };
    }
    
    private void initStudentResources(ResourceRepository repository) {
        if (repository.count() == 0) {
            System.out.println("Creating sample resources for students...");
            
            List<Resource> sampleResources = List.of(
                createResource(
                    "Understanding Anxiety in Students",
                    "Learn to recognize and manage anxiety in academic settings.",
                    "Full article content about student anxiety...",
                    "Anxiety",
                    "5 min read",
                    "Dr. Sarah Johnson",
                    "Campus Psychologist",
                    LocalDate.now().minusDays(10)
                ),
                createResource(
                    "Exam Stress Management",
                    "Effective techniques to handle exam-related stress.",
                    "Full content about exam stress management...",
                    "Stress",
                    "6 min read",
                    "MindCare Team",
                    "Student Support",
                    LocalDate.now().minusDays(5)
                ),
                createResource(
                    "Building Healthy Sleep Habits",
                    "Essential tips for students to improve sleep quality.",
                    "Full content about sleep hygiene...",
                    "Sleep",
                    "7 min read",
                    "Dr. Michael Chen",
                    "Sleep Specialist",
                    LocalDate.now().minusDays(3)
                ),
                createResource(
                    "Social Anxiety on Campus",
                    "Navigating social situations in university life.",
                    "Full content about social anxiety...",
                    "Anxiety",
                    "8 min read",
                    "Dr. Lisa Wang",
                    "Clinical Psychologist",
                    LocalDate.now().minusDays(1)
                )
            );
            
            repository.saveAll(sampleResources);
        }
    }
    
    private void initStudentSelfCare(SelfCareActivityRepository repository) {
        if (repository.count() == 0) {
            System.out.println("Creating self-care activities for students...");
            
            List<SelfCareActivity> sampleActivities = List.of(
                createSelfCareActivity(
                    "5-Minute Study Break Breathing",
                    "Quick breathing exercise for between study sessions.",
                    "VIDEO",
                    "breathing",
                    5,
                    "Beginner",
                    "https://www.youtube.com/embed/example1",
                    "1. Sit up straight\n2. Breathe in for 4 seconds\n3. Hold for 4\n4. Exhale for 6",
                    "Reduces stress, improves focus"
                ),
                createSelfCareActivity(
                    "10-Minute Guided Meditation",
                    "Short meditation to clear your mind before studying.",
                    "GUIDED_MEDITATION",
                    "meditation",
                    10,
                    "Beginner",
                    "https://www.youtube.com/embed/example2",
                    "Find a quiet space and follow the audio guide",
                    "Improves concentration, reduces anxiety"
                ),
                createSelfCareActivity(
                    "Desk Yoga for Students",
                    "Gentle stretches you can do at your desk.",
                    "EXERCISE",
                    "yoga",
                    7,
                    "Beginner",
                    null,
                    "1. Neck rolls\n2. Shoulder stretches\n3. Seated twists",
                    "Relieves tension, improves posture"
                ),
                createSelfCareActivity(
                    "Gratitude Journaling Exercise",
                    "Simple writing practice to boost positivity.",
                    "EXERCISE",
                    "journaling",
                    10,
                    "Beginner",
                    null,
                    "Write 3 things you're grateful for each day",
                    "Improves mood, reduces stress"
                )
            );
            
            repository.saveAll(sampleActivities);
        }
    }
    
    private Resource createResource(String title, String desc, String content, 
                                   String category, String readTime, String author, 
                                   String role, LocalDate date) {
        Resource resource = new Resource();
        resource.setTitle(title);
        resource.setDescription(desc);
        resource.setContent(content);
        resource.setCategory(category);
        resource.setReadTime(readTime);
        resource.setAuthor(author);
        resource.setAuthorRole(role);
        resource.setPublishDate(date);
        resource.setFeatured(title.contains("Anxiety") || title.contains("Stress")); // Some featured
        return resource;
    }
    
    private SelfCareActivity createSelfCareActivity(String title, String desc, 
                                                   String type, String category, 
                                                   Integer duration, String difficulty,
                                                   String videoUrl, String instructions,
                                                   String benefits) {
        SelfCareActivity activity = new SelfCareActivity();
        activity.setTitle(title);
        activity.setDescription(desc);
        activity.setContentType(type);
        activity.setCategory(category);
        activity.setDurationMinutes(duration);
        activity.setDifficulty(difficulty);
        activity.setVideoUrl(videoUrl);
        activity.setInstructions(instructions);
        activity.setBenefits(benefits);
        activity.setFeatured(title.contains("Breathing") || title.contains("Meditation"));
        return activity;
    }
}