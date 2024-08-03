package com.example.studymanagementapp.repository;

import com.example.studymanagementapp.model.SpecialDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SpecialDayRepository extends JpaRepository<SpecialDay, Integer> {

    @Query("SELECT s from SpecialDay s WHERE s.sourceDay BETWEEN :from and :until OR s.targetDay BETWEEN :from and :until")
    List<SpecialDay> findBySourceDayOrTargetDay(LocalDate from, LocalDate until);
}
