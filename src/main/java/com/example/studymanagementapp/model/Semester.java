package com.example.studymanagementapp.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Builder
public class Semester {

    public enum SemesterType {
        SPRING, FALL
    }

    @EqualsAndHashCode.Include
    private int year;
    @EqualsAndHashCode.Include
    private SemesterType semesterType;
    @Transient
    private LocalDate semesterStart;
    @Transient
    private LocalDate semesterEnd;

    public static Semester fromMidSemesterDay(LocalDate midSemesterDay) {
        Semester semester = new Semester();
        semester.year = midSemesterDay.getYear();
        int month = midSemesterDay.getMonthValue();

        if (month >= 2 && month <= 5) {
            semester.semesterType = SemesterType.SPRING;
        } else if (month >= 9) {
            semester.semesterType = SemesterType.FALL;
        } else {
            throw new IllegalArgumentException("Not valid start date for the semester");
        }

        semester.semesterStart = getSemesterStart(semester.year, semester.semesterType);
        semester.semesterEnd = semester.semesterStart.plusWeeks(14).minusDays(1);
        return semester;
    }

    private static LocalDate getSemesterStart(int year, SemesterType semesterType) {
        LocalDate localDate = LocalDate.of(year, semesterType == SemesterType.FALL ? 8 : 1, 31);
        localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        return localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
    }
}
