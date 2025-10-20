package com.example.funding.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckNicknameRequestDto {
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
}
