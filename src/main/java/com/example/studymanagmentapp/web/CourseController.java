package com.example.studymanagmentapp.web;

import com.example.studymanagmentapp.api.CourseControllerApi;
import com.example.studymanagmentapp.api.model.CourseDto;
import com.example.studymanagmentapp.api.model.HistoryDataDtoCourseDto;
import com.example.studymanagmentapp.api.model.Pageable;
import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.repository.CourseRepository;
import com.example.studymanagmentapp.service.CourseService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CourseController implements CourseControllerApi {

    private final NativeWebRequest nativeWebRequest;
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final QuerydslPredicateArgumentResolver predicateResolver;
    private final PageableHandlerMethodArgumentResolver pageableResolver;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<List<CourseDto>> findAll(Pageable pageable, Boolean full, String teachers, String name, String students, Integer id) {
        return CourseControllerApi.super.findAll(pageable, full, teachers, name, students, id);
    }

    @Override
    public ResponseEntity<CourseDto> findById(Integer id) {
        return CourseControllerApi.super.findById(id);
    }

    @Override
    public ResponseEntity<CourseDto> getHistory(Integer id, LocalDateTime at) {
        return CourseControllerApi.super.getHistory(id, at);
    }

    @Override
    public ResponseEntity<List<HistoryDataDtoCourseDto>> getVersionsAt(Integer id) {
        return CourseControllerApi.super.getVersionsAt(id);
    }

    private Predicate createPredicate(String configMethodName) {
        Method method = null;
        try {
            method = this.getClass().getMethod(configMethodName, Predicate.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        MethodParameter methodParameter = new MethodParameter(method, 0);
        ModelAndViewContainer mavContainer = null;
        WebDataBinderFactory binderFactory = null;
        try {
            return (Predicate) predicateResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private org.springframework.data.domain.Pageable createPageable(String pageableConfigurerMethodName) {
        Method method = null;
        try {
            method = this.getClass().getMethod(pageableConfigurerMethodName, org.springframework.data.domain.Pageable.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        MethodParameter methodParameter = new MethodParameter(method, 0);
        ModelAndViewContainer mavContainer = null;
        WebDataBinderFactory binderFactory = null;
        org.springframework.data.domain.Pageable pageable = pageableResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);
        return pageable;
    }
    public void configurePredicate(@QuerydslPredicate(root = Course.class) Predicate predicate) {}
    public void configPageable(@SortDefault("id") org.springframework.data.domain.Pageable pageable) {}
}
