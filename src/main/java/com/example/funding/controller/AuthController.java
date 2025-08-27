package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.user.SignInRequestDto;
import com.example.funding.dto.request.user.SignUpRequestDto;
import com.example.funding.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
