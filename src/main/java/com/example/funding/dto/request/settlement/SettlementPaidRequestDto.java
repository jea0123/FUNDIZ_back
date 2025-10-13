package com.example.funding.dto.request.settlement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettlementPaidRequestDto {
    private Long settlementId;
    private Long projectId;
    private Long creatorId;
    private String settlementStatus;
}
