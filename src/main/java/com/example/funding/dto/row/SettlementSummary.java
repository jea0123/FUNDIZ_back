package com.example.funding.dto.row;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementSummary {
    private Long waitingAmount;
    private Long completedAmount;
    private Long settledCount;
    private String bank;
    private String account;
}
