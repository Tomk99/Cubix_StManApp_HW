package com.example.studymanagmentapp.service;

import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.model.QCourse;
import com.example.studymanagmentapp.repository.CourseRepository;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.NamedEntityGraph;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public List<Course> findAllWithAllParameters(Predicate predicate, Pageable pageable) {
        List<Course> all = courseRepository.findAll(predicate, pageable).getContent();
        List<Integer> idsOnPage = all.stream().map(Course::getId).toList();
        all = courseRepository.findByIdInWithStudents(idsOnPage, Sort.unsorted());
        all = courseRepository.findByIdInWithTeachers(idsOnPage, pageable.getSort());
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
