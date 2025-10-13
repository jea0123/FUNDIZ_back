package com.example.funding.dto.request.backing;

import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.response.backing.BackingRewardDto;
import com.example.funding.dto.response.user.MyPageBackingProjectDto;
import com.example.funding.dto.response.user.MyPageBackingRewardDto;
import com.example.funding.model.Backing;
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
    private Long projectId;
    private String thumbnail;
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private LocalDate endDate;
    private String projectStatus;

    private Long rewardId;
    private String rewardName;
    private LocalDate deliveryDate;

    private Long price;
    private Long quantity;

    private Long backingId;
    private Long userId;
    private Long amount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    private String backingStatus;

}
