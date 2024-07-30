package com.example.studymanagementapp.repository;

import com.example.studymanagementapp.model.Course;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public Page<Course> findAll(Predicate predicate, String entityGraphName, Pageable pageable) {
        JPAQuery query = (JPAQuery) querydsl.applyPagination(pageable, createQuery(predicate).select(path));
        query.setHint(EntityGraph.EntityGraphType.LOAD.getKey(), entityManager.getEntityGraph(entityGraphName));

        long total = query.fetchCount();
        List<Course> content = query.fetch();

        return new PageImpl<>(content, pageable, total);
    }

    private JPAQuery createQuery(Predicate predicate) {
        return querydsl.createQuery(path).where(predicate);
    }
}
