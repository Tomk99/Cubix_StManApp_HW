package com.example.studymanagementapp.web;

import com.example.studymanagementapp.api.StudentControllerApi;
import com.example.studymanagementapp.api.model.StudentDto;
import com.example.studymanagementapp.mapper.StudentMapper;
import com.example.studymanagementapp.repository.StudentRepository;
import com.example.studymanagementapp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentControllerApi {

    private final NativeWebRequest nativeWebRequest;
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    HttpHeaders headers = new HttpHeaders();


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    public ResponseEntity<StudentDto> findStudentById(Integer id) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok().headers(headers).body(studentMapper.studentToDto(studentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))));
    }

    @Override
    public ResponseEntity<Resource> getProfilePicture(Integer id) {
        return ResponseEntity.ok(studentService.getProfilePicture(id));
    }

    @Override
    public ResponseEntity<Void> uploadProfilePicture(Integer id, MultipartFile content) {
        try {
            studentService.saveProfilePicture(id, content.getInputStream());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
