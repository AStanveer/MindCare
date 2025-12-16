package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.AssessmentResult;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    List<AssessmentResult> findByUserIdOrderByCompletedAtDesc(Long userId);
    List<AssessmentResult> findByUserId(Long userId);
}