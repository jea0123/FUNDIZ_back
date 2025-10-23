package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Settlement {
    private Long settlementId;
    private Long projectId;
    private Long creatorId;
    private Long totalAmount;
    private Long fee;
    private Long settlementAmount;
    private LocalDateTime settlementDate;
    private String settlementStatus;
    private Long refundAmount;
}
