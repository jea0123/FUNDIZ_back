package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.auth.CheckEmailRequestDto;
import com.example.funding.dto.request.auth.CheckNicknameRequestDto;
import com.example.funding.dto.request.auth.SignInRequestDto;
import com.example.funding.dto.request.auth.SignUpRequestDto;
import com.example.funding.exception.DuplicatedEmailException;
import com.example.funding.exception.DuplicatedNicknameException;
import com.example.funding.exception.InvalidCredentialsException;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    /**
     * <p>회원가입</p>
     * <p>- 이미 존재하는 이메일인지 확인, 비밀번호 암호화, 회원 정보 저장</p>
     *
     * @param dto 가입 정보
     * @return 이메일
     * @throws DuplicatedEmailException 이미 존재하는 이메일인 경우
     * @author by: 장민규
     * @since 2025-08-27
     */
    ResponseEntity<ResponseDto<String>> signUp(SignUpRequestDto dto);

    /**
     * <p>로그인</p>
     * <p>- 이메일과 비밀번호 확인, JWT 토큰 생성 및 반환</p>
     *
     * @param dto 로그인 정보
     * @return JWT 토큰
     * @throws InvalidCredentialsException 이메일 또는 비밀번호가 올바르지 않은 경우
     * @author by: 장민규
     * @since 2025-08-27
     */
    ResponseEntity<ResponseDto<String>> signIn(SignInRequestDto dto);

    /**
     * <p>이메일 중복 확인</p>
     * <p>- 이미 존재하는 이메일인지 확인</p>
     *
     * @param dto 이메일 정보
     * @return 이메일
     * @throws DuplicatedEmailException 이미 존재하는 이메일인 경우
     * @author by: 장민규
     * @since 2025-08-27
     */
    ResponseEntity<ResponseDto<String>> checkEmail(CheckEmailRequestDto dto);

    /**
     * <p>닉네임 중복 확인</p>
     * <p>- 이미 존재하는 닉네임인지 확인</p>
     *
     * @param dto 닉네임 정보
     * @return 닉네임
     * @throws DuplicatedNicknameException 이미 존재하는 닉네임인 경우
     * @author by: 장민규
     * @since 2025-08-27
     */
    ResponseEntity<ResponseDto<String>> checkNickname(CheckNicknameRequestDto dto);

    ResponseEntity<ResponseDto<String>> withdrawUser(Long userId);
}
