package com.example.funding.model;

import com.example.funding.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private char isSuspended;
    private Date joinedAt;
    private Date lastLoginAt;
    private char isDeleted;
    private Date deletedAt;
    private int followCnt;
    private String reason;
    private Date suspendedAt;
    private Date releasedAt;
    private char isCreator;
    private Role role;
}
