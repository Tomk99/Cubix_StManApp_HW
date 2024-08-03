package com.example.studymanagementapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.HashSet;
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
@Audited
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
    @OneToMany(mappedBy = "course")
    private Set<TimeTableItem> timeTableItems;

    private Semester semester;

    public void addTimeTableItem(TimeTableItem timeTableItem) {
        timeTableItem.setCourse(this);
        if (this.timeTableItems == null) {
            this.timeTableItems = new HashSet<>();
        }
        this.timeTableItems.add(timeTableItem);
    }
}
