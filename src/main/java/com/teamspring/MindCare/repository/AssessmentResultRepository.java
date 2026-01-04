package com.teamspring.MindCare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamspring.MindCare.model.AssessmentResult;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    List<AssessmentResult> findByUserIdOrderByCompletedAtDesc(Long userId);
    List<AssessmentResult> findByUserId(Long userId);
    long countByUserId(Long userId);
}