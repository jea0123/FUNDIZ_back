package com.example.funding.dto.request.backing;

import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.response.Backing.BackingRewardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingRequestUpdateDto {
    private Long backingId;
    private Long userId;

    private List<BackingRewardDto> backingRewards;

    private Long rewardId;

    private AddrAddRequestDto newAddress;

}
