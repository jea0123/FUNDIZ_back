package com.example.funding.model;

import com.example.funding.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate joinedAt;
    private LocalDate lastLoginAt;
    private Integer followCnt;
    private String reason;
    private LocalDate suspendedAt;
    private LocalDate releasedAt;
    private Character isCreator;
    private Role role;
}
