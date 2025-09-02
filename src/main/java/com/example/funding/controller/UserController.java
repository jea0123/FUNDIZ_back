package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.LoginUserDto;
import com.example.funding.dto.response.user.MyPageUserDto;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * <p>로그인 사용자 정보 조회</p>
     * @param userId 인증된 사용자의 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 장민규
     */
    @GetMapping("/loginUser")
    public ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(@AuthenticationPrincipal(expression = "userId") Long userId) {
        return userService.getLoginUser(userId);
    }

    //user 마다 mypage를 => /me/{id} 로받아야될지 생각좀해봐야될듯?
    /*@GetMapping("/me")
    public ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId) {

    }*/
}
