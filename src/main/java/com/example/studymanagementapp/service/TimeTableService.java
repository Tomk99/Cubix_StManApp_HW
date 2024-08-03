package com.example.studymanagementapp.service;

import com.example.studymanagementapp.model.Semester;
import com.example.studymanagementapp.model.SpecialDay;
import com.example.studymanagementapp.model.TimeTableItem;
import com.example.studymanagementapp.repository.SpecialDayRepository;
import com.example.studymanagementapp.repository.StudentRepository;
import com.example.studymanagementapp.repository.TeacherRepository;
import com.example.studymanagementapp.repository.TimeTableItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeTableService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TimeTableItemRepository timeTableItemRepository;
    private final SpecialDayRepository specialDayRepository;

    @Autowired
    @Lazy
    private TimeTableService self;

    @Cacheable("studentTimetableResults")
    public Map<LocalDate, List<TimeTableItem>> getTimeTableForStudent(int studentId, LocalDate from, LocalDate until) {
        Map<LocalDate, List<TimeTableItem>> timeTable = new LinkedHashMap<>();

        Semester semester = Semester.fromMidSemesterDay(from);
        Semester semesterOfUntil = Semester.fromMidSemesterDay(until);
        if(!semester.equals(semesterOfUntil)) {
            throw new IllegalArgumentException("from and until should be in the same semester");
        }

        if(!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("student does not exist");
        }

        List<TimeTableItem> relevantTimeTableItems = timeTableItemRepository.findByStudentAndSemester(studentId, semester.getYear(), semester.getSemesterType());
        Map<Integer, List<TimeTableItem>> timeTableItemsByDayOfWeek = relevantTimeTableItems.stream().collect(Collectors.groupingBy(TimeTableItem::getDayOfWeek));
        List<SpecialDay> specialDaysAffected = specialDayRepository.findBySourceDayOrTargetDay(from, until);
        Map<LocalDate, List<SpecialDay>> specialDaysBySourceDay = specialDaysAffected.stream().collect(Collectors.groupingBy(SpecialDay::getSourceDay));
        Map<LocalDate, List<SpecialDay>> specialDaysByTargetDay = specialDaysAffected.stream().filter(specialDay -> specialDay.getTargetDay() != null).collect(Collectors.groupingBy(SpecialDay::getTargetDay));

        for (LocalDate day = from; !day.isAfter(until); day = day.plusDays(1)) {
            List<TimeTableItem> itemsOnDay = new ArrayList<>();
            int dayOfWeek = day.getDayOfWeek().getValue();
            List<TimeTableItem> normalItemsOnDay = timeTableItemsByDayOfWeek.get(dayOfWeek);
            if (normalItemsOnDay != null && isDayNotFreeNeitherSwapped(specialDaysBySourceDay, day)) {
                itemsOnDay.addAll(normalItemsOnDay);
            }

            Integer dayOfWeekMovedToThisDay = getDayOfWeekMovedToThisDay(specialDaysByTargetDay, day);
            if (dayOfWeekMovedToThisDay != null) {
                itemsOnDay.addAll(timeTableItemsByDayOfWeek.get(dayOfWeekMovedToThisDay));
            }

            itemsOnDay.sort(Comparator.comparing(TimeTableItem::getStartLesson));

            timeTable.put(day, itemsOnDay);
        }

        return timeTable;
    }

    @Cacheable("studentTimetableResults")
    public Map<LocalDate, List<TimeTableItem>> getTimeTableForTeacher(int teacherId, LocalDate from, LocalDate until) {
        Map<LocalDate, List<TimeTableItem>> timeTable = new LinkedHashMap<>();

        Semester semester = Semester.fromMidSemesterDay(from);
        Semester semesterOfUntil = Semester.fromMidSemesterDay(until);
        if(!semester.equals(semesterOfUntil)) {
            throw new IllegalArgumentException("from and until should be in the same semester");
        }

        if(!teacherRepository.existsById(teacherId)) {
            throw new IllegalArgumentException("student does not exist");
        }

        List<TimeTableItem> relevantTimeTableItems = timeTableItemRepository.findByTeacherAndSemester(teacherId, semester.getYear(), semester.getSemesterType());
        Map<Integer, List<TimeTableItem>> timeTableItemsByDayOfWeek = relevantTimeTableItems.stream().collect(Collectors.groupingBy(TimeTableItem::getDayOfWeek));
        List<SpecialDay> specialDaysAffected = specialDayRepository.findBySourceDayOrTargetDay(from, until);
        Map<LocalDate, List<SpecialDay>> specialDaysBySourceDay = specialDaysAffected.stream().collect(Collectors.groupingBy(SpecialDay::getSourceDay));
        Map<LocalDate, List<SpecialDay>> specialDaysByTargetDay = specialDaysAffected.stream().filter(specialDay -> specialDay.getTargetDay() != null).collect(Collectors.groupingBy(SpecialDay::getTargetDay));

        for (LocalDate day = from; !day.isAfter(until); day = day.plusDays(1)) {
            List<TimeTableItem> itemsOnDay = new ArrayList<>();
            int dayOfWeek = day.getDayOfWeek().getValue();
            List<TimeTableItem> normalItemsOnDay = timeTableItemsByDayOfWeek.get(dayOfWeek);
            if (normalItemsOnDay != null && isDayNotFreeNeitherSwapped(specialDaysBySourceDay, day)) {
                itemsOnDay.addAll(normalItemsOnDay);
            }

            Integer dayOfWeekMovedToThisDay = getDayOfWeekMovedToThisDay(specialDaysByTargetDay, day);
            if (dayOfWeekMovedToThisDay != null) {
                itemsOnDay.addAll(timeTableItemsByDayOfWeek.get(dayOfWeekMovedToThisDay));
            }

            itemsOnDay.sort(Comparator.comparing(TimeTableItem::getStartLesson));

            timeTable.put(day, itemsOnDay);
        }

        return timeTable;
    }

    public Map.Entry<LocalDate, TimeTableItem> searchTimeTableOfStudent(int studentId, LocalDate from, String courseName) {
        Map.Entry<LocalDate, TimeTableItem> result = null;

        Map<LocalDate, List<TimeTableItem>> timeTableForStudent = self.getTimeTableForStudent(studentId, from, Semester.fromMidSemesterDay(from).getSemesterEnd());

        for (Map.Entry<LocalDate, List<TimeTableItem>> entry : timeTableForStudent.entrySet()) {
            LocalDate day = entry.getKey();
            List<TimeTableItem> itemsOnDay = entry.getValue();
            for (TimeTableItem tti : itemsOnDay) {
                if(tti.getCourse().getName().toLowerCase().startsWith(courseName.toLowerCase())) {
                    result = Map.entry(day, tti);
                    break;
                }
            }
            if(result != null)
                break;
        }
        return result;

    }

    private Integer getDayOfWeekMovedToThisDay(Map<LocalDate, List<SpecialDay>> specialDaysByTargetDay, LocalDate day) {
        List<SpecialDay> movedToThisDay = specialDaysByTargetDay.get(day);
        if(movedToThisDay == null || movedToThisDay.isEmpty())
            return null;

        return movedToThisDay.get(0).getSourceDay().getDayOfWeek().getValue();
    }

    private boolean isDayNotFreeNeitherSwapped(Map<LocalDate, List<SpecialDay>> specialDaysBySourceDay, LocalDate day) {
        List<SpecialDay> specialDaysOnDay = specialDaysBySourceDay.get(day);
        return specialDaysOnDay == null || specialDaysOnDay.isEmpty();
    }

}
