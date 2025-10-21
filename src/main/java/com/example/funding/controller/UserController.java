package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.PagerRequest;
import com.example.funding.dto.request.user.UserNicknameDto;
import com.example.funding.dto.request.user.UserPasswordDto;
import com.example.funding.dto.request.user.UserProfileImgDto;
import com.example.funding.dto.response.creator.CreatorQnaDto;
import com.example.funding.dto.response.user.*;
import com.example.funding.exception.conflict.DuplicatedFollowCreatorException;
import com.example.funding.exception.conflict.DuplicatedLikedProjectException;
import com.example.funding.exception.notfound.*;
import com.example.funding.service.AuthService;
import com.example.funding.service.ProjectService;
import com.example.funding.service.UserService;
import jakarta.validation.Valid;
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
    private final AuthService authService;

    /**
     * <p>로그인 사용자 정보 조회</p>
     *
     * @param principal 인증된 사용자의 정보
     * @return 로그인 사용자 정보
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     */
    @GetMapping("/loginUser")
    public ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return userService.getLoginUser(principal.userId());
    }

    @PostMapping("/recentView/{projectId}")
    public ResponseEntity<ResponseDto<?>> addRecentViewProject(@PathVariable Long projectId,
                                                               @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.addRecentViewProject(principal.userId(), projectId);
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
    public ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                                                      @RequestParam(required = false) Integer limit) {
        return userService.getRecentViewProjects(principal.userId(), limit != null ? limit : 10);
    }

    @PostMapping("/nickname")
    public ResponseEntity<ResponseDto<String>> updateNickname(@Valid @RequestBody UserNicknameDto dto,
                                                              @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.userNickname(principal.userId(), dto);
    }

    @PostMapping("/profileImg")
    public ResponseEntity<ResponseDto<String>> updateProfileImg(@Valid @ModelAttribute UserProfileImgDto dto,
                                                                @AuthenticationPrincipal CustomUserPrincipal principal
    ) throws Exception {
        return userService.userProfileImg(principal.userId(), dto);
    }

    @PostMapping("/password")
    public ResponseEntity<ResponseDto<String>> updatePassword(@Valid @RequestBody UserPasswordDto dto,
                                                              @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.userPassword(principal.userId(), dto);
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

    /**
     * <p>사용자 Q&A 목록</p>
     *
     * @param userId 사용자 ID
     * @param req    요청 pager
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-05
     */
    @GetMapping("/qna/{userId}")
    public ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfUser(@PathVariable Long userId, @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
        return userService.getQnaListOfUser(userId, pager);
    }

    /**
     * <p>프로젝트 좋아요</p>
     *
     * @param projectId 좋아요할 프로젝트 ID
     * @param principal 인증된 사용자의 정보
     * @return 좋아요한 프로젝트 ID
     * @throws UserNotFoundException           사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException        프로젝트가 존재하지 않을 때
     * @throws DuplicatedLikedProjectException 이미 좋아요한 프로젝트일 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    @PostMapping("/like/{projectId}")
    public ResponseEntity<ResponseDto<Long>> likeProject(@PathVariable Long projectId,
                                                         @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.likeProject(principal.userId(), projectId);
    }

    /**
     * <p>프로젝트 좋아요 취소</p>
     *
     * @param projectId 좋아요 취소할 프로젝트 ID
     * @param principal 인증된 사용자의 정보
     * @return 좋아요 취소한 프로젝트 ID
     * @throws UserNotFoundException         사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException      프로젝트가 존재하지 않을 때
     * @throws LikedProjectNotFoundException 좋아요한 프로젝트가 아닐 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    @DeleteMapping("/dislike/{projectId}")
    public ResponseEntity<ResponseDto<Long>> dislikeProject(@PathVariable Long projectId,
                                                            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.dislikeProject(principal.userId(), projectId);
    }

    /**
     * <p>프로젝트 좋아요 여부 확인</p>
     *
     * @param projectId 확인할 프로젝트 ID
     * @param principal 인증된 사용자의 정보
     * @return 프로젝트 좋아요 여부 (true/false)
     * @throws UserNotFoundException    사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException 프로젝트가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    @GetMapping("/checkLike/{projectId}")
    public ResponseEntity<ResponseDto<Boolean>> isProjectLiked(@PathVariable Long projectId,
                                                               @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.checkLikedProject(principal.userId(), projectId);
    }

    /**
     * <p>크리에이터 팔로우</p>
     *
     * @param creatorId 팔로우할 크리에이터 ID
     * @param principal 인증된 사용자의 정보
     * @return 팔로우한 크리에이터 ID
     * @throws UserNotFoundException            사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException         크리에이터가 존재하지 않을 때
     * @throws DuplicatedFollowCreatorException 이미 팔로우한 크리에이터일 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    @PostMapping("/follow/{creatorId}")
    public ResponseEntity<ResponseDto<String>> followCreator(@PathVariable Long creatorId,
                                                             @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.followCreator(principal.userId(), creatorId);
    }

    /**
     * <p>크리에이터 언팔로우</p>
     *
     * @param creatorId 언팔로우할 크리에이터 ID
     * @param principal 인증된 사용자의 정보
     * @return 언팔로우한 크리에이터 ID
     * @throws UserNotFoundException             사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException          크리에이터가 존재하지 않을 때
     * @throws FollowingCreatorNotFoundException 팔로우한 크리에이터가 아닐 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    @DeleteMapping("/unfollow/{creatorId}")
    public ResponseEntity<ResponseDto<String>> unfollowCreator(@PathVariable Long creatorId,
                                                               @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.unfollowCreator(principal.userId(), creatorId);
    }

    /**
     * <p>크리에이터 팔로우 여부 확인</p>
     *
     * @param creatorId 확인할 크리에이터 ID
     * @param principal 인증된 사용자의 정보
     * @return 크리에이터 팔로우 여부 (true/false)
     * @throws UserNotFoundException    사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException 크리에이터가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    @GetMapping("/checkFollow/{creatorId}")
    public ResponseEntity<ResponseDto<Boolean>> isFollowingCreator(@PathVariable Long creatorId,
                                                                   @AuthenticationPrincipal CustomUserPrincipal principal
    ) {
        return userService.isFollowingCreator(principal.userId(), creatorId);
    }

    /**
     * <p>사용자 요약 정보 조회</p>
     *
     * @param principal 인증된 사용자의 정보
     * @return 사용자 요약 정보
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-10-21
     */
    @GetMapping("/summary")
    public ResponseEntity<ResponseDto<UserSummaryDto>> getUserSummary(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return userService.getUserSummary(principal.userId());
    }

    /**
     * <p>회원 탈퇴</p>
     *
     * @param principal 인증된 사용자의 정보
     * @return 탈퇴 완료 메시지
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-10-21
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<ResponseDto<String>> withdrawUser(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return authService.withdrawUser(principal.userId());
    }
}
