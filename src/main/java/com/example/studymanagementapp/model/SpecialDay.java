package com.example.studymanagementapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Audited
@Builder
public class SpecialDay {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;
    private LocalDate sourceDay;
    private LocalDate targetDay;
}
