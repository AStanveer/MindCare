package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.AssessmentResult;
import com.teamspring.MindCare.repository.AssessmentResultRepository;
import com.teamspring.MindCare.security.CurrentUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AssessmentService {
    
    @Autowired
    private AssessmentResultRepository assessmentResultRepository;

    @Autowired
    private CurrentUserService currentUserService;
    
    // Submit assessment
    public AssessmentResult submitAssessment(int[] answers) {
        Long userId = currentUserService.getUserId();
        AssessmentResult result = new AssessmentResult(answers);
        result.setUserId(userId);
        return assessmentResultRepository.save(result);
    }
    
    // Get all results for a user
    public List<AssessmentResult> getUserResults() {
        Long userId = currentUserService.getUserId();
        return assessmentResultRepository.findByUserIdOrderByCompletedAtDesc(userId);
    }
    
    // Get specific result
    public AssessmentResult getResultById(Long resultId) {
        return assessmentResultRepository.findById(resultId)
            .orElseThrow(() -> new RuntimeException("Result not found"));
    }
    
    // Get latest result
    public AssessmentResult getLatestUserResult() {
        List<AssessmentResult> results = getUserResults();
        return results.isEmpty() ? null : results.get(0);
    }
    
    // Delete result
    public void deleteResult(Long resultId) {
        assessmentResultRepository.deleteById(resultId);
    }
}