package com.example.studymanagementapp.repository;

import com.example.studymanagementapp.model.Semester;
import com.example.studymanagementapp.model.TimeTableItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeTableItemRepository extends JpaRepository<TimeTableItem, Integer> {

    @Query("SELECT t FROM TimeTableItem t WHERE t.course IN ("
            +	"SELECT c FROM Course c JOIN c.students s "
            + 	"WHERE s.id=:studentId AND c.semester.year = :year AND c.semester.semesterType = :semesterType"
            + ")")
    public List<TimeTableItem> findByStudentAndSemester(int studentId, int year, Semester.SemesterType semesterType);

    @Query("SELECT t FROM TimeTableItem t WHERE t.course IN ("
            +	"SELECT c FROM Course c JOIN c.teachers t "
            + 	"WHERE t.id=:teacherId AND c.semester.year = :year AND c.semester.semesterType = :semesterType"
            + ")")
    public List<TimeTableItem> findByTeacherAndSemester(int teacherId, int year, Semester.SemesterType semesterType);
}
