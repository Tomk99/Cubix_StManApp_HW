package com.example.studymanagmentapp.dto;


import lombok.*;
import org.hibernate.envers.RevisionType;

import java.util.Date;

@Getter
@Setter
public class HistoryDataDto<T> {
    private T data;
    private RevisionType revType;
    private int revision;
    private Date date;
}
