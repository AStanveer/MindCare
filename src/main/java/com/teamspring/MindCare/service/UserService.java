package com.teamspring.MindCare.service;

import com.teamspring.MindCare.model.Role;
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* =========================
       REGISTRATION & AUTH
       ========================= */

    public User registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (user.getStudentId() != null && !user.getStudentId().isBlank()) {
            if (userRepository.existsByStudentId(user.getStudentId())) {
                throw new RuntimeException("Student ID already exists");
            }
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setJoinDate(LocalDateTime.now());
        user.setLastActive(LocalDateTime.now());
        user.setActive(true);

        return userRepository.save(user);
    }

    public User authenticateUser(String email, String password) {

        User user = getUserByEmail(email);

        if (!user.isActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        user.updateLastActive();
        return userRepository.save(user);
    }

    /* =========================
       SAFE RETRIEVAL
       ========================= */

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found with email: " + email)
                );
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    /* =========================
       UPDATE PROFILE (FIXED)
       ========================= */

    public User updateUser(Long id, User incoming) {

        User existing = getUserById(id);

        /* Email */
        if (incoming.getEmail() != null &&
            !incoming.getEmail().equals(existing.getEmail())) {

            if (userRepository.existsByEmail(incoming.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            existing.setEmail(incoming.getEmail());
        }

        if (incoming.getPhone() != null) {
            existing.setPhone(incoming.getPhone());
        }

        if (incoming.getDepartment() != null) {
            existing.setDepartment(incoming.getDepartment());
        }

        if (incoming.getBio() != null) {
            existing.setBio(incoming.getBio());
        }

        if (incoming.getProfilePicture() != null) {
        existing.setProfilePicture(incoming.getProfilePicture());

}


        if (incoming.getLocation() != null) {
            existing.setLocation(incoming.getLocation());
        }

        if (incoming.getYear() != null) {
            existing.setYear(incoming.getYear());
        }

        if (incoming.getLicenseNumber() != null) {
            existing.setLicenseNumber(incoming.getLicenseNumber());
        }

        if (incoming.getExperience() != null) {
            existing.setExperience(incoming.getExperience());
        }

        return userRepository.save(existing);
    }

    /* =========================
       PASSWORD
       ========================= */

    public void changePassword(Long userId, String oldPassword, String newPassword) {

        User user = getUserById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void initiatePasswordReset(String email) {

        User user = getUserByEmail(email);

        user.setResetToken(UUID.randomUUID().toString());
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(user);
    }

    public void resetPassword(String resetToken, String newPassword) {

        User user = userRepository.findByResetToken(resetToken)
                .orElseThrow(() ->
                        new RuntimeException("Invalid reset token")
                );

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }

    /* =========================
       ADMIN / STATS
       ========================= */

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> searchUsersByName(String name) {
        return userRepository.searchByName(name);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public long countUsersByRole(Role role) {
        return userRepository.countByRole(role);
    }

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getActiveUsersCount() {
        return userRepository.countByIsActive(true);
    }

    public long getInactiveUsersCount() {
        return userRepository.countByIsActive(false);
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

    public void deactivateUser(Long id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long id) {
        User user = getUserById(id);
        user.setActive(true);
        userRepository.save(user);
    }
}
