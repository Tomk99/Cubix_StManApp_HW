package com.example.studymanagementapp.service;

import com.example.studymanagementapp.model.Student;
import com.example.studymanagementapp.repository.StudentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final CentralEducationService centralEducationService;

    @Value("${studymanagementapp.content.profilePics}")
    private String profilePicsFolder;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Path.of(profilePicsFolder));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "${studymanagementapp.updateFreeSemester.cron}")
    public void updateFreeSemesters() {
        List<Student> students = studentRepository.findAll();
        students.forEach(student -> {
            int numFreeSemesters = centralEducationService.getNumFreeSemestersForStudent(student.getEduId());
            student.setNumFreeSemesters(numFreeSemesters);
            studentRepository.save(student);
            System.out.println("Number of free semesters of student "+ student.getName()+" ["+student.getEduId()+"]: "+numFreeSemesters);
        });
    }

    public void saveProfilePicture(Integer id, InputStream inputStream) {
        if (!studentRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        try {
            Files.copy(inputStream, getProfilePicPathForStudent(id), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Path getProfilePicPathForStudent(Integer id) {
        return Paths.get(profilePicsFolder, id.toString() + ".jpg");
    }

    public Resource getProfilePicture(Integer studentId) {
        FileSystemResource fileSystemResource = new FileSystemResource(getProfilePicPathForStudent(studentId));
        if (!fileSystemResource.exists()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return fileSystemResource;
    }

    @Transactional
    public void updateBalance(int studentId, int amount) {
        studentRepository.findById(studentId).ifPresent(s -> s.setBalance(s.getBalance() + amount));
    }
}
