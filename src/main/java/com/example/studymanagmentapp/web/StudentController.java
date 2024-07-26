package com.example.studymanagmentapp.web;

import com.example.studymanagmentapp.api.StudentControllerApi;
import com.example.studymanagmentapp.api.model.StudentDto;
import com.example.studymanagmentapp.mapper.StudentMapper;
import com.example.studymanagmentapp.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentControllerApi {

    private final NativeWebRequest nativeWebRequest;
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<StudentDto> findStudentById(Integer id) {
        return ResponseEntity.ok(studentMapper.studentToDto(studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))));
    }
}
