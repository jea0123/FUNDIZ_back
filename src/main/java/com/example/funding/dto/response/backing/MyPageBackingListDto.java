package com.example.funding.dto.response.backing;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageBackingListDto {
    //프로젝트 테이블
    private Long projectId;
    private String title;
    private Long goalAmount;
    private Long currAmount;
    private LocalDateTime endDate;
    private String thumbnail;

    //리워드 테이블
    private List<MyPageBacking_RewardDto> mpBackingList = new ArrayList<>();

    //후원 테이블
    private Long userId;
    private Long backingId;
    private Long amount;
    private LocalDateTime createdAt;
    private String backingStatus;

    //배송 테이블
    private String shippingStatus;

    //창작자 테이블
    private String creatorName;

}
