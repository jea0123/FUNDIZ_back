package com.example.funding.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CardListDto {
    private Long payInfoId;
    private String cardCompany;
    private String method;
    private String cardNum;
}
