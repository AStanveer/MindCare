package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.CounsellingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CounsellingSessionRepository extends JpaRepository<CounsellingSession, Long> {
    
    // Find all sessions for a counselor, ordered by date and time
    List<CounsellingSession> findByCounselorIdOrderBySessionDateAscSessionTimeAsc(Long counselorId);
    
    // Find all sessions for a student
    List<CounsellingSession> findByStudentIdOrderBySessionDateAscSessionTimeAsc(Long studentId);
    
    // Find upcoming sessions (on or after a specific date)
    List<CounsellingSession> findBySessionDateGreaterThanEqualOrderBySessionDateAscSessionTimeAsc(LocalDate date);
    
    // Find sessions by counselor and date range
    List<CounsellingSession> findByCounselorIdAndSessionDateBetweenOrderBySessionDateAscSessionTimeAsc(
            Long counselorId, LocalDate startDate, LocalDate endDate);
}

