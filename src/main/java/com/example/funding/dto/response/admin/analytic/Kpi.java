package com.example.funding.dto.response.admin.analytic;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kpi {
    private Long totalBackingAmount;
    private Long fee;
    private Double successRate;
    private Long backingAmountAvg;
}
