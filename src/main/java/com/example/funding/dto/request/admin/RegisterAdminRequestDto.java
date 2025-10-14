package com.example.funding.dto.request.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAdminRequestDto {
    private String adminId;
    private String adminPwd;
}
