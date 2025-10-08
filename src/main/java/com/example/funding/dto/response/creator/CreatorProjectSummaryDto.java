package com.example.funding.dto.response.creator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CreatorProjectSummaryDto {
    private Long projectId;
    private String title;
    private LocalDate endDate;
    private String projectStatus;
}
