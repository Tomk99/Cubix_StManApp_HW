package com.example.studymanagementapp.web;

import com.example.studymanagementapp.api.TeacherControllerApi;
import com.example.studymanagementapp.api.model.TeacherDto;
import com.example.studymanagementapp.mapper.TeacherMapper;
import com.example.studymanagementapp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TeacherController implements TeacherControllerApi {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return TeacherControllerApi.super.getRequest();
    }

    @Override
    public ResponseEntity<TeacherDto> findTeacherById(Integer id) {
        return ResponseEntity.ok(
                teacherMapper.teacherToDto(
                        teacherRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))));
    }
}
