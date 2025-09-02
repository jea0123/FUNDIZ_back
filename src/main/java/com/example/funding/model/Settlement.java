package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Settlement {
    private Long settlementId;
    private Long projectId;
    private Long totalAmount;
    private Long fee;
    private Long settlementAmount;
    private Date settlementDate;
}
