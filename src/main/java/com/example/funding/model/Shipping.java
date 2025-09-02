package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Shipping {
    private Long shippingId;
    private Long backingId;
    private Long addrId;
    private String shippingStatus;
    private String trackingNum;
    private Date shippedAt;
    private Date deliveredAt;
}
