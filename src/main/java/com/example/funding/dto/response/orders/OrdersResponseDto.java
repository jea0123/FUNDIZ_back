package com.example.funding.dto.response.orders;

import com.example.funding.dto.response.reward.OrderRewardDto;
import com.example.funding.enums.PaymentCardCompany;
import com.example.funding.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrdersResponseDto {
    // 유저 테이블
    private Long userId;
    private String nickname;
    private String email;

    // 배송지 테이블
    private Long addrId;
    private String addrName;
    private String recipient;
    private String postalCode;
    private String roadAddr;
    private String detailAddr;
    private String recipientPhone;

    //창작자 테이블
    private Long creatorId;
    private String creatorName;
    private String profileImg;

    //프로젝트 테이블
    private Long projectId;
    private String title;
    private String thumbnail;

    private List<OrderRewardDto> rewards;
}
