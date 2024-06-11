package com.example.studymanagmentapp.web;

import com.example.studymanagmentapp.dto.CourseDto;
import com.example.studymanagmentapp.mapper.CourseMapper;
import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.repository.CourseRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @GetMapping
    private List<CourseDto> findAll(@RequestParam Optional<Boolean> full,
                                    @QuerydslPredicate(root = Course.class, bindings = CourseRepository.class) Predicate predicate) {
        return courseMapper.coursesToDtos(courseRepository.findAll(predicate));
    }
}
