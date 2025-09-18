package com.example.funding.dto.row;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewListRow {
    private Long projectId;
    private String title;
    private String creatorName;
    private Long ctgrId;
    private String ctgrName;
    private Long subctgrId;
    private String subctgrName;
    private Integer goalAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String projectStatus;
    private LocalDate requestedAt;
}
