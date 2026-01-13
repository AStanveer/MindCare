package com.teamspring.MindCare.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamspring.MindCare.dto.SessionDTO;
import com.teamspring.MindCare.model.CounsellingSession;
import com.teamspring.MindCare.model.Counselor;
import com.teamspring.MindCare.model.MoodEntry;
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.repository.AssessmentResultRepository;
import com.teamspring.MindCare.repository.CounsellingSessionRepository;
import com.teamspring.MindCare.repository.CounselorRepository;
import com.teamspring.MindCare.repository.MoodEntryRepository;
import com.teamspring.MindCare.repository.UserRepository;

@Service
public class DashboardService {
    
    @Autowired private CounsellingSessionRepository sessionRepo;
    @Autowired private MoodEntryRepository moodRepo;
    @Autowired private CounselorRepository counselorRepo;
    @Autowired private AssessmentResultRepository assessmentRepo;
    @Autowired private UserRepository userRepo;
    
    public List<CounsellingSession> getStudentSessions(Long studentId) {
        return sessionRepo.findByStudentIdAndSessionDateGreaterThanEqualOrderBySessionDateAscSessionTimeAsc(
            studentId, LocalDate.now()
        );
    }

    public MoodEntry getTodayMood(Long userId) {
        List<MoodEntry> entries = moodRepo.findByUserIdAndEntryDate(userId, LocalDate.now());
        
        return entries.isEmpty() ? null : entries.get(0);
    }

    public Double getWeeklyMoodAverage(Long userId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(7);
        Double average = moodRepo.findAverageMoodByUserIdAndDateRange(userId, start, end);
        return average != null ? average : 0.0;
    }

    public List<SessionDTO> getProfessionalSessions(Long currentUserId) {
        // 1. Find the Counselor profile linked to this User ID
        Counselor counselorProfile = counselorRepo.findByUserId(currentUserId);

        if (counselorProfile == null) {
            return new ArrayList<>(); 
        }

        Long realCounselorId = counselorProfile.getId();

        // 2. Get sessions for today
        List<CounsellingSession> sessions = sessionRepo.findByCounselorIdAndSessionDateOrderBySessionTimeAsc(
            realCounselorId, LocalDate.now()
        );

        List<SessionDTO> displayList = new ArrayList<>();
        
        for (CounsellingSession session : sessions) {
            User student = userRepo.findById(session.getStudentId()).orElse(new User());
            
            // Handle unknown students gracefully
            String name = (student.getFullName() != null) ? student.getFullName() : "Unknown Student";
            String email = (student.getEmail() != null) ? student.getEmail() : "No Email";

            displayList.add(new SessionDTO(
                session.getId(),
                session.getSessionDate(),
                session.getSessionTime(),
                session.getSessionType(),
                session.getStatus(),
                session.getNotes(),
                name,
                email
            ));
        }
        return displayList;
    }

    public long getAssessmentCount(Long userId) {
        return assessmentRepo.countByUserId(userId);
    }

    public User getUser(Long id) {
        return userRepo.findById(id).orElse(null);
    }
}