package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.AssessmentResult;
import com.teamspring.MindCare.repository.AssessmentResultRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {
    
    @Autowired
    private AssessmentResultRepository assessmentResultRepository;
    
    // Submit assessment
    public AssessmentResult submitAssessment(int[] answers) {
        AssessmentResult result = new AssessmentResult(answers);
        return assessmentResultRepository.save(result);
    }
    
    // Get all results for a user
    public List<AssessmentResult> getUserResults(Long userId) {
        return assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
    }
    
    // Get specific result
    public AssessmentResult getResultById(Long resultId) {
        return assessmentResultRepository.findById(resultId)
            .orElseThrow(() -> new RuntimeException("Result not found"));
    }
    
    // Get latest result
    public AssessmentResult getLatestUserResult(Long userId) {
        List<AssessmentResult> results = getUserResults(userId);
        return results.isEmpty() ? null : results.get(0);
    }
    
    // Delete result
    public void deleteResult(Long resultId) {
        assessmentResultRepository.deleteById(resultId);
    }
}