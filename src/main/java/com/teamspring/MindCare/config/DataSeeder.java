package com.teamspring.MindCare.config;

import com.teamspring.MindCare.model.*;
import com.teamspring.MindCare.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
@Order(1)
public class DataSeeder {

    @Bean
    @Transactional
    public CommandLineRunner seedData(
            UserRepository userRepository,
            CounselorRepository counselorRepository,
            CounsellingSessionRepository sessionRepository,
            MoodEntryRepository moodRepository,
            ResourceRepository resourceRepository,
            SelfCareActivityRepository selfCareActivityRepository,
            PostRepository postRepository,
            ReplyRepository replyRepository,
            AvailabilityRepository availabilityRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🌱 MINDCARE DATA SEEDER - Starting...");
            System.out.println("=".repeat(60));

            // 1. Seed Users (Admin, Students, Professionals)
            seedUsers(userRepository, passwordEncoder);

            // 2. Seed Counselors (must be linked to PROFESSIONAL users)
            seedCounselors(counselorRepository, userRepository);

            // 3. Seed Counselor Availability
            seedAvailability(availabilityRepository, counselorRepository);

            // 4. Seed Counselling Sessions
            seedCounselingSessions(sessionRepository, userRepository);

            // 5. Seed Mood Entries
            seedMoodEntries(moodRepository, userRepository);

            // 6. Seed Resources
            seedResources(resourceRepository);

            // 7. Seed Self-Care Activities
            seedSelfCareActivities(selfCareActivityRepository);

            // 8. Seed Support Forum Posts
            seedSupportForumData(postRepository, replyRepository, userRepository);

            System.out.println("=".repeat(60));
            System.out.println("✅ DATA SEEDING COMPLETED!");
            System.out.println("=".repeat(60) + "\n");
        };
    }

    private void seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        System.out.println("\n📝 Seeding Users...");

        // Admin
        createUserIfNotExists(userRepository, passwordEncoder, 
            "System Administrator", "admin@mindcare.com", "admin123", Role.ADMIN,
            "0123456789", "I am the system super-admin.");

        // Students
        createStudentIfNotExists(userRepository, passwordEncoder,
            "Test Student", "student@mindcare.com", "student123",
            "0111222333", "A23CS0001", "3", "Software Engineering");

        createStudentIfNotExists(userRepository, passwordEncoder,
            "Moaz Student", "moaz@mindcare.com", "password",
            "0000000000", "M23CS0002", "4", "Software Engineering");

        createStudentIfNotExists(userRepository, passwordEncoder,
            "Alex M.", "alex@mindcare.com", "password",
            "0222333444", "A23CS0003", "2", "Computer Science");

        // Professionals
        createProfessionalIfNotExists(userRepository, passwordEncoder,
            "Dr. Sarah Johnson", "pro@mindcare.com", "pro123",
            "0155566677", "LIC-998877", "10 Years", "Health Center A", "Anxiety & Stress");

        createProfessionalIfNotExists(userRepository, passwordEncoder,
            "Dr. Michael Chen", "chen@mindcare.com", "chen123",
            "0166677788", "LIC-123456", "12 Years", "Health Center B", "Depression & Mood");

        createProfessionalIfNotExists(userRepository, passwordEncoder,
            "Dr. Emily Carter", "emily@mindcare.com", "emily123",
            "0177788899", "LIC-789012", "15 Years", "Health Center A", "Clinical Psychology");

        System.out.println("✅ Users seeded successfully");
    }

    private void createUserIfNotExists(UserRepository repo, PasswordEncoder encoder,
            String name, String email, String password, Role role, String phone, String bio) {
        if (!repo.existsByEmail(email)) {
            User user = new User(name, email, encoder.encode(password), role);
            user.setPhone(phone);
            user.setBio(bio);
            repo.save(user);
            System.out.println("  ✓ Created " + role + ": " + email);
        }
    }

    private void createStudentIfNotExists(UserRepository repo, PasswordEncoder encoder,
            String name, String email, String password, String phone, String studentId, String year, String department) {
        if (!repo.existsByEmail(email)) {
            User user = new User(name, email, encoder.encode(password), Role.STUDENT);
            user.setPhone(phone);
            user.setStudentId(studentId);
            user.setYear(year);
            user.setDepartment(department);
            repo.save(user);
            System.out.println("  ✓ Created STUDENT: " + email);
        }
    }

    private void createProfessionalIfNotExists(UserRepository repo, PasswordEncoder encoder,
            String name, String email, String password, String phone, String licenseNumber, 
            String experience, String location, String specialty) {
        if (!repo.existsByEmail(email)) {
            User user = new User(name, email, encoder.encode(password), Role.PROFESSIONAL);
            user.setPhone(phone);
            user.setLicenseNumber(licenseNumber);
            user.setExperience(experience);
            user.setLocation(location);
            user.setDepartment(specialty);
            repo.save(user);
            System.out.println("  ✓ Created PROFESSIONAL: " + email);
        }
    }

    private void seedCounselors(CounselorRepository counselorRepository, UserRepository userRepository) {
        System.out.println("\n👨‍⚕️ Seeding Counselors...");

        if (counselorRepository.count() > 0) {
            System.out.println("  ⏭️ Counselors already exist, skipping...");
            return;
        }

        // Link Counselors to Professional Users
        User sarah = userRepository.findByEmail("pro@mindcare.com").orElse(null);
        User michael = userRepository.findByEmail("chen@mindcare.com").orElse(null);
        User emily = userRepository.findByEmail("emily@mindcare.com").orElse(null);

        if (sarah != null) {
            Counselor c1 = new Counselor(null, "Dr. Sarah Johnson", "Anxiety & Stress", 4.9, true);
            c1.setUser(sarah);
            counselorRepository.save(c1);
            System.out.println("  ✓ Created Counselor: Dr. Sarah Johnson");
        }

        if (michael != null) {
            Counselor c2 = new Counselor(null, "Dr. Michael Chen", "Depression & Mood", 4.8, true);
            c2.setUser(michael);
            counselorRepository.save(c2);
            System.out.println("  ✓ Created Counselor: Dr. Michael Chen");
        }

        if (emily != null) {
            Counselor c3 = new Counselor(null, "Dr. Emily Carter", "Clinical Psychology", 4.9, true);
            c3.setUser(emily);
            counselorRepository.save(c3);
            System.out.println("  ✓ Created Counselor: Dr. Emily Carter");
        }
    }

    private void seedAvailability(AvailabilityRepository availabilityRepository, CounselorRepository counselorRepository) {
        System.out.println("\n📅 Seeding Counselor Availability...");

        if (availabilityRepository.count() > 0) {
            System.out.println("  ⏭️ Availability already exists, skipping...");
            return;
        }

        List<Counselor> counselors = counselorRepository.findAll();
        if (counselors.isEmpty()) {
            System.out.println("  ⚠️ No counselors found, skipping availability seeding");
            return;
        }

        // Seed availability for next 14 days
        LocalDate startDate = LocalDate.now();
        String[] timeSlots = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"};

        int totalSlots = 0;
        for (Counselor counselor : counselors) {
            for (int day = 0; day < 14; day++) {
                LocalDate date = startDate.plusDays(day);
                
                // Each counselor available on different days (spread out)
                if ((day + counselor.getId().intValue()) % 3 == 0) {
                    // Available every 3rd day with offset based on counselor ID
                    for (String timeSlotStr : timeSlots) {
                        LocalTime time = LocalTime.parse(timeSlotStr);
                        Availability availability = new Availability(counselor.getId(), date, time);
                        availabilityRepository.save(availability);
                        totalSlots++;
                    }
                }
            }
            System.out.println("  ✓ Created availability for: " + counselor.getName());
        }
        System.out.println("  ✓ Total slots created: " + totalSlots);
    }

    private void seedCounselingSessions(CounsellingSessionRepository sessionRepository, UserRepository userRepository) {
        System.out.println("\n📅 Seeding Counselling Sessions...");

        if (sessionRepository.count() > 0) {
            System.out.println("  ⏭️ Sessions already exist, skipping...");
            return;
        }

        User student = userRepository.findByEmail("moaz@mindcare.com").orElse(null);
        if (student == null) return;

        Long studentId = student.getId();

        // Past Session
        CounsellingSession past = new CounsellingSession();
        past.setCounselorId(1L);
        past.setCounselorName("Dr. Emily Carter");
        past.setStudentId(studentId);
        past.setSessionDate(LocalDate.now().minusDays(10));
        past.setSessionTime(LocalTime.of(10, 0));
        past.setSessionType("Individual");
        past.setStatus("Confirmed");
        past.setNotes("Initial consultation.");
        sessionRepository.save(past);

        // Today's Session
        CounsellingSession today = new CounsellingSession();
        today.setCounselorId(1L);
        today.setCounselorName("Dr. Emily Carter");
        today.setStudentId(studentId);
        today.setSessionDate(LocalDate.now());
        today.setSessionTime(LocalTime.of(14, 0));
        today.setSessionType("Individual");
        today.setStatus("Confirmed");
        today.setNotes("Follow up on exam stress management.");
        sessionRepository.save(today);

        // Future Session
        CounsellingSession future = new CounsellingSession();
        future.setCounselorId(1L);
        future.setCounselorName("Dr. Sarah Johnson");
        future.setStudentId(studentId);
        future.setSessionDate(LocalDate.now().plusDays(5));
        future.setSessionTime(LocalTime.of(10, 0));
        future.setSessionType("Group Therapy");
        future.setStatus("Confirmed");
        future.setNotes("Peer support group introduction.");
        sessionRepository.save(future);

        System.out.println("  ✓ Created 3 counselling sessions");
    }

    private void seedMoodEntries(MoodEntryRepository moodRepository, UserRepository userRepository) {
        System.out.println("\n😊 Seeding Mood Entries...");

        User student = userRepository.findByEmail("moaz@mindcare.com").orElse(null);
        if (student == null || !moodRepository.findByUserIdOrderByEntryDateDesc(student.getId()).isEmpty()) {
            System.out.println("  ⏭️ Mood entries already exist, skipping...");
            return;
        }

        Long studentId = student.getId();

        List<MoodEntry> moods = List.of(
            createMood(studentId, 2, "Feeling overwhelmed.", LocalDate.now().minusDays(4), LocalTime.of(20, 0)),
            createMood(studentId, 3, "Better than yesterday.", LocalDate.now().minusDays(3), LocalTime.of(9, 0)),
            createMood(studentId, 4, "Had a nice workout.", LocalDate.now().minusDays(2), LocalTime.of(18, 30)),
            createMood(studentId, 5, "Great study session!", LocalDate.now().minusDays(1), LocalTime.of(14, 0)),
            createMood(studentId, 4, "Starting the day fresh.", LocalDate.now(), LocalTime.of(8, 0))
        );

        moodRepository.saveAll(moods);
        System.out.println("  ✓ Created 5 mood entries");
    }

    private MoodEntry createMood(Long userId, Integer level, String notes, LocalDate date, LocalTime time) {
        MoodEntry mood = new MoodEntry();
        mood.setUserId(userId);
        mood.setMoodLevel(level);
        mood.setNotes(notes);
        mood.setEntryDate(date);
        mood.setEntryTime(time);
        return mood;
    }

    private void seedResources(ResourceRepository resourceRepository) {
        System.out.println("\n📚 Seeding Resources...");

        if (resourceRepository.count() > 0) {
            System.out.println("  ⏭️ Resources already exist, skipping...");
            return;
        }

        List<Resource> resources = List.of(
            createResource("Understanding Anxiety in Students", 
                "Learn to recognize and manage anxiety in academic settings.",
                "Full article content about student anxiety...", "Anxiety", "5 min read", "Dr. Sarah Johnson", LocalDate.now().minusDays(10)),
            createResource("Exam Stress Management",
                "Effective techniques to handle exam-related stress.",
                "Full content about exam stress management...", "Stress", "6 min read", "MindCare Team", LocalDate.now().minusDays(5)),
            createResource("Building Healthy Sleep Habits",
                "Essential tips for students to improve sleep quality.",
                "Full content about sleep hygiene...", "Sleep", "7 min read", "Dr. Michael Chen", LocalDate.now().minusDays(3))
        );

        resourceRepository.saveAll(resources);
        System.out.println("  ✓ Created " + resources.size() + " resources");
    }

    private Resource createResource(String title, String description, String content, String category, String readTime, String author, LocalDate publishDate) {
        Resource resource = new Resource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setContent(content);
        resource.setCategory(category);
        resource.setReadTime(readTime);
        resource.setAuthor(author);
        resource.setPublishDate(publishDate);
        return resource;
    }

    private void seedSelfCareActivities(SelfCareActivityRepository repository) {
        System.out.println("\n🧘 Seeding Self-Care Activities...");

        if (repository.count() > 0) {
            System.out.println("  ⏭️ Activities already exist, skipping...");
            return;
        }

        List<SelfCareActivity> activities = List.of(
            createSelfCareActivity("5-Minute Study Break Breathing", 
                "Quick breathing exercise for between study sessions.",
                "VIDEO", "breathing", 5, "Beginner",
                "https://www.youtube.com/embed/8vkYJf8DOsc?si=cD9nTglanIvEvq0H",
                "1. Sit up straight\n2. Breathe in for 4 seconds\n3. Hold for 4\n4. Exhale for 6",
                "Reduces stress, improves focus"),
            createSelfCareActivity("Progressive Muscle Relaxation",
                "Reduce physical tension through guided muscle relaxation.",
                "VIDEO", "relaxation", 15, "Beginner",
                "https://www.youtube.com/embed/4bP8sLNrS3g?si=FXsx5E8kRWPl98ZJ",
                "Systematically tense and relax each muscle group.",
                "Relieves muscle tension, promotes sleep")
        );

        repository.saveAll(activities);
        System.out.println("  ✓ Created " + activities.size() + " self-care activities");
    }

    private SelfCareActivity createSelfCareActivity(String title, String description, String contentType, 
            String category, Integer duration, String difficulty, String videoUrl, String instructions, String benefits) {
        SelfCareActivity activity = new SelfCareActivity();
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setContentType(contentType);
        activity.setCategory(category);
        activity.setDurationMinutes(duration);
        activity.setDifficulty(difficulty);
        activity.setVideoUrl(videoUrl);
        activity.setInstructions(instructions);
        activity.setBenefits(benefits);
        return activity;
    }

    private void seedSupportForumData(PostRepository postRepository, ReplyRepository replyRepository, UserRepository userRepository) {
        System.out.println("\n💬 Seeding Support Forum...");

        if (postRepository.count() > 0) {
            System.out.println("  ⏭️ Forum data already exists, skipping...");
            return;
        }

        User alex = userRepository.findByEmail("alex@mindcare.com").orElse(null);
        User moaz = userRepository.findByEmail("moaz@mindcare.com").orElse(null);

        if (alex == null || moaz == null) {
            System.out.println("  ⚠️ Required users not found, skipping forum seeding...");
            return;
        }

        // Create a sample post
        Post post = new Post();
        post.setTitle("How to manage exam anxiety?");
        post.setContent("I'm struggling with exam anxiety. Any tips?");
        post.setAuthor(alex);
        post.setCreatedAt(LocalDateTime.now().minusDays(2));
        postRepository.save(post);

        // Create a reply
        Reply reply = new Reply();
        reply.setContent("Try deep breathing exercises 30 minutes before the exam.");
        reply.setAuthor(moaz);
        reply.setPost(post);
        reply.setCreatedAt(LocalDateTime.now().minusDays(1));
        replyRepository.save(reply);

        System.out.println("  ✓ Created forum post with replies");
    }
}
