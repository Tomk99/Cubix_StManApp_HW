package com.example.studymanagmentapp.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class CourseDto {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    private String name;
    @ManyToMany
    private List<StudentDto> students;
    @ManyToMany
    private List<TeacherDto> teachers;
}
