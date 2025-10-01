package com.example.funding.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchAdminProjectDto {
    private String projectStatus;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String rangeType;

    public void applyRangeType() {
        if (rangeType != null) {
            LocalDate today = LocalDate.now();

            switch (rangeType) {
                case "7d" -> {
                    fromDate = today.minusDays(7);
                    toDate = today;
                }
                case "30d" -> {
                    fromDate = today.minusDays(30);
                    toDate = today;
                }
                case "90d" -> {
                    fromDate = today.minusDays(90);
                    toDate = today;
                }
            }
        }
    }

    public LocalDate getToDateEndExclusive() {
        return toDate == null ? null : toDate.plusDays(1);
    }
}
