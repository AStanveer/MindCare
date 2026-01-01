package com.teamspring.MindCare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamspring.MindCare.model.Counselor;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    List<Counselor> findByAvailableTrue();
    List<Counselor> findBySpecialty(String specialty);
    Counselor findByUserId(Long userId);
}
