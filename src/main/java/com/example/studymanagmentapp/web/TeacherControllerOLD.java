package com.example.studymanagmentapp.web;

import com.example.studymanagmentapp.dto.TeacherDto;
import com.example.studymanagmentapp.mapper.TeacherMapper;
import com.example.studymanagmentapp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/teachers")
public class TeacherControllerOLD {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @GetMapping("/{id}")
    public TeacherDto findById(@PathVariable int id) {
        return teacherMapper.teacherToDto(teacherRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
