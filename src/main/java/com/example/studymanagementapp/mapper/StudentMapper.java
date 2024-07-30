package com.example.studymanagementapp.mapper;

import com.example.studymanagementapp.api.model.StudentDto;
import com.example.studymanagementapp.model.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDto studentToDto(Student student);
}
