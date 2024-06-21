package com.example.studymanagmentapp.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
public class CourseDto {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    private String name;
    @ManyToMany
    private Set<StudentDto> students;
    @ManyToMany
    private Set<TeacherDto> teachers;
}
