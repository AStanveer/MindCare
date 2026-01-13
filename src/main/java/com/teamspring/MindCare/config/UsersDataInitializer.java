package com.teamspring.MindCare.config;

import com.teamspring.MindCare.model.Role;
import com.teamspring.MindCare.model.User;
import com.teamspring.MindCare.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsersDataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            
            // 1. Create ADMIN (if not exists)
            if (!userRepository.existsByEmail("admin@mindcare.com")) {
                User admin = new User(
                    "System Administrator", 
                    "admin@mindcare.com", 
                    passwordEncoder.encode("admin123"), // Encoded password
                    Role.ADMIN
                );
                admin.setPhone("0123456789"); // Dummy phone to satisfy any strict regex
                admin.setBio("I am the system super-admin.");
                
                userRepository.save(admin);
                System.out.println("✅ ADMIN Created: admin@mindcare.com / admin123");
            }

            // 2. Create STUDENT (for testing)
            if (!userRepository.existsByEmail("student@mindcare.com")) {
                User student = new User(
                    "Test Student", 
                    "student@mindcare.com", 
                    passwordEncoder.encode("student123"), 
                    Role.STUDENT
                );
                student.setPhone("0111222333");
                student.setStudentId("A23CS0001"); // Sample Matric ID
                student.setYear("3");
                student.setDepartment("Software Engineering");
                
                userRepository.save(student);
                System.out.println("✅ STUDENT Created: student@mindcare.com / student123");
            }

            // 3. Create PROFESSIONAL (for testing)
            if (!userRepository.existsByEmail("pro@mindcare.com")) {
                User pro = new User(
                    "Dr. Sarah", 
                    "pro@mindcare.com", 
                    passwordEncoder.encode("pro123"), 
                    Role.PROFESSIONAL
                );
                pro.setPhone("0155566677");
                pro.setLicenseNumber("LIC-998877");
                pro.setExperience("10 Years");
                pro.setLocation("Health Center A");
                
                userRepository.save(pro);
                System.out.println("✅ PROFESSIONAL Created: pro@mindcare.com / pro123");
            }
        };
    }
}