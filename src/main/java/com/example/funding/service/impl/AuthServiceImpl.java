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
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public ResponseEntity<ResponseDto<String>> signUp(SignUpRequestDto dto) {
        User existingUser = userMapper.findByEmail(dto.getEmail());
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        User user = User.builder().email(dto.getEmail()).password(dto.getPassword()).nickname(dto.getNickname())
                .build();
        userMapper.signUp(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "회원가입 성공", dto.getEmail()));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> signIn(SignInRequestDto dto) {
        User user = userMapper.findByEmail(dto.getEmail());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        if (user == null || passwordEncoder.matches(encodedPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        String token = jwtProvider.createAccessToken(user.getUserId(), user.getEmail(), user.getRole().toString());
        userMapper.updateLastLogin(user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "로그인 성공", token));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> checkEmail(CheckEmailRequestDto dto) {
        User existingUser = userMapper.findByEmail(dto.getEmail());
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용 가능한 이메일입니다.", dto.getEmail()));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> checkNickname(CheckNicknameRequestDto dto) {
        User existingUser = userMapper.findByNickname(dto.getNickname());
        if (existingUser != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용 가능한 닉네임입니다.", dto.getNickname()));
    }
}
