package com.example.funding.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementPaidRequestDto {
    private Long settlementId;
    private Long projectId;
    private Long creatorId;
    private Long totalAmount;
    private Long fee;
    private Long settlementAmount;
    private Long refundAmount;
}
