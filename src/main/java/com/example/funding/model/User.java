package com.example.funding.model;

import com.example.funding.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
    private Character isSuspended;
    private LocalDateTime joinedAt;
    private LocalDateTime lastLoginAt;
    private Integer followCnt;
    private String reason;
    private LocalDateTime suspendedAt;
    private LocalDateTime releasedAt;
    private Character isCreator;
    private Role role;
}
