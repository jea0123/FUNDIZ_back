package com.example.funding.dto.request.backing;

import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.response.backing.BackingRewardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingRequestDto {
    //유저 테이블
    private Long userId;

    //리워드 목록
    private List<BackingRewardDto> backingRewardList;

    //기존배송지
    private Long addrId;
    
    //새 배송지 지정
    private AddrAddRequestDto newAddress;
}
