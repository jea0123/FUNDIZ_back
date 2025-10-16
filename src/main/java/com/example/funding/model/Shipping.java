package com.example.funding.model;

import com.example.funding.enums.BackingStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Shipping {
    private Long shippingId;
    private Long backingId;
    private Long addrId;
    private BackingStatus shippingStatus;
    private String trackingNum;
    private LocalDate shippedAt;
    private LocalDate deliveredAt;
}
