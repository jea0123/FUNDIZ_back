package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginUserDto {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;
    private Date joinedAt;
    private int followCnt;
    private char isCreator;
    private String role;


}
