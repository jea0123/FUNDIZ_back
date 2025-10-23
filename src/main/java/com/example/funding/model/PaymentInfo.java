package com.example.funding.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PaymentInfo {
    private Long payInfoId;
    private Long userId;
    private String cardCompany;
    private String method;
    private String cardNum;
}
