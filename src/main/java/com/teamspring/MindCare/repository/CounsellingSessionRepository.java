package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.CounsellingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounsellingSessionRepository extends JpaRepository<CounsellingSession, Long> {
    List<CounsellingSession> findAll();
}
