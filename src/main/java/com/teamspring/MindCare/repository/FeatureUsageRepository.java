package com.teamspring.MindCare.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamspring.MindCare.model.FeatureUsage;

@Repository
public interface FeatureUsageRepository extends JpaRepository<FeatureUsage, Long> {
    
    /**
     * Find feature usage record for a specific user
     */
    Optional<FeatureUsage> findByUserId(Long userId);
}
