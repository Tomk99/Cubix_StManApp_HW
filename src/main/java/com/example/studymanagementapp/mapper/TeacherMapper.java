package com.example.studymanagementapp.mapper;

import com.example.studymanagementapp.api.model.TeacherDto;
import com.example.studymanagementapp.model.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDto teacherToDto(Teacher teacher);
}
