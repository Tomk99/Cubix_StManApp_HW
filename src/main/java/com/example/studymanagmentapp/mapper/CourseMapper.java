package com.example.studymanagmentapp.mapper;

import com.example.studymanagmentapp.dto.CourseDto;
import com.example.studymanagmentapp.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto courseToDto(Course course);
    Course dtoToCourse(CourseDto courseDto);
    List<CourseDto> coursesToDtos(Iterable<Course> all);
}
