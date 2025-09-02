package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Report {
    private Long reportId;
    private Long userId;
    private Long target;
    private String reason;
    private Date reportDate;
    private String reportStatus;
    private String reportType;
}
