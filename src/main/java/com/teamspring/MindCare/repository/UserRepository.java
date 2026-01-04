package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.model.User.Role;
import com.teamspring.MindCare.model.User.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    List<User> findByRole(Role role);
    
    List<User> findByStatus(Status status);
    
    List<User> findByRoleAndStatus(Role role, Status status);
    
    long countByRole(Role role);
    
    long countByStatus(Status status);
}
