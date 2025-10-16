package com.example.funding.dto.response.user;

import com.example.funding.model.Backing;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackingDto {
    private List<MyPageBackingRewardDto> backingReward;
    private Long price;
    private Long quantity;
    private List<Backing> backing;

}
