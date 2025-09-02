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
    private long settlementId;
    private long projectId;
    private long totalAmount;
    private long fee;
    private long settlementAmount;
    private Date settlementDate;
}
