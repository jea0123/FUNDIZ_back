package com.example.funding.dto.request.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementWaitingRequestDto {
    private Long projectId;
    private Long creatorId;
    private Long totalAmount;
    private Long fee;
    private Long settlementAmount;
    private Long refundAmount;
}
