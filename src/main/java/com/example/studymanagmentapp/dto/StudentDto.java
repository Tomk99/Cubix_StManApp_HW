package com.example.studymanagmentapp.dto;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class StudentDto {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;

    private String name;
    private LocalDate birthdate;
    private int semester;
}
