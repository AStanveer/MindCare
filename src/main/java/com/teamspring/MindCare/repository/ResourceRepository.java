package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.Resource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    // Find by category
    List<Resource> findByCategory(String category);
    
    // Get all distinct categories
    @Query("SELECT DISTINCT r.category FROM Resource r ORDER BY r.category")
    List<String> findAllDistinctCategories();
    
    // Search by title
    List<Resource> findByTitleContainingIgnoreCase(String title);
    
    // Get by author
    List<Resource> findByAuthorContainingIgnoreCase(String author);
    
    // ========== CRITICAL: ADD THIS MISSING METHOD ==========
    
    // Get all resources ordered by publish date (newest first)
    List<Resource> findAllByOrderByPublishDateDesc();
    
    // Filter by category ordered by publish date
    List<Resource> findByCategoryOrderByPublishDateDesc(String category);
    
    // ========== ADVANCED SEARCH QUERIES ==========
    
    // Search across title, description, content, and tags
    @Query("SELECT r FROM Resource r WHERE " +
           "LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.tags) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY r.publishDate DESC")
    List<Resource> searchResources(@Param("query") String query);
    
    // Search within specific category
    @Query("SELECT r FROM Resource r WHERE r.category = :category AND " +
           "(LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.content) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.tags) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY r.publishDate DESC")
    List<Resource> searchByCategory(@Param("category") String category, @Param("query") String query);
}