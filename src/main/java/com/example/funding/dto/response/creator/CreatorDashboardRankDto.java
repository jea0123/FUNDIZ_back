package com.example.funding.dto.response.creator;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorDashboardRankDto {
    private Long projectId;
    //private Long creatorId;
    private String title;

    //내 프로젝트 랭킹(후원자 수)
    private Long backerCnt;
    //내 프로젝트 랭킹(좋아요 수)
    private Long likeCnt;
    //내 프로젝트 랭킹 (조회수)
    private Long viewCnt;

}
