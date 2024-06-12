package com.example.studymanagmentapp.service;

import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.repository.CourseRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public Iterable<Course> findAllWithAllParameters(Predicate predicate, Pageable pageable) {
        Iterable<Course> all = courseRepository.findAllWithStudents(pageable);
        all = courseRepository.findAllWithTeachers(pageable);
        all = courseRepository.findAll(predicate, pageable);
        return all;
    }

    @Transactional
    public Course findByIdWithAllParameters(int id) {
        Course byId = courseRepository.findByIdWithStudents(id);
        byId = courseRepository.findByIdWithTeachers(id);
        if (byId == null) throw new NoSuchElementException();
        return byId;
    }
}
