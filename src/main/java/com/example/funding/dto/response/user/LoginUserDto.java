package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginUserDto {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;
    private LocalDate joinedAt;
    private Integer followCnt;
    private Character isCreator;
    private Long creatorId;
    private String role;
}
