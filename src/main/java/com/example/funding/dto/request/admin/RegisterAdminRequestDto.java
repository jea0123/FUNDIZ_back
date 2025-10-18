package com.example.funding.dto.request.admin;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAdminRequestDto {
    @NotNull(message = "관리자 아이디는 필수입니다.")
    private String adminId;
    @NotNull(message = "관리자 비밀번호는 필수입니다.")
    private String adminPwd;
}
