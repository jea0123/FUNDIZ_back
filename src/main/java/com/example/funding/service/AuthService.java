package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.auth.CheckEmailRequestDto;
import com.example.funding.dto.request.auth.CheckNicknameRequestDto;
import com.example.funding.dto.request.auth.SignInRequestDto;
import com.example.funding.dto.request.auth.SignUpRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<ResponseDto<String>> signUp(SignUpRequestDto dto);

    ResponseEntity<ResponseDto<String>> signIn(SignInRequestDto dto);

    ResponseEntity<ResponseDto<String>> checkEmail(CheckEmailRequestDto dto);

    ResponseEntity<ResponseDto<String>> checkNickname(CheckNicknameRequestDto dto);
}
