package com.example.studymanagmentapp.mapper;

import com.example.studymanagmentapp.dto.TeacherDto;
import com.example.studymanagmentapp.model.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDto teacherToDto(Teacher teacher);
}
