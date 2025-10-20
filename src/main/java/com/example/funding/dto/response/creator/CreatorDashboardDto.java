package com.example.funding.dto.response.creator;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatorDashboardDto {
    private Long creatorId;

    private Integer projectTotal;
    private Long totalAmount;
    private Long totalBackingCnt;
    private Long totalVerifyingCnt;

    //내프로젝트 성공률 (파이차트 계산용)
    private Double totalProjectCnt; // 전체 프로젝트 개수
    private Double projectFailedCnt; // 실패한 프로젝트 개수
    private Double projectFailedPercentage; // 실패한 프로젝트 퍼센트
    private Double projectSuccessPercentage; // 성공한 프로젝트 퍼센트

    private List<CreatorDashboardRankDto> top3BackerCnt;
    private List<CreatorDashboardRankDto> top3LikeCnt;
    private List<CreatorDashboardRankDto> top3ViewCnt;

    private List<DailyCountDto> dailyStatus;
    private List<MonthCountDto> monthStatus;
}
