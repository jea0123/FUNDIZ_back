package com.example.funding.service.impl;

import com.example.funding.common.FileUploader;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.user.UserNicknameDto;
import com.example.funding.dto.request.user.UserPasswordDto;
import com.example.funding.dto.request.user.UserProfileImgDto;
import com.example.funding.dto.response.creator.CreatorQnaDto;
import com.example.funding.dto.response.user.*;
import com.example.funding.enums.NotificationType;
import com.example.funding.exception.conflict.DuplicatedFollowCreatorException;
import com.example.funding.exception.conflict.DuplicatedLikedProjectException;
import com.example.funding.exception.conflict.DuplicatedPasswordException;
import com.example.funding.exception.forbidden.InCorrectPasswordException;
import com.example.funding.exception.notfound.FollowingCreatorNotFoundException;
import com.example.funding.exception.notfound.LikedProjectNotFoundException;
import com.example.funding.exception.notfound.QnANotFoundException;
import com.example.funding.handler.NotificationPublisher;
import com.example.funding.mapper.*;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import com.example.funding.model.Qna;
import com.example.funding.model.User;
import com.example.funding.service.UserService;
import com.example.funding.validator.Loaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final Loaders loaders;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final CreatorMapper creatorMapper;
    private final QnaMapper qnaMapper;
    private final FollowMapper followMapper;
    private final PasswordEncoder passwordEncoder;
    private final FileUploader fileUploader;

    private final NotificationPublisher notificationPublisher;

    /**
     * 로그인 사용자 정보
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId) {
        User user = loaders.user(userId);

        Long creatorId = userMapper.getCreatorIdByUserId(userId);
        LoginUserDto loginUserDto = LoginUserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .joinedAt(user.getJoinedAt())
                .followCnt(user.getFollowCnt())
                .isCreator(user.getIsCreator())
                .creatorId(creatorId)
                .role(user.getRole().toString())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용자 정보 조회 성공", loginUserDto));
    }

    /**
     * <p>로그인 사용자 개인정보</p>
     *
     * @param userId 사용자 후원리스트
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-03
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId) {
        User user = loaders.user(userId);
        MyPageUserDto mypageUserDto = MyPageUserDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "유저 정보 불러오기 성공", mypageUserDto));
    }

    /**
     * <p>로그인 사용자 좋아요 리스트</p>
     *
     * @param userId 사용자
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-05
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<MyPageLikedDto>>> getLikedList(Long userId) {
        loaders.user(userId);
        List<MyPageLikedDto> LikedList = userMapper.getLikedList(userId);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "좋아요한 프로젝트 리스트 조회 성공", LikedList));
    }

    /**
     * <p>사용자 Q&A 목록</p>
     *
     * @param userId 사용자 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-05
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfUser(Long userId, Pager pager) {
        loaders.user(userId);

        int total = qnaMapper.qnaTotalOfUser(userId);
        List<CreatorQnaDto> qnaList = qnaMapper.getQnaListOfUser(userId, pager);
        PageResult<CreatorQnaDto> result = PageResult.of(qnaList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "Q&A 목록 조회 성공", result));
    }

    @Override
    public ResponseEntity<ResponseDto<?>> addRecentViewProject(Long userId, Long projectId) {
        loaders.user(userId);
        loaders.project(projectId);
        userMapper.upsertRecentView(userId, projectId);
        return ResponseEntity.ok(ResponseDto.success(200, "최근 본 프로젝트 추가 성공", null));
    }

    /**
     * 최근 본 프로젝트
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(Long userId, int limit) {
        loaders.user(userId);

        List<RecentViewProject> recentViewProjects = userMapper.getRecentViewProjects(userId, limit);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "최근 본 프로젝트 조회 성공", recentViewProjects));
    }

    /**
     * <p>로그인 사용자 QnA 리스트 상세</p>
     *
     * @param userId 사용자
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-09-07
     */
    //서비스에서구현
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<MyPageQnADetailDto>> getQnADetail(Long userId, Long projectId) {
        Project project = loaders.project(projectId);
        User user = loaders.user(userId);
        Creator creator = loaders.creator(project.getCreatorId());

        Qna qna = qnaMapper.getQnAById(userId, projectId);
        if (qna == null) throw new QnANotFoundException();

        MyPageQnADetailDto myPageQnADetail = MyPageQnADetailDto.builder()
                //qna
                .qnaId(qna.getQnaId())
                .title(qna.getTitle())
                .projectId(project.getProjectId())
                .userId(qna.getUserId())
                .createdAt(qna.getCreatedAt())
                .content(qna.getContent())
                // 프로젝트
                .projectTitle(project.getTitle())
                //창작자
                .creatorName(creator.getCreatorName())
                .creatorId(creator.getCreatorId())
                // 유저
                .nickName(user.getNickname())

                .build();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "QnA 상세 페이지 조회 성공", myPageQnADetail));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> userNickname(Long userId, UserNicknameDto dto) {
        loaders.user(userId);
        userMapper.updateNickname(userId, dto.getNickname());
        return ResponseEntity.ok(ResponseDto.success(200, "닉네임 변경 성공", dto.getNickname()));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> userProfileImg(Long userId, UserProfileImgDto dto) throws Exception {
        loaders.user(userId);
        String profileImgUrl = fileUploader.upload(dto.getProfileImg());
        userMapper.updateProfile(userId, profileImgUrl);
        return ResponseEntity.ok(ResponseDto.success(200, "프로필 이미지 변경 성공", profileImgUrl));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> userPassword(Long userId, UserPasswordDto dto) {
        User user = loaders.user(userId);
        if (!passwordEncoder.matches(dto.getPassword(), user.getPwd())) throw new InCorrectPasswordException();
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPwd())) throw new DuplicatedPasswordException();

        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        userMapper.updatePwd(userId, encodedNewPassword);
        return ResponseEntity.ok(ResponseDto.success(200, "비밀번호 변경 성공", "********"));
    }

    /**
     * 프로젝트 좋아요 추가
     */
    @Override
    public ResponseEntity<ResponseDto<Long>> likeProject(Long userId, Long projectId) {
        loaders.user(userId);
        loaders.project(projectId);
        if (userMapper.isProjectLiked(userId, projectId) == 1) throw new DuplicatedLikedProjectException();
        userMapper.likeProject(userId, projectId);
        projectMapper.increaseLikeCnt(projectId);
        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 좋아요 성공", projectId));
    }

    /**
     * 프로젝트 좋아요 취소
     */
    @Override
    public ResponseEntity<ResponseDto<Long>> dislikeProject(Long userId, Long projectId) {
        loaders.user(userId);
        loaders.project(projectId);
        if (userMapper.isProjectLiked(userId, projectId) == 0) throw new LikedProjectNotFoundException();
        userMapper.dislikeProject(userId, projectId);
        projectMapper.decreaseLikeCnt(projectId);
        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 좋아요 취소 성공", projectId));
    }

    /**
     * 프로젝트 좋아요 여부 확인
     */
    @Override
    public ResponseEntity<ResponseDto<Boolean>> checkLikedProject(Long userId, Long projectId) {
        loaders.user(userId);
        loaders.project(projectId);
        int isLiked = userMapper.isProjectLiked(userId, projectId);
        if (isLiked > 0) {
            return ResponseEntity.ok(ResponseDto.success(200, "좋아요한 프로젝트입니다.", true));
        } else {
            return ResponseEntity.ok(ResponseDto.success(200, "좋아요하지 않은 프로젝트입니다.", false));
        }
    }

    /**
     * 크리에이터 팔로우
     */
    @Override
    public ResponseEntity<ResponseDto<String>> followCreator(Long userId, Long creatorId) {
        User existingUser = loaders.user(userId);
        Creator creator = loaders.creator(creatorId);
        if (followMapper.isFollowingCreator(userId, creatorId) == 1) throw new DuplicatedFollowCreatorException();
        followMapper.followCreator(userId, creatorId);
        creatorMapper.increaseFollowersCount(creatorId);

        notificationPublisher.publish(userId, NotificationType.NEW_FOLLOWER, existingUser.getNickname(), null);
        return ResponseEntity.ok(ResponseDto.success(200, "크리에이터 팔로우 성공", creator.getCreatorName()));
    }

    /**
     * 크리에이터 언팔로우
     */
    @Override
    public ResponseEntity<ResponseDto<String>> unfollowCreator(Long userId, Long creatorId) {
        loaders.user(userId);
        Creator creator = loaders.creator(creatorId);
        if (followMapper.isFollowingCreator(userId, creatorId) == 0) throw new FollowingCreatorNotFoundException();
        followMapper.unfollowCreator(userId, creatorId);
        creatorMapper.decreaseFollowersCount(creatorId);
        return ResponseEntity.ok(ResponseDto.success(200, "크리에이터 언팔로우 성공", creator.getCreatorName()));
    }

    /**
     * 크리에이터 팔로우 여부 확인
     */
    @Override
    public ResponseEntity<ResponseDto<Boolean>> isFollowingCreator(Long userId, Long creatorId) {
        loaders.user(userId);
        loaders.creator(creatorId);
        int isFollowing = followMapper.isFollowingCreator(userId, creatorId);
        if (isFollowing > 0) {
            return ResponseEntity.ok(ResponseDto.success(200, "팔로우한 크리에이터입니다.", true));
        } else {
            return ResponseEntity.ok(ResponseDto.success(200, "팔로우하지 않은 크리에이터입니다.", false));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<UserSummaryDto>> getUserSummary(Long userId) {
        loaders.user(userId);
        UserSummaryDto dto = userMapper.findUserSummary(userId);
        return ResponseEntity.ok(ResponseDto.success(200, "유저 요약 정보 조회 성공", dto));
    }
}
