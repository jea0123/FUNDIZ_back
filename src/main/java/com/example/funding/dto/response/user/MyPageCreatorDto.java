package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class MyPageCreatorDto {
    //창작자 테이블에서 가져올 정보
    private String creatorId;
    private String creatorName;
    private String creatorProfileImg;
}
