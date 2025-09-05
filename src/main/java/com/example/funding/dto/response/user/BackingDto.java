package com.example.funding.dto.response.user;

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
public class BackingDto {
    //프로젝트 테이블
    private Long projectId;
    private String title;
    private String thumbnail;
    private Date endDate;
    private String projectStatus;

    //리워드 테이블
    private String rewardName;

    //후원 테이블
    private Long backingId;
    private String backingStatus;

    //후원 상세
    private Long price;
    private Long quantity;


}
