package com.teamspring.MindCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamspring.MindCare.model.UserTemp;

@Repository
public interface UserTempRepository extends JpaRepository<UserTemp, Long> {
    UserTemp findByEmail(String email);
}