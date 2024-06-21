package com.example.studymanagmentapp.repository;

import com.example.studymanagmentapp.model.Course;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import java.util.List;

public class QuerydslWithEntityGraphRepositoryImpl extends SimpleJpaRepository<Course, Integer> implements QuerydslWithEntityGraphRepository<Course, Integer> {
    private PathBuilder<Course> builder;
    private Querydsl querydsl;
    private EntityPath<Course> path;
    private EntityManager entityManager;

    public QuerydslWithEntityGraphRepositoryImpl(EntityManager entityManager) {
        super(Course.class, entityManager);
        this.entityManager = entityManager;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Course.class);
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    @Override
    public List<Course> findAll(Predicate predicate, String entityGraphName, Sort sort) {
        JPAQuery query = (JPAQuery) querydsl.applySorting(sort, createQuery(predicate).select(path));
        query.setHint(EntityGraph.EntityGraphType.LOAD.getKey(), entityManager.getEntityGraph(entityGraphName));
        return query.fetch();
    }

    private JPAQuery createQuery(Predicate predicate) {
        return querydsl.createQuery(path).where(predicate);
    }
}
