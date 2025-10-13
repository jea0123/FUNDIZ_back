package com.example.funding.dto.response.settlement;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SettlementItem {
    private Long settlementId;
    private Long projectId;
    private String projectTitle;
    private Long creatorId;
    private String creatorName;
    private Long totalAmount;
    private Long fee;
    private Long settlementAmount;
    private Long refundAmount;
    private LocalDateTime settlementDate;
    private String settlementStatus;
}
