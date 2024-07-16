package com.example.studymanagmentapp.service;

import com.example.studymanagmentapp.model.Student;
import com.example.studymanagmentapp.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final CentralEducationService centralEducationService;

    @Scheduled(cron = "${studymanagementapp.updateFreeSemester.cron}")
    public void updateFreeSemesters() {
        List<Student> students = studentRepository.findAll();
        students.forEach(student -> {
            int numFreeSemesters = centralEducationService.getNumFreeSemestersForStudent(student.getEduId());
            student.setNumFreeSemesters(numFreeSemesters);
            studentRepository.save(student);
            System.out.println("Number of free semesters of student "+ student.getName()+" ["+student.getEduId()+"]: "+numFreeSemesters);
        });
    }
}
