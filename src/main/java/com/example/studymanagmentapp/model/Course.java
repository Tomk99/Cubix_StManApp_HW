package com.example.studymanagmentapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
public class Course {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    private String name;
    @ManyToMany(mappedBy = "course")
    private List<Student> students;
    @ManyToMany(mappedBy = "course")
    private List<Teacher> teachers;
}
