package com.example.funding.dto.response.backing;

import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.dto.response.payment.BackingPagePaymentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingResponseDto {
    // 유저 테이블
    private Long userId;
    private String nickname;
    private String email;

    // 배송지 테이블
    // 배송지 목록이 0개일 수 있음
    private List<AddressResponseDto> addressList;

    //창작자 테이블
    private Long creatorId;
    private String creatorName;
    private String profileImg;

    //프로젝트 테이블
    private Long projectId;
    private String title;
    private String thumbnail;

    private List<BackingRewardDto> rewardsList;

    private List<BackingPagePaymentDto> backingPagePaymentList;
}
