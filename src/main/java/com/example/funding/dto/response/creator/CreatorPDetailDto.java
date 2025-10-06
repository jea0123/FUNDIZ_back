package com.example.funding.dto.response.creator;

import com.example.funding.model.Project;
import com.example.funding.model.Reward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorPDetailDto {
    //프로젝트 정보
    private Long projectId;
    private Long creatorId;
    private Long subctgrId;
    private String title;
    private Integer goalAmount;
    private Integer currAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String thumbnail;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String projectStatus;
    private Integer backerCnt;
    private Integer likeCnt;
    private Integer viewCnt;
    private Character isVerified;
    private String rejectedReason;
    private LocalDate requestedAt;

    //리워드 정보
    private Long rewardId;
    private String rewardName;
    private Long price;
    private String rewardContent;
    private LocalDate deliveryDate;
    private Integer rewardCnt;
    private LocalDate createdAtR;
    private Character isPosting;
    private Integer remain;

    //창작자명
    private String creatorName;
}
