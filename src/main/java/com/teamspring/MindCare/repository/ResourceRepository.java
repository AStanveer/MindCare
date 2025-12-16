package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.Resource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    // Find by category
    List<Resource> findByCategory(String category);
    
    // Find featured resources
    List<Resource> findByFeaturedTrue();
    
    // Get all distinct categories
    @Query("SELECT DISTINCT r.category FROM Resource r ORDER BY r.category")
    List<String> findAllDistinctCategories();
    
    // Search
    List<Resource> findByTitleContainingIgnoreCase(String title);
    
    // Get by author
    List<Resource> findByAuthorContainingIgnoreCase(String author);
}