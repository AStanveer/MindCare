package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleItemRepository extends JpaRepository<ScheduleItem, Long> {
    List<ScheduleItem> findByStatus(String status);
    List<ScheduleItem> findByStudentEmail(String email);
}
