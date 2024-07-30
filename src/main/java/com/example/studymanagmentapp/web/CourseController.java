package com.example.studymanagmentapp.web;

import com.example.studymanagmentapp.api.CourseControllerApi;
import com.example.studymanagmentapp.api.model.CourseDto;
import com.example.studymanagmentapp.api.model.HistoryDataCourseDto;
import com.example.studymanagmentapp.mapper.CourseMapper;
import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.repository.CourseRepository;
import com.example.studymanagmentapp.service.CourseService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class CourseController implements CourseControllerApi {

    private final NativeWebRequest nativeWebRequest;
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final QuerydslPredicateArgumentResolver predicateResolver;
    private final PageableHandlerMethodArgumentResolver pageableResolver;

    HttpHeaders headers = new HttpHeaders();

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<List<CourseDto>> findAll(Boolean full, String teachers, String name, String students, Integer id, Integer page, Integer size, String sort) {
        Pageable pageable = createPageable("configPageable");
        Predicate predicate = createPredicate("configurePredicate");
        headers.setContentType(MediaType.APPLICATION_JSON);
        boolean isFull = full != null && full;
        if (isFull) {
            Iterable<Course> courses = courseService.findAllWithAllParameters(predicate, pageable);
            return ResponseEntity.ok().headers(headers).body(courseMapper.coursesToDtos(courses));
        } else {
            Iterable<Course> courses = courseRepository.findAll(predicate, pageable).getContent();
            return ResponseEntity.ok().headers(headers).body(courseMapper.coursesToSummaryDtos(courses));
        }
    }

    @Override
    public ResponseEntity<CourseDto> findById(Integer id) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            return ResponseEntity.ok().headers(headers).body(courseMapper.courseToDto(courseService.findByIdWithAllParameters(id)));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<HistoryDataCourseDto>> getHistory(Integer id) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok().headers(headers).body(courseMapper.courseHistoriesToHistoryDataCourseDtos(courseService.getHistoryById(id)));
    }

    @Override
    public ResponseEntity<CourseDto> getVersionsAt(Integer id, LocalDateTime at) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(courseMapper.courseToDto(courseService.getVersionAt(id, at.atOffset(ZoneOffset.UTC))));
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
    private Pageable createPageable(String pageableConfigurerMethodName) {
        Method method = null;
        try {
            method = this.getClass().getMethod(pageableConfigurerMethodName, Pageable.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        MethodParameter methodParameter = new MethodParameter(method, 0);
        ModelAndViewContainer mavContainer = null;
        WebDataBinderFactory binderFactory = null;
        return pageableResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);
    }
    public void configurePredicate(@QuerydslPredicate(root = Course.class) Predicate predicate) {}
    public void configPageable(@SortDefault("id") org.springframework.data.domain.Pageable pageable) {}
}
