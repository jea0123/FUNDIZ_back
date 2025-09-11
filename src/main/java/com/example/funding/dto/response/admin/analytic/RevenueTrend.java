package com.example.funding.dto.response.admin.analytic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevenueTrend {
    private String month;
    private Integer projectCnt;
    private Long revenue;
}
