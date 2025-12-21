package com.teamspring.MindCare.repository;

import com.teamspring.MindCare.model.AvailabilityDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityDatesRepository extends JpaRepository<AvailabilityDates, Long> {
    Optional<AvailabilityDates> findByCounselorId(Long counselorId);
    List<AvailabilityDates> findAll();
}
