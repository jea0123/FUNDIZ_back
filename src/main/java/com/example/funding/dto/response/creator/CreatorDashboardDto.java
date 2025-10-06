package com.example.funding.dto.response.creator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorDashboardDto {
    private Long creatorId;
    //총 프로젝트 개수
    private Long totalProject;
    //모든 프로젝트 후원 금액 합계
    private Long totalAmount;
    // 모든 프로젝트의 후원받은수의 합계
    private Long totalBackers;
    //프로젝트 승인대기 개수
    private Long pendingApproval;

    //내프로젝트 성공률 (파이차트)
    private Long successRate;
    //월별수익
    private Long monthAmount;
    //내가한 프로젝트 랭킹
    private Long projectRank;
}
