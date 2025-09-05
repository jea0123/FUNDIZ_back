package com.example.funding.dto.response.user;

import com.example.funding.model.Backing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingDetailDto {
    BackingRewardDto backingReward;
    private long price;
    private long quantity;
    Backing backing;

}
