package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.Counselor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Long> {
    List<Counselor> findByAvailableTrue();
    List<Counselor> findBySpecialty(String specialty);
}
