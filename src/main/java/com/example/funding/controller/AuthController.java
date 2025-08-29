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

    /**
     * <p>회원가입</p>
     * <p>- 이미 존재하는 이메일인지 확인, 비밀번호 암호화, 회원 정보 저장</p>
     * @param dto
     * @return : 성공 시 200 OK, 실패 시 409 CONFLICT
     * @since 2025-08-26
     * @author by: 장민규
     */
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto<String>> signUp(@RequestBody SignUpRequestDto dto) {
        return authService.signUp(dto);
    }

    /**
     * <p>로그인</p>
     * <p>- 이메일과 비밀번호 확인, JWT 토큰 생성 및 반환</p>
     * @param dto
     * @return : 성공 시 200 OK, 실패 시 400 BAD REQUEST
     * @since 2025-08-26
     * @author by: 장민규
     */
    @PostMapping("/signIn")
    public ResponseEntity<ResponseDto<String>> signIn(@RequestBody SignInRequestDto dto) {
        return authService.signIn(dto);
    }

    /**
     * <p>이메일 중복 확인</p>
     * @param dto
     * @return : 성공 시 200 OK, 실패 시 409 CONFLICT
     * @since 2025-08-27
     * @author by: 장민규
     */
    @PostMapping("/checkEmail")
    public ResponseEntity<ResponseDto<String>> checkEmail(@RequestBody CheckEmailRequestDto dto) {
        return authService.checkEmail(dto);
    }

    /**
     * <p>닉네임 중복 확인</p>
     * @param dto
     * @return : 성공 시 200 OK, 실패 시 409 CONFLICT
     * @since 2025-08-27
     * @author by: 장민규
     */
    @PostMapping("/checkNickname")
    public ResponseEntity<ResponseDto<String>> checkNickname(@RequestBody CheckNicknameRequestDto dto) {
        return authService.checkNickname(dto);
    }
}
