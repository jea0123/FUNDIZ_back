package com.example.funding.dto.request.project;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectUpdateRequestDto {
    private Long projectId;
    private Long subctgrId;
    private String title;
    private String content;
    private String thumbnail;
    private Integer goalAmount;
    private LocalDate startDate;
    private LocalDate endDate;
}
