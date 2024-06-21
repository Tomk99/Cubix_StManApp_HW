package com.example.studymanagmentapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NamedEntityGraph(name = "Course.students", attributeNodes = @NamedAttributeNode("students"))
@NamedEntityGraph(name = "Course.teachers", attributeNodes = @NamedAttributeNode("teachers"))
@Entity
public class Course {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    private String name;
    @ManyToMany
    private Set<Student> students;
    @ManyToMany
    private Set<Teacher> teachers;
}
