package com.example.studymanagmentapp.service;

import com.example.studymanagmentapp.aspect.Retry;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CentralEducationService {
    private Random random = new Random();

    @Retry
    public int getNumFreeSemestersForStudent(int eduId) {
        if (random.nextInt(0, 2) == 0) {
            throw new RuntimeException("Service timed out");
        } else {
            return random.nextInt(0, 10);
        }
    }
}
