package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.LoginUserDto;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.User;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body(ResponseDto.fail(404, "사용자를 찾을 수 없습니다."));
        }
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .joinedAt(user.getJoinedAt())
                .followCnt(user.getFollowCnt())
                .isCreator(user.getIsCreator())
                .role(user.getRole().toString())
                .build();
        return ResponseEntity.status(200).body(ResponseDto.success(200, "사용자 정보 조회 성공", loginUserDto));
    }
}
