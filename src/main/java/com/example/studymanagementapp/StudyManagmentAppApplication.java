package com.example.studymanagementapp;

import com.example.studymanagementapp.service.InitDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@RequiredArgsConstructor
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class StudyManagmentAppApplication implements CommandLineRunner {

    private final InitDbService initDbService;

    public static void main(String[] args) {
        SpringApplication.run(StudyManagmentAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initDbService.deleteDb();
        initDbService.deleteAudTables();
        initDbService.addInitData();
        initDbService.updateNamesForTestingEnvers();
    }
}
