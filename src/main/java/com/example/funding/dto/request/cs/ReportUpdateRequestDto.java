package com.example.funding.dto.request.cs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReportUpdateRequestDto {
    private Long reportId;
    private String reason;
    private String reportStatus;
}
