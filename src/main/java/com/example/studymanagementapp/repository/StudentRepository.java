package com.example.studymanagementapp.repository;

import com.example.studymanagementapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
