package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.admin.RegisterAdminRequestDto;
import com.example.funding.dto.request.auth.CheckEmailRequestDto;
import com.example.funding.dto.request.auth.CheckNicknameRequestDto;
import com.example.funding.dto.request.auth.SignInRequestDto;
import com.example.funding.dto.request.auth.SignUpRequestDto;
import com.example.funding.exception.badrequest.InvalidCredentialsException;
import com.example.funding.exception.conflict.DuplicatedAdminIdException;
import com.example.funding.exception.conflict.DuplicatedEmailException;
import com.example.funding.exception.conflict.DuplicatedNicknameException;
import com.example.funding.exception.forbidden.InvalidAdminCredentialsException;
import com.example.funding.exception.notfound.AdminNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
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

    /**
     * <p>회원 탈퇴</p>
     * <p>- 회원 정보 삭제</p>
     *
     * @param userId 인증된 사용자의 ID
     * @return 탈퇴 완료 메시지
     * @author by: 장민규
     * @since 2025-09-10
     */
    ResponseEntity<ResponseDto<String>> withdrawUser(@NotNull(message = "사용자 ID는 필수입니다. 현재: ${validatedValue}")
                                                     @Positive(message = "사용자 ID는 양수여야 합니다. 현재: ${validatedValue}")
                                                     Long userId);

    /**
     * <p>관리자 회원가입</p>
     * <p>- 이미 존재하는 관리자 아이디인지 확인, 비밀번호 암호화, 관리자 정보 저장</p>
     *
     * @param dto 가입 정보
     * @return 관리자 아이디
     * @throws DuplicatedAdminIdException 이미 존재하는 관리자 아이디인 경우
     * @author by: 장민규
     * @since 2025-10-14
     */
    ResponseEntity<ResponseDto<String>> registerAdmin(RegisterAdminRequestDto dto);

    /**
     * <p>관리자 로그인</p>
     * <p>- 관리자 아이디와 비밀번호 확인, JWT 토큰 생성 및 반환</p>
     *
     * @param dto 로그인 정보
     * @return JWT 토큰
     * @throws AdminNotFoundException 관리자 아이디가 올바르지 않은 경우
     * @throws InvalidAdminCredentialsException 관리자 아이디 또는 비밀번호가 올바르지 않은 경우
     * @author by: 장민규
     * @since 2025-10-14
     */
    ResponseEntity<ResponseDto<String>> loginAdmin(RegisterAdminRequestDto dto);
}
