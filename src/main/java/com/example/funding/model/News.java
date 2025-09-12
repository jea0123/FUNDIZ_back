package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class News {
    private Long newsId;
    private Long projectId;
    private String content;
    private LocalDate createdAt;
}
