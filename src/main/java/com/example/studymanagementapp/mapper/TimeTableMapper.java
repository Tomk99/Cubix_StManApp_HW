package com.example.studymanagementapp.mapper;

import com.example.studymanagementapp.api.model.TimeTableItemDto;
import com.example.studymanagementapp.model.TimeTableItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeTableMapper {

    @Mapping(target = "courseName", source="course.name")
    public TimeTableItemDto timeTableItemToDto(TimeTableItem item);

    public List<TimeTableItemDto> timeTableItemsToDtos(List<TimeTableItem> items);

}