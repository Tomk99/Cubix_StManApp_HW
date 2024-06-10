package com.example.studymanagmentapp.repository;

import com.example.studymanagmentapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
