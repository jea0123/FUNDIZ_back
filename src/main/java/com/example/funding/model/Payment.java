package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Payment {
    private Long paymentId;
    private Long backingId;
    private String orderId;
    private String method;
    private String status;
    private Long amount;
    private String cardCompany;
    private LocalDate createdAt;
}
