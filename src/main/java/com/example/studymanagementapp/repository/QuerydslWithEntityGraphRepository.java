package com.example.studymanagementapp.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface QuerydslWithEntityGraphRepository<T,ID> {
    Page<T> findAll(Predicate predicate, String entityGraphName, Pageable pageable);
}
