package com.example.studymanagmentapp.mapper;

import com.example.studymanagmentapp.dto.StudentDto;
import com.example.studymanagmentapp.model.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDto studentToDto(Student student);
}
