package com.teamspring.MindCare.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    
    @Column(nullable = false)
    private String studentId;
    
    @Column(nullable = false)
    private String department;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;
    
    @Column(nullable = false)
    private LocalDateTime joinDate = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime lastActive = LocalDateTime.now();
    
    public enum Role {
        STUDENT, PROFESSIONAL, ADMIN
    }
    
    public enum Status {
        ACTIVE, INACTIVE
    }
    
    // Constructors
    public User() {}
    
    public User(String fullName, String email, String phone, Role role, 
                String studentId, String department) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.studentId = studentId;
        this.department = department;
        this.status = Status.ACTIVE;
        this.joinDate = LocalDateTime.now();
        this.lastActive = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public LocalDateTime getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }
    
    public LocalDateTime getLastActive() {
        return lastActive;
    }
    
    public void setLastActive(LocalDateTime lastActive) {
        this.lastActive = lastActive;
    }
}
