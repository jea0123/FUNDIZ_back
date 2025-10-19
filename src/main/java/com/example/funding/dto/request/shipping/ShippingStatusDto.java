package com.example.funding.dto.request.shipping;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShippingStatusDto {
    private Long backingId;
    private String shippingStatus;
    private String trackingNum;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private String originalTrackingNum;
}
