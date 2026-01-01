package com.teamspring.MindCare.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamspring.MindCare.model.CounsellingSession;

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

    // METHODS REQUIRED FOR DASHBOARD

    // Finds sessions for specific student that are today or in the future
    List<CounsellingSession> findByStudentIdAndSessionDateGreaterThanEqualOrderBySessionDateAscSessionTimeAsc(Long studentId, LocalDate date);
    
    // Finds sessions for specific counselor that match a specific date
    List<CounsellingSession> findByCounselorIdAndSessionDateOrderBySessionTimeAsc(Long counselorId, LocalDate date);

}

