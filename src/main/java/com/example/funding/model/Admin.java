package com.example.funding.model;

import com.example.funding.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Admin {
    private Long adId;
    private String adminId;
    private String adminPwd;
    private Long author;
    private Role role;
}
