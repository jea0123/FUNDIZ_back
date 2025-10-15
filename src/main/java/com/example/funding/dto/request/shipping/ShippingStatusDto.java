package com.example.funding.dto.request.shipping;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ShippingStatusDto {
    private Long backingId;
    private String shippingStatus;
    @Pattern(regexp = "^[0-9]{10,14}$", message = "운송장 번호는 10~14자리 숫자만 가능합니다")
    private String trackingNum;
    private LocalDate shippedAt;
    private LocalDate deliveredAt;
}
