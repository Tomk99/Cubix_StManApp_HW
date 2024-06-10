package com.example.studymanagmentapp.repository;

import com.example.studymanagmentapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
