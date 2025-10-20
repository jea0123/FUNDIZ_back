package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentInfo {
    private Long userPaymentId;
    private Long userId;
    private String cardCompany;
    private String method;
}
