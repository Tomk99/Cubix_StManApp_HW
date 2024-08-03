package com.example.studymanagementapp.web;

import com.example.studymanagementapp.api.TimeTableControllerApi;
import com.example.studymanagementapp.api.model.TimeTableItemDto;
import com.example.studymanagementapp.mapper.TimeTableMapper;
import com.example.studymanagementapp.model.TimeTableItem;
import com.example.studymanagementapp.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TimeTableController implements TimeTableControllerApi {

    private final NativeWebRequest nativeWebRequest;
    private final TimeTableMapper timeTableMapper;
    private final TimeTableService timeTableService;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<List<TimeTableItemDto>> getTimetable(Integer studentId, Integer teacherId, LocalDate from, LocalDate until) {

        try {
            ArrayList<TimeTableItemDto> result = new ArrayList<>();

            if (studentId != null) {

                Map<LocalDate, List<TimeTableItem>> timeTableForStudent = timeTableService
                        .getTimeTableForStudent(studentId, from, until);

                for (Map.Entry<LocalDate, List<TimeTableItem>> entry : timeTableForStudent.entrySet()) {
                    LocalDate day = entry.getKey();
                    List<TimeTableItem> items = entry.getValue();
                    List<TimeTableItemDto> itemDtos = timeTableMapper.timeTableItemsToDtos(items);
                    itemDtos.forEach(i -> i.setDay(day));
                    result.addAll(itemDtos);
                }
                return ResponseEntity.ok(result);
            } else if (teacherId != null) {

                Map<LocalDate, List<TimeTableItem>> timeTableForTeacher = timeTableService
                        .getTimeTableForTeacher(teacherId, from, until);

                for (Map.Entry<LocalDate, List<TimeTableItem>> entry : timeTableForTeacher.entrySet()) {
                    LocalDate day = entry.getKey();
                    List<TimeTableItem> items = entry.getValue();
                    List<TimeTableItemDto> itemDtos = timeTableMapper.timeTableItemsToDtos(items);
                    itemDtos.forEach(i -> i.setDay(day));
                    result.addAll(itemDtos);
                }
                return ResponseEntity.ok(result);
            } else
                return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<TimeTableItemDto> searchTimeTable(Integer studentId, Integer teacherId, LocalDate from, String course) {

        Map.Entry<LocalDate, TimeTableItem> foundTimeTableEntry = timeTableService.searchTimeTableOfStudent(studentId, from, course);
        if(foundTimeTableEntry == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);


        TimeTableItemDto timeTableItemDto = timeTableMapper.timeTableItemToDto(foundTimeTableEntry.getValue());
        timeTableItemDto.setDay(foundTimeTableEntry.getKey());

        return ResponseEntity.ok(timeTableItemDto);
    }
}
