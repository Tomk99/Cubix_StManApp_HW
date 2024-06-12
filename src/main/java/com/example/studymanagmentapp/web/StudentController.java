package com.example.studymanagmentapp.web;

import com.example.studymanagmentapp.dto.StudentDto;
import com.example.studymanagmentapp.mapper.StudentMapper;
import com.example.studymanagmentapp.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @GetMapping("/{id}")
    public StudentDto findById(@PathVariable int id) {
        return studentMapper.studentToDto(studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }
}
