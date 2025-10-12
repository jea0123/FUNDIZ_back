package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.auth.CheckEmailRequestDto;
import com.example.funding.dto.request.auth.CheckNicknameRequestDto;
import com.example.funding.dto.request.auth.SignInRequestDto;
import com.example.funding.dto.request.auth.SignUpRequestDto;
import com.example.funding.exception.DuplicatedEmailException;
import com.example.funding.exception.DuplicatedNicknameException;
import com.example.funding.exception.InvalidCredentialsException;
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

    /**
     * <p>회원가입</p>
     *
     * @param dto 가입 정보
     * @return 이메일
     * @throws DuplicatedEmailException 이미 존재하는 이메일인 경우
     * @author by: 장민규
     * @since 2025-08-26
     */
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto<String>> signUp(@RequestBody SignUpRequestDto dto) {
        return authService.signUp(dto);
    }

    /**
     * <p>로그인</p>
     *
     * @param dto 로그인 정보
     * @return JWT 토큰
     * @throws InvalidCredentialsException 이메일 또는 비밀번호가 올바르지 않은 경우
     * @author by: 장민규
     * @since 2025-08-26
     */
    @PostMapping("/signIn")
    public ResponseEntity<ResponseDto<String>> signIn(@RequestBody SignInRequestDto dto) {
        return authService.signIn(dto);
    }

    /**
     * <p>이메일 중복 확인</p>
     *
     * @param dto 이메일 정보
     * @return 이메일
     * @throws DuplicatedEmailException 이미 존재하는 이메일인 경우
     * @author by: 장민규
     * @since 2025-08-27
     */
    @PostMapping("/checkEmail")
    public ResponseEntity<ResponseDto<String>> checkEmail(@RequestBody CheckEmailRequestDto dto) {
        return authService.checkEmail(dto);
    }

    /**
     * <p>닉네임 중복 확인</p>
     *
     * @param dto 닉네임 정보
     * @return 닉네임
     * @throws DuplicatedNicknameException 이미 존재하는 닉네임인 경우
     * @author by: 장민규
     * @since 2025-08-27
     */
    @PostMapping("/checkNickname")
    public ResponseEntity<ResponseDto<String>> checkNickname(@RequestBody CheckNicknameRequestDto dto) {
        return authService.checkNickname(dto);
    }
}
