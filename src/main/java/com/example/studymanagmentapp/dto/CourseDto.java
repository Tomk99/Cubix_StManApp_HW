package com.example.studymanagmentapp.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class CourseDto {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    private String name;
    @ManyToMany(mappedBy = "course")
    private List<StudentDto> students;
    @ManyToMany(mappedBy = "course")
    private List<TeacherDto> teachers;
}
