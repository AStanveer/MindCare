package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<User> findByResetToken(String resetToken);
    
    List<User> findByRole(Role role);
    
    Optional<User> findByStudentId(String studentId);
    
    boolean existsByStudentId(String studentId);
    
    List<User> findByDepartment(String department);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchByName(@Param("name") String name);
    
    long countByRole(Role role);
    
    long countByIsActive(boolean isActive);

}