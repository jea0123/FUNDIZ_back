package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.user.UserNicknameDto;
import com.example.funding.dto.request.user.UserPasswordDto;
import com.example.funding.dto.request.user.UserProfileImgDto;
import com.example.funding.dto.response.creator.CreatorQnaDto;
import com.example.funding.dto.response.user.*;
import com.example.funding.exception.UserNotFoundException;
import com.example.funding.service.ProjectService;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
     *
     * @param principal 인증된 사용자의 정보
     * @return 로그인 사용자 정보
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     */
    @GetMapping("/loginUser")
    public ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(
//            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
//        Long userId = principal.userId();
        Long userId = 501L; // TODO: 임시
        return userService.getLoginUser(userId);
    }

    /**
     * <p>최근 본 프로젝트 목록 조회</p>
     *
     * @param principal 인증된 사용자의 정보
     * @return 최근 본 프로젝트 목록
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-09-05
     */
    @GetMapping("/recentViewProjects")
    public ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(
//            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
//        Long userId = principal.userId();
        Long userId = 501L; // TODO: 임시
        return userService.getRecentViewProjects(userId);
    }

    /**
     * <p>마이페이지 구현</p>
     *
     * @param userId 에 따른 MyPage 프로필 조회
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-02
     */
    @GetMapping("/userPage/{userId}")
    public ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(@PathVariable Long userId) {
        return userService.getMyPageUser(userId);
    }

    @PostMapping("/nickname")
    public ResponseEntity<ResponseDto<String>> updateNickname(@RequestBody UserNicknameDto dto
//                                                              @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
//        Long userId = principal.userId();
        Long userId = 501L; // TODO: 임시
        return userService.userNickname(userId, dto);
    }

    @PostMapping("/profileImg")
    public ResponseEntity<ResponseDto<String>> updateProfileImg(@ModelAttribute UserProfileImgDto dto
//                                                                @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
//        Long userId = principal.userId();
        Long userId = 501L; // TODO: 임시
        return userService.userProfileImg(userId, dto);
    }

    @PostMapping("/password")
    public ResponseEntity<ResponseDto<String>> updatePassword(@RequestBody UserPasswordDto dto
//                                                              @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
//        Long userId = principal.userId();
        Long userId = 501L; // TODO: 임시
        return userService.userPassword(userId, dto);
    }

    /**
     * <p>마이페이지 구현</p>
     *
     * @param userId 에 따른 MyPage 후원한 프로젝트 목록 조회
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-02
     */

    @GetMapping("/likedList/{userId}")
    public ResponseEntity<ResponseDto<List<MyPageLikedDto>>> getLikedList(@PathVariable Long userId) {
        return userService.getLikedList(userId);
    }

    @GetMapping("/qna/{userId}")
    public ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfUser(@PathVariable Long userId, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        return userService.getQnaListOfUser(userId, pager);
    }

    @GetMapping("/QnAListDetail/{userId}/project/{projectId}")
    public ResponseEntity<ResponseDto<MyPageQnADetailDto>> getQnADetail(@PathVariable Long userId, @PathVariable Long projectId) {
        return userService.getQnADetail(userId, projectId);
    }

}
