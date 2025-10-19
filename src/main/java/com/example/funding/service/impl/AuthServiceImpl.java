package com.example.funding.service.impl;

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
import com.example.funding.mapper.AdminMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.Admin;
import com.example.funding.model.User;
import com.example.funding.provider.JwtProvider;
import com.example.funding.service.AuthService;
import com.example.funding.validator.Loaders;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class AuthServiceImpl implements AuthService {
    private final Loaders loaders;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AdminMapper adminMapper;

    /**
     * 회원가입
     */
    @Override
    public ResponseEntity<ResponseDto<String>> signUp(SignUpRequestDto dto) {
        User existingUser = userMapper.findByEmail(dto.getEmail());
        if (existingUser != null) throw new DuplicatedEmailException();

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        User user = User.builder().email(dto.getEmail()).password(dto.getPassword()).nickname(dto.getNickname())
                .build();
        userMapper.signUp(user);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "회원가입 성공", dto.getEmail()));
    }

    /**
     * 로그인
     */
    @Override
    public ResponseEntity<ResponseDto<String>> signIn(SignInRequestDto dto) {
        User user = userMapper.findByEmail(dto.getEmail());
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

      //임시로그인 암호화
        if (user == null || passwordEncoder.matches(user.getPassword(), encodedPassword))
            throw new InvalidCredentialsException();

//        //임시 로그인 암호화 해제
//        if (user == null || !dto.getPassword().equals(user.getPassword())) {
//            throw new InvalidCredentialsException();
//        }

        String token = jwtProvider.createAccessToken(user.getUserId(), user.getEmail(), user.getRole().toString());
        userMapper.updateLastLogin(user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "로그인 성공", token));
    }

    /**
     * 이메일 중복 확인
     */
    @Override
    public ResponseEntity<ResponseDto<String>> checkEmail(CheckEmailRequestDto dto) {
        User existingUser = userMapper.findByEmail(dto.getEmail());
        if (existingUser != null) throw new DuplicatedEmailException();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용 가능한 이메일입니다.", dto.getEmail()));
    }

    /**
     * 닉네임 중복 확인
     */
    @Override
    public ResponseEntity<ResponseDto<String>> checkNickname(CheckNicknameRequestDto dto) {
        User existingUser = userMapper.findByNickname(dto.getNickname());
        if (existingUser != null) throw new DuplicatedNicknameException();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용 가능한 닉네임입니다.", dto.getNickname()));
    }

    /**
     * 회원 탈퇴
     */
    @Override
    public ResponseEntity<ResponseDto<String>> withdrawUser(@NotBlank Long userId) {
        loaders.user(userId);
        userMapper.withdrawUser(userId);
        return ResponseEntity.ok(ResponseDto.success(200, "회원 탈퇴 성공", null));
    }

    /**
     * 관리자 등록
     */
    @Override
    public ResponseEntity<ResponseDto<String>> registerAdmin(RegisterAdminRequestDto dto) {
        if (adminMapper.getAdminByAdminId(dto.getAdminId()) != null) throw new DuplicatedAdminIdException();

        String encodedPwd = passwordEncoder.encode(dto.getAdminPwd());
        dto.setAdminPwd(encodedPwd);
        adminMapper.registerAdmin(dto);
        return ResponseEntity.ok(ResponseDto.success(200, "관리자 등록 성공", null));
    }

    /**
     * 관리자 로그인
     */
    @Override
    public ResponseEntity<ResponseDto<String>> loginAdmin(RegisterAdminRequestDto dto) {
        Admin admin = loaders.admin(dto.getAdminId());

        if (passwordEncoder.matches(dto.getAdminPwd(), admin.getAdminPwd())) {
            String token = jwtProvider.createAdminAccessToken(admin.getAdminId(), admin.getRole().toString());
            return ResponseEntity.ok(ResponseDto.success(200, "로그인 성공", token));
        } else {
            throw new InvalidAdminCredentialsException();
        }
    }
}
