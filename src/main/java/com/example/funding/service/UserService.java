package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.LoginUserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId);
}
