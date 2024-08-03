package com.example.studymanagementapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Audited
@Builder
public class TimeTableItem {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;
    private int dayOfWeek;
    private LocalTime startLesson;
    private LocalTime endLesson;
    @ManyToOne
    private Course course;
}
