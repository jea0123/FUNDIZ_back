package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.user.*;
import com.example.funding.service.ProjectService;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;

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

    /**
     * <p>최근 본 프로젝트 목록 조회</p>
     * @param userId 인증된 사용자의 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-09-05
     * @author by: 장민규
     */
    @GetMapping("/recentViewProjects/{userId}")
    public ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(@PathVariable Long userId) {
        return userService.getRecentViewProjects(userId);
    }

    /**
     * <p>마이페이지 구현</p>
     * @param userId 에 따른 MyPage 프로필 조회
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-09-02
     * @author by: 이윤기
     */

    @GetMapping("/me/userPage/{userId}")
    public ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(@PathVariable Long userId) {
        return userService.getMyPageUser(userId);
    }

    @PostMapping("me/userPage/{userId}")
    public ResponseEntity<ResponseDto<String>> updateMydata (@PathVariable Long userId, @RequestBody MyPageUserDto myPageUserDto) {
        return userService.getMyData(userId);
    }

    /**
     * <p>마이페이지 구현</p>
     * @param userId 에 따른 MyPage 후원한 프로젝트 목록 조회
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-09-02
     * @author by: 이윤기
     */

    @GetMapping("/me/likedList/{userId}")
    public ResponseEntity<ResponseDto<List<MyPageLikedDto>>> getLikedList(@PathVariable Long userId) {
        return userService.getLikedList(userId);
    }

    @GetMapping("/me/QnAList/{userId}")
    public ResponseEntity<ResponseDto<List<MyPageQnADto>>> getQnAList(@PathVariable Long userId) {
        return userService.getQnAList(userId);
    }

    @GetMapping("/me/QnAListDetail/{userId}/project/{projectId}")
    public ResponseEntity<ResponseDto<MyPageQnADetailDto>> getQnADetail(@PathVariable Long userId, @PathVariable Long projectId){
        return userService.getQnADetail(userId, projectId);
    }

//    @GetMapping("/userStatus/{userId}")
//    public ResponseEntity<ResponseDto<>>

}
