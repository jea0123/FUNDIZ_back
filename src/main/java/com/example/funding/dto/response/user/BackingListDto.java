package com.example.funding.dto.response.user;

import com.example.funding.enums.BackingStatus;
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
public class BackingListDto {

    private Long projectId;
    private String title;
    private String thumbnail;
    private Date endDate;
    private String projectStatus;

    private String rewardName;
    private Long backingId;
    private String backingStatus;
    private Long amount;
    private Date createAt;
    private int quantity;
}
