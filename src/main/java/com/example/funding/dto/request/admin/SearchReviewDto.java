package com.example.funding.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchReviewDto {
    private String status = "UNDER_REVIEW";
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer page = 1;
    private Integer size = 20;

    public LocalDate getToDateEndExclusive() {
        return toDate == null ? null : toDate.plusDays(1);
    }
}
