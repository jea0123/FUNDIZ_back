package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.LoginUserDto;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.User;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    /**
     * <p>로그인 사용자 정보 조회</p>
     * @param userId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-08-28
     * @author by: 장민규
     */
    @Override
    public ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "사용자를 찾을 수 없습니다."));
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
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용자 정보 조회 성공", loginUserDto));
    }
}
