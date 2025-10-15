package com.example.funding.dto.request.backing;

import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.request.reward.RewardBackingRequestDto;
import com.example.funding.dto.response.backing.BackingRewardDto;
import com.example.funding.dto.response.user.MyPageBackingProjectDto;
import com.example.funding.dto.response.user.MyPageBackingRewardDto;
import com.example.funding.enums.BackingStatus;
import com.example.funding.model.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BackingRequestDto {
    private Long backingId;
    private Backing backing;
    private Shipping shipping;
    private Payment payment;
    private Address address;
    private List<RewardBackingRequestDto> rewards;
}
