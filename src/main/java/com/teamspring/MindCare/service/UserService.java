package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.model.User.Role;
import com.teamspring.MindCare.model.User.Status;
import com.teamspring.MindCare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
    
    public List<User> getUsersByStatus(Status status) {
        return userRepository.findByStatus(status);
    }
    
    public long getTotalUsers() {
        return userRepository.count();
    }
    
    public long getStudentsCount() {
        return userRepository.countByRole(Role.STUDENT);
    }
    
    public long getProfessionalsCount() {
        return userRepository.countByRole(Role.PROFESSIONAL);
    }
    
    public long getAdminsCount() {
        return userRepository.countByRole(Role.ADMIN);
    }
    
    public long getActiveUsersCount() {
        return userRepository.countByStatus(Status.ACTIVE);
    }
    
    public long getInactiveUsersCount() {
        return userRepository.countByStatus(Status.INACTIVE);
    }
    
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    public void deactivateUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(Status.INACTIVE);
            userRepository.save(user);
        }
    }
    
    public void activateUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(Status.ACTIVE);
            user.setLastActive(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
