package com.example.funding.dto.request.shipping;


import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShippingStatusDto {
    private Long backingId;
    private String shippingStatus;
    @Pattern(regexp = "^[0-9]{10,14}$", message = "운송장 번호는 10~14자리 숫자만 가능합니다")
    private String trackingNum;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
}
