package com.example.studymanagmentapp.web;

import com.example.studymanagmentapp.dto.CourseDto;
import com.example.studymanagmentapp.dto.HistoryDataDto;
import com.example.studymanagmentapp.mapper.CourseMapper;
import com.example.studymanagmentapp.model.Course;
import com.example.studymanagmentapp.repository.CourseRepository;
import com.example.studymanagmentapp.service.CourseService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CourseService courseService;

    @GetMapping
    private List<CourseDto> findAll(@RequestParam Optional<Boolean> full,
                                    @QuerydslPredicate(root = Course.class, bindings = CourseRepository.class) Predicate predicate,
                                    @SortDefault(sort = "id") Pageable pageable) {
        List<CourseDto> all = null;
        if (full.orElse(false)) {
            return all = courseMapper.coursesToDtos(courseService.findAllWithAllParameters(predicate, pageable));
        } else {
            return all = courseMapper.coursesToSummaryDtos(courseRepository.findAll(predicate, pageable));
        }
    }

    @GetMapping("/{id}")
    private CourseDto findById(@PathVariable int id) {
        try {
            return courseMapper.courseToDto(courseService.findByIdWithAllParameters(id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/versions")
    private List<HistoryDataDto<CourseDto>> getHistory(@PathVariable int id) {
        return courseMapper.courseHistoriesToHistoryDataCourseDtos(courseService.getHistoryById(id));
    }

    @GetMapping("/history/{id}")
    public CourseDto getVersionsAt(@PathVariable int id, @RequestParam OffsetDateTime at) {
        return courseMapper.courseToDto(courseService.getVersionAt(id, at));
    }
}
