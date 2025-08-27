package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.auth.CheckEmailRequestDto;
import com.example.funding.dto.request.auth.CheckNicknameRequestDto;
import com.example.funding.dto.request.auth.SignInRequestDto;
import com.example.funding.dto.request.auth.SignUpRequestDto;
import com.example.funding.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto<String>> signUp(@RequestBody SignUpRequestDto dto) {
        return authService.signUp(dto);
    }

    @PostMapping("/signIn")
    public ResponseEntity<ResponseDto<String>> signIn(@RequestBody SignInRequestDto dto) {
        return authService.signIn(dto);
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<ResponseDto<String>> checkEmail(@RequestBody CheckEmailRequestDto dto) {
        return authService.checkEmail(dto);
    }

    @PostMapping("/checkNickname")
    public ResponseEntity<ResponseDto<String>> checkNickname(@RequestBody CheckNicknameRequestDto dto) {
        return authService.checkNickname(dto);
    }
}
