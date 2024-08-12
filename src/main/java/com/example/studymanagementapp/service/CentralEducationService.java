package com.example.studymanagementapp.service;

import com.example.smaservice.wsclient.StudentXmlWsImplService;
import com.example.studymanagementapp.aspect.Retry;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CentralEducationService {
//    private Random random = new Random();

    @Retry
    public int getNumFreeSemestersForStudent(int eduId) {

        return new StudentXmlWsImplService().getStudentXmlWsImplPort().getFreeSemestersByStudent(eduId);

//        if (random.nextInt(0, 2) == 0) {
//            throw new RuntimeException("Service timed out");
//        } else {
//            return random.nextInt(0, 10);
//        }
    }
}
