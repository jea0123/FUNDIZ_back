package com.example.funding.dto.request.creator;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class SearchCreatorProjectDto {
    private String projectStatus;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String rangeType;

    public void applyRangeType() {
        if (rangeType != null) {
            LocalDate today = LocalDate.now();

            switch (rangeType) {
                case "7d" -> {
                    fromDate = today.minusDays(7).atStartOfDay();
                    toDate = today.atStartOfDay();
                }
                case "30d" -> {
                    fromDate = today.minusDays(30).atStartOfDay();
                    toDate = today.atStartOfDay();
                }
                case "90d" -> {
                    fromDate = today.minusDays(90).atStartOfDay();
                    toDate = today.atStartOfDay();
                }
            }
        }
    }

    public LocalDate getToDateEndExclusive() {
        return toDate == null ? null : LocalDate.from(toDate.plusDays(1));
    }
}
