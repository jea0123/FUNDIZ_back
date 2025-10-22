package com.example.funding.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Payment {
    private Long paymentId;
    private Long backingId;
    private String orderId;
    private String method;
    private String status;
    private Long amount;
    private String cardCompany;
    private LocalDateTime createdAt;
}
