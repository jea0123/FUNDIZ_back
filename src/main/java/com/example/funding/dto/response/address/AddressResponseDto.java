package com.example.funding.dto.response.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddressResponseDto {
    // 배송지 테이블
    private Long addrId;
    private Long userId;
    private String addrName;
    private String recipient;
    private String postalCode;
    private String roadAddr;
    private String detailAddr;
    private String recipientPhone;
    private String isDefault;

}
