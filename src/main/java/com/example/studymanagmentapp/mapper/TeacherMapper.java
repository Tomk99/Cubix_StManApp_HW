package com.example.studymanagmentapp.mapper;

import com.example.studymanagmentapp.api.model.TeacherDto;
import com.example.studymanagmentapp.model.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDto teacherToDto(Teacher teacher);
}
