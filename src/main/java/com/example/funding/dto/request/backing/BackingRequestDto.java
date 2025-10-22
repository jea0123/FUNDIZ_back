package com.example.funding.dto.request.backing;

import com.example.funding.dto.request.reward.RewardBackingRequestDto;
import com.example.funding.model.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BackingRequestDto {
    private Long backingId;
    private Backing backing;
    private BackingDetail backingDetail;
    private PaymentInfo paymentInfo;
    private Payment payment;
    private Address address;
    private Shipping shipping;
    private List<RewardBackingRequestDto> rewards;
    private Long amount;
    private Long backerCnt;
}
