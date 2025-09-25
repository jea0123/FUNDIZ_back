package com.example.funding.dto.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordDto {
    private String password;

    private String newPassword;
}
