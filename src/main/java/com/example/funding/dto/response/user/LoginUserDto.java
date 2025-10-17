package com.example.funding.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginUserDto {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;
    private LocalDateTime joinedAt;
    private Integer followCnt;
    private Character isCreator;
    private Long creatorId;
    private String role;
}
