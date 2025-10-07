package com.example.funding.dto.request.reward;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RewardCreateRequestDto {
    private String rewardName;
    private Long price;
    private String rewardContent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    private Integer rewardCnt;
    private Character isPosting;
}
