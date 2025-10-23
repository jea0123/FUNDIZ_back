package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Report {
    private Long reportId;
    private Long userId;
    private String reportType;
    private Long target;
    private String reason;
    private LocalDateTime reportDate;
    private String reportStatus;
}
