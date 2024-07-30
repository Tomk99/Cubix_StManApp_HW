package com.example.studymanagementapp.mapper;

import com.example.studymanagementapp.api.model.CourseDto;
import com.example.studymanagementapp.api.model.HistoryDataCourseDto;
import com.example.studymanagementapp.model.Course;
import com.example.studymanagementapp.model.HistoryData;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDto courseToDto(Course course);
    @Named("summary")
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "teachers", ignore = true)
    CourseDto courseToSummaryDto(Course course);
    Course dtoToCourse(CourseDto courseDto);
    List<CourseDto> coursesToDtos(Iterable<Course> all);
    @IterableMapping(qualifiedByName = "summary")
    List<CourseDto> coursesToSummaryDtos(Iterable<Course> all);

    List<HistoryDataCourseDto> courseHistoriesToHistoryDataCourseDtos(List<HistoryData<Course>> historyById);
}
