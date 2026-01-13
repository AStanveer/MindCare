package com.teamspring.MindCare.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.transaction.annotation.Transactional;

import com.teamspring.MindCare.model.CounsellingSession;
import com.teamspring.MindCare.model.Counselor;
import com.teamspring.MindCare.model.MoodEntry;
import com.teamspring.MindCare.model.Role;
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.repository.CounsellingSessionRepository;
import com.teamspring.MindCare.repository.CounselorRepository;
import com.teamspring.MindCare.repository.MoodEntryRepository;
import com.teamspring.MindCare.repository.UserRepository;

// @Configuration
@Order(3) // Run this AFTER StudentModule (1) and SupportForum (2) initializers
public class DashboardDataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initDashboardData(
            UserRepository userRepository,
            CounselorRepository counselorRepository,
            MoodEntryRepository moodRepository,
            CounsellingSessionRepository sessionRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            System.out.println("=== Initializing Dashboard Demo Data ===");

            // 1. Ensure our specific Demo Student (Moaz) exists
            User student = initDemoUsers(userRepository, passwordEncoder);
            
            // 2. Ensure Counselors exist
            Counselor doctor = initCounselors(counselorRepository);

            // 3. Initialize Mood History (For Student Dashboard Graph)
            initMoodHistory(moodRepository, student.getId());

            // 4. Initialize Sessions (For "Upcoming" and "Today's Schedule")
            initSessions(sessionRepository, student.getId(), doctor.getId());

            System.out.println("=== Dashboard data ready! ===");
        };
    }

    private User initDemoUsers(UserRepository userRepo, PasswordEncoder encoder) {
        // Check if Moaz exists, if not create him
        return userRepo.findByEmail("moaz@mindcare.com")
            .orElseGet(() -> {
                System.out.println("Creating demo student: Moaz...");
                User student = new User(
                    "Moaz Student", 
                    "moaz@mindcare.com", 
                    encoder.encode("password"),
                    Role.STUDENT
                );
                // Fill mandatory fields to avoid DataIntegrityViolation
                student.setPhone("0000000000"); 
                student.setDepartment("Software Engineering");
                student.setYear("4");
                student.setBio("Final year student working on MindCare.");
                
                return userRepo.save(student);
            });
    }

    private Counselor initCounselors(CounselorRepository counselorRepo) {
        if (counselorRepo.count() > 0) {
            // Return the first one found just to have a reference
            return counselorRepo.findAll().get(0);
        }

        System.out.println("Creating demo counselors...");
        
        Counselor doc1 = new Counselor(null, "Dr. Emily Carter", "Clinical Psychology", 4.9, true);
        counselorRepo.save(doc1);

        counselorRepo.save(new Counselor(null, "Dr. Sarah Johnson", "Anxiety & Depression", 4.8, true));
        counselorRepo.save(new Counselor(null, "Dr. Michael Chen", "Academic Stress", 4.7, false));
        
        return doc1; // Return Dr. Emily
    }

    private void initMoodHistory(MoodEntryRepository moodRepo, Long studentId) {
        // Only add if this user has no moods yet
        if (!moodRepo.findByUserIdOrderByEntryDateDesc(studentId).isEmpty()) {
            return;
        }

        System.out.println("Creating mood history for student...");

        List<MoodEntry> entries = List.of(
            // Past Days (for the Weekly Average Graph)
            createMood(studentId, 2, "Feeling overwhelmed.", LocalDate.now().minusDays(4), LocalTime.of(20, 0)),
            createMood(studentId, 3, "Better than yesterday.", LocalDate.now().minusDays(3), LocalTime.of(9, 0)),
            createMood(studentId, 4, "Had a nice workout.", LocalDate.now().minusDays(2), LocalTime.of(18, 30)),
            createMood(studentId, 5, "Great study session!", LocalDate.now().minusDays(1), LocalTime.of(14, 0)),

            // TODAY'S Entry (For "Recent Activity" widget)
            createMood(studentId, 4, "Starting the day fresh.", LocalDate.now(), LocalTime.of(8, 0))
        );

        moodRepo.saveAll(entries);
    }

    private void initSessions(CounsellingSessionRepository sessionRepo, Long studentId, Long counselorId) {
        // Only add if this student has no sessions
        if (!sessionRepo.findByStudentIdOrderBySessionDateAscSessionTimeAsc(studentId).isEmpty()) {
            return;
        }

        System.out.println("Creating counselling sessions...");

        // 1. Past Session (History)
        CounsellingSession past = new CounsellingSession();
        past.setCounselorId(counselorId);
        past.setCounselorName("Dr. Emily Carter");
        past.setStudentId(studentId);
        past.setSessionDate(LocalDate.now().minusDays(10));
        past.setSessionTime(LocalTime.of(10, 0));
        past.setSessionType("Individual");
        past.setStatus("Completed");
        past.setNotes("Initial consultation.");

        // 2. TODAY'S Session (Crucial for Professional Dashboard "Today's Schedule")
        CounsellingSession today = new CounsellingSession();
        today.setCounselorId(counselorId); 
        today.setCounselorName("Dr. Emily Carter");
        today.setStudentId(studentId); 
        today.setSessionDate(LocalDate.now()); // TODAY
        today.setSessionTime(LocalTime.of(14, 0)); // 2:00 PM
        today.setSessionType("Individual");
        today.setStatus("Confirmed");
        today.setNotes("Follow up on exam stress management.");

        // 3. Future Session (Crucial for Student Dashboard "Upcoming")
        CounsellingSession future = new CounsellingSession();
        future.setCounselorId(counselorId);
        future.setCounselorName("Dr. Sarah Johnson");
        future.setStudentId(studentId);
        future.setSessionDate(LocalDate.now().plusDays(5));
        future.setSessionTime(LocalTime.of(10, 0));
        future.setSessionType("Group Therapy");
        future.setStatus("Confirmed");
        future.setNotes("Peer support group introduction.");

        sessionRepo.saveAll(List.of(past, today, future));
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
}