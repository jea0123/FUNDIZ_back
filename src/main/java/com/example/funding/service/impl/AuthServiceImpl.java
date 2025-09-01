package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.auth.CheckEmailRequestDto;
import com.example.funding.dto.request.auth.CheckNicknameRequestDto;
import com.example.funding.dto.request.auth.SignInRequestDto;
import com.example.funding.dto.request.auth.SignUpRequestDto;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.User;
import com.example.funding.provider.JwtProvider;
import com.example.funding.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    /**
     * <p>회원가입</p>
     * <p>- 이미 존재하는 이메일인지 확인, 비밀번호 암호화, 회원 정보 저장</p>
     * @param dto SignUpRequestDto
     * @return : 성공 시 200 OK, 실패 시 409 CONFLICT
     * @since 2025-08-27
     * @author by: 장민규
     */
    @Override
    public ResponseEntity<ResponseDto<String>> signUp(SignUpRequestDto dto) {
        User existingUser = userMapper.findByEmail(dto.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(409,"이미 사용중인 이메일입니다."));
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        User user = User.builder().email(dto.getEmail()).password(dto.getPassword()).nickname(dto.getNickname())
                .build();
        userMapper.signUp(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"회원가입 성공", dto.getEmail()));
    }

    /**
     * <p>로그인</p>
     * <p>- 이메일과 비밀번호 확인, JWT 토큰 생성 및 반환</p>
     * @param dto SignInRequestDto
     * @return : 성공 시 200 OK, 실패 시 400 BAD REQUEST
     * @since 2025-08-27
     * @author by: 장민규
     */
    @Override
    public ResponseEntity<ResponseDto<String>> signIn(SignInRequestDto dto) {
        User user = userMapper.findByEmail(dto.getEmail());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        if (user == null || passwordEncoder.matches(encodedPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDto.fail(400,"이메일 또는 비밀번호가 일치하지 않습니다."));
        }
        String token = jwtProvider.createAccessToken(user.getUserId(), user.getEmail(), user.getRole().toString());
        userMapper.updateLastLogin(user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"로그인 성공", token));
    }

    /**
     * <p>이메일 중복 확인</p>
     * <p>- 이미 존재하는 이메일인지 확인</p>
     * @param dto CheckEmailRequestDto
     * @return : 사용 가능 시 200 OK, 이미 존재 시 409 CONFLICT
     * @since 2025-08-27
     * @author by: 장민규
     */
    @Override
    public ResponseEntity<ResponseDto<String>> checkEmail(CheckEmailRequestDto dto) {
        User existingUser = userMapper.findByEmail(dto.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(409,"이미 사용중인 이메일입니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"사용 가능한 이메일입니다.", dto.getEmail()));
    }

    /**
     * <p>닉네임 중복 확인</p>
     * <p>- 이미 존재하는 닉네임인지 확인</p>
     * @param dto CheckNicknameRequestDto
     * @return : 사용 가능 시 200 OK, 이미 존재 시 409 CONFLICT
     * @since 2025-08-27
     * @author by: 장민규
     */
    @Override
    public ResponseEntity<ResponseDto<String>> checkNickname(CheckNicknameRequestDto dto) {
        User existingUser = userMapper.findByNickname(dto.getNickname());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDto.fail(409,"이미 사용중인 닉네임입니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"사용 가능한 닉네임입니다.", dto.getNickname()));
    }
}
