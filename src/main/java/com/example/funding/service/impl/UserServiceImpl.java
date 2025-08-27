package com.example.funding.service.impl;

import com.example.funding.dao.UserDao;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.user.SignInRequestDto;
import com.example.funding.dto.request.user.SignUpRequestDto;
import com.example.funding.model.User;
import com.example.funding.provider.JwtProvider;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public ResponseEntity<ResponseDto<String>> signUp(SignUpRequestDto dto) {
        User existingUser = userDao.findByEmail(dto.getEmail());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body(ResponseDto.fail(400,"이미 사용중인 이메일입니다."));
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        User user = User.builder().email(dto.getEmail()).password(dto.getPassword()).nickname(dto.getNickname())
                .build();
        userDao.signUp(user);
        return ResponseEntity.ok(ResponseDto.success(200,"회원가입 성공", dto.getEmail()));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> signIn(SignInRequestDto dto) {
        User user = userDao.findByEmail(dto.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body(ResponseDto.fail(400,"존재하지 않는 이메일입니다."));
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        if (passwordEncoder.matches(encodedPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body(ResponseDto.fail(400,"비밀번호가 일치하지 않습니다."));
        }
        String token = jwtProvider.createAccessToken(user.getUserId(), user.getEmail(), user.getRole().toString());
        return ResponseEntity.ok(ResponseDto.success(200,"로그인 성공", token));
    }
}
