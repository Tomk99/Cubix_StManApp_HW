package com.example.studymanagmentapp.service;

import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.model.HistoryData;
import com.example.studymanagmentapp.model.QCourse;
import com.example.studymanagmentapp.repository.CourseRepository;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NamedEntityGraph;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EntityManager entityManager;

    @Transactional
    @Cacheable("courseSearchResults")
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

    @Transactional
    @SuppressWarnings({"rawtypes","unchecked"})
    public List<HistoryData<Course>> getHistoryById(int id) {

        return AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntity(Course.class, false, true)
                .add(AuditEntity.property("id").eq(id))
                .getResultList()
                .stream()
                .map(o -> {
                    Object[] objects = (Object[]) o;

                    DefaultRevisionEntity defaultRevisionEntity = (DefaultRevisionEntity) objects[1];
                    RevisionType revType = (RevisionType) objects[2];

                    Course course = (Course) objects[0];
                    fetchRelationships(course);

                    return new HistoryData<Course>(course, revType, defaultRevisionEntity.getId(), defaultRevisionEntity.getRevisionDate());
                }).toList();
    }

    private static void fetchRelationships(Course course) {
        course.getStudents().size();
        course.getTeachers().size();
    }

    @Transactional
    public Course getVersionAt(int id, OffsetDateTime at) {
        Course course = AuditReaderFactory.get(entityManager).find(Course.class, id, Date.from(at.toInstant()));
        if (course == null) return null;
        fetchRelationships(course);
        return course;
    }
}
