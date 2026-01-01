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
import com.teamspring.MindCare.model.UserTemp;
import com.teamspring.MindCare.repository.CounsellingSessionRepository;
import com.teamspring.MindCare.repository.CounselorRepository;
import com.teamspring.MindCare.repository.MoodEntryRepository;
import com.teamspring.MindCare.repository.UserTempRepository;

@Service
public class DashboardService {
    @Autowired private CounsellingSessionRepository sessionRepo;
    @Autowired private UserTempRepository userRepo;
    @Autowired private MoodEntryRepository moodRepo;
    @Autowired private CounselorRepository counselorRepo;

    public List<CounsellingSession> getStudentSessions(Long studentId) {
        return sessionRepo.findByStudentIdAndSessionDateGreaterThanEqualOrderBySessionDateAscSessionTimeAsc(
            studentId, LocalDate.now()
        );
    }

    public MoodEntry getTodayMood(Long userId) {
        List<MoodEntry> entires = moodRepo.findTodayEntriesByUserId(userId);

        return entires.isEmpty()? null: entires.get(0);
    }

    public Double getWeeklyMoodAverage(Long userId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(7);
        Double average = moodRepo.findAverageMoodByUserIdAndDateRange(userId, start, end);
        return average != null? average: 0.0;
    }

    public List<SessionDTO> getProfessionalSessions(Long currentUserId) {
        Counselor counselorProfile = counselorRepo.findByUserId(currentUserId);

        if (counselorProfile == null) {
            return new ArrayList<>(); 
        }

        Long realCounselorId = counselorProfile.getId();

        List<CounsellingSession> sessions = sessionRepo.findByCounselorIdAndSessionDateOrderBySessionTimeAsc(
            realCounselorId, LocalDate.now()
        );

        List<SessionDTO> displayList = new ArrayList<>();
        for (CounsellingSession session : sessions) {
            UserTemp student = userRepo.findById(session.getStudentId()).orElse(new UserTemp());
            if(student.getFullName() == null) student.setFullName("Unknown Student");

            displayList.add(new SessionDTO(
                session.getId(),
                session.getSessionDate(),
                session.getSessionTime(),
                session.getSessionType(),
                session.getStatus(),
                session.getNotes(),
                student.getFullName(),
                student.getEmail()
            ));
        }
        return displayList;
    }

    public UserTemp getUser(Long id) {
        return userRepo.findById(id).orElse(null);
    }
}
