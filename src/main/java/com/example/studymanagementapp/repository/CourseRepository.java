package com.example.studymanagementapp.repository;

import com.example.studymanagementapp.model.Course;
import com.example.studymanagementapp.model.QCourse;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;
import java.util.Optional;

public interface CourseRepository
        extends JpaRepository<Course, Integer>,
        QuerydslPredicateExecutor<Course>,
        QuerydslBinderCustomizer<QCourse>,
        JpaSpecificationExecutor<Course>,
        QuerydslWithEntityGraphRepository<Course, Integer>{

    @Override
    default void customize(QuerydslBindings bindings, QCourse course) {
        bindings.bind(course.name).first(StringExpression::startsWithIgnoreCase);
        bindings.bind(course.id).first(NumberPath::eq);
        bindings.bind(course.teachers.any().name).first(StringExpression::startsWithIgnoreCase);
        bindings.bind(course.students.any().id).first(NumberPath::eq);
        bindings.bind(course.students.any().semester).all((path, value) -> {
            Integer minSemester = null;
            Integer maxSemester = null;

            for (Integer val : value) {
                if (minSemester == null) {
                    minSemester = val;
                } else {
                    maxSemester = val;
                }
            }
            if (minSemester != null && maxSemester != null) {
                return Optional.of(path.between(minSemester, maxSemester));
            } else if (minSemester != null) {
                return Optional.of(path.goe(minSemester));
            } else {
                return Optional.empty();
            }
        });
    }

    @EntityGraph(attributePaths = {"students"})
    @Query("select c from Course c where c.id = :id")
    Course findByIdWithStudents(int id);

    @EntityGraph(attributePaths = {"teachers"})
    @Query("select c from Course c where c.id = :id")
    Course findByIdWithTeachers(int id);

    @EntityGraph("Course.students")
    @Query("select c from Course c where c.id in :ids")
    List<Course> findByIdInWithStudents(List<Integer> ids, Sort sort);
    @EntityGraph("Course.teachers")
    @Query("select c from Course c where c.id in :ids")
    List<Course> findByIdInWithTeachers(List<Integer> ids, Sort sort);
}
