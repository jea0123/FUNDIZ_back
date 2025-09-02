package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MyPageUserDto {
    // 유저 테이블에서 가져올 정보
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;

}
