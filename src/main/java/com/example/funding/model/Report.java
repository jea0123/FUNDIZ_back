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
    private long reportId;
    private long userId;
    private long target;
    private String reason;
    private Date reportDate;
    private String reportStatus;
    private String reportType;
}
