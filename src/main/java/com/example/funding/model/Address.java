package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Address {
    private Long userId;
    private Long addrName;
    private String recipient;
    private String postalCode;
    private String roadAddr;
    private String detailAddr;
    private String recipientPhone;
    private Character isDefault;
}

