package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.user.SignInRequestDto;
import com.example.funding.dto.request.user.SignUpRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ResponseDto<String>> signUp(SignUpRequestDto dto);

    ResponseEntity<ResponseDto<String>> signIn(SignInRequestDto dto);
}
