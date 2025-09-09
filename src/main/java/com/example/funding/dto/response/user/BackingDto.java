package com.example.funding.dto.response.user;

import com.example.funding.model.Backing;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackingDto {
    BackingRewardDto backingReward;
    private Long price;
    private Long quantity;
    Backing backing;

}
