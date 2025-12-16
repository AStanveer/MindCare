package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.SelfCareActivity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfCareActivityRepository extends JpaRepository<SelfCareActivity, Long> {
    
    // Find by category
    List<SelfCareActivity> findByCategory(String category);
    
    // Find by content type
    List<SelfCareActivity> findByContentType(String contentType);
    
    // Find featured activities
    List<SelfCareActivity> findByFeaturedTrue();
    
    // Get all distinct categories
    @Query("SELECT DISTINCT s.category FROM SelfCareActivity s ORDER BY s.category")
    List<String> findAllDistinctCategories();
    
    // Search
    List<SelfCareActivity> findByTitleContainingIgnoreCase(String title);
}