package com.example.funding.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AddCardDto {
    private Long payInfoId;
    private Long userId;
    private String cardCompany;
    private String method;
    private String cardNum;
}
