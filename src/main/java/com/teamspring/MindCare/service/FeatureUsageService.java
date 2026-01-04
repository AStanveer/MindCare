package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.FeatureUsage;
import com.teamspring.MindCare.repository.FeatureUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureUsageService {
    
    @Autowired
    private FeatureUsageRepository featureUsageRepository;
    
    /**
     * Get or create feature usage record for a user
     */
    public FeatureUsage getOrCreateUserFeatureUsage(Long userId) {
        return featureUsageRepository.findByUserId(userId)
            .orElseGet(() -> {
                FeatureUsage newUsage = new FeatureUsage();
                newUsage.setUserId(userId);
                return featureUsageRepository.save(newUsage);
            });
    }
    
    /**
     * Increment self-care feature usage counter
     */
    public FeatureUsage incrementSelfCareUsage(Long userId) {
        FeatureUsage usage = getOrCreateUserFeatureUsage(userId);
        usage.setSelfCare(usage.getSelfCare() + 1);
        return featureUsageRepository.save(usage);
    }
    
    /**
     * Increment assessments feature usage counter
     */
    public FeatureUsage incrementAssessmentsUsage(Long userId) {
        FeatureUsage usage = getOrCreateUserFeatureUsage(userId);
        usage.setAssessments(usage.getAssessments() + 1);
        return featureUsageRepository.save(usage);
    }
    
    /**
     * Increment peer support feature usage counter
     */
    public FeatureUsage incrementPeerSupportUsage(Long userId) {
        FeatureUsage usage = getOrCreateUserFeatureUsage(userId);
        usage.setPeerSupport(usage.getPeerSupport() + 1);
        return featureUsageRepository.save(usage);
    }
    
    /**
     * Increment resources feature usage counter
     */
    public FeatureUsage incrementResourcesUsage(Long userId) {
        FeatureUsage usage = getOrCreateUserFeatureUsage(userId);
        usage.setResources(usage.getResources() + 1);
        return featureUsageRepository.save(usage);
    }
    
    /**
     * Increment counselling feature usage counter
     */
    public FeatureUsage incrementCounsellingUsage(Long userId) {
        FeatureUsage usage = getOrCreateUserFeatureUsage(userId);
        usage.setCounselling(usage.getCounselling() + 1);
        return featureUsageRepository.save(usage);
    }
    
    /**
     * Get feature usage for a specific user
     */
    public FeatureUsage getUserFeatureUsage(Long userId) {
        return getOrCreateUserFeatureUsage(userId);
    }
}
