package com.example.studymanagmentapp.repository;

import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.model.QCourse;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface CourseRepository
        extends JpaRepository<Course, Integer>,
        QuerydslPredicateExecutor<Course>,
        QuerydslBinderCustomizer<QCourse> {

    @Override
    default void customize(QuerydslBindings bindings, QCourse course) {
        bindings.bind(course.name).first(StringExpression::startsWithIgnoreCase);
        bindings.bind(course.id).first(NumberPath::eq);
    };
}
