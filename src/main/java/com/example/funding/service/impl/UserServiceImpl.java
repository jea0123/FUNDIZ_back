package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.creator.CreatorQnaDto;
import com.example.funding.dto.response.user.*;
import com.example.funding.exception.UserNotFoundException;
import com.example.funding.mapper.*;
import com.example.funding.model.Creator;
import com.example.funding.model.Project;
import com.example.funding.model.Qna;
import com.example.funding.model.User;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final BackingMapper backingMapper;

    private final CreatorMapper creatorMapper;
    private final QnaMapper qnaMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
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
    public ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "유저 정보를 불러올 수 없습니다."));
        }
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
    public ResponseEntity<ResponseDto<List<MyPageLikedDto>>> getLikedList(Long userId) {
        User user = userMapper.getUserById(userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "좋아요한 프로젝트 목록을 찾을 수없습니다."));
        }
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
    public ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfUser(Long userId, Pager pager) {
        User user = userMapper.getUserById(userId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "QnA 리스트 조회 불가"));
        }

        int total = qnaMapper.qnaTotalOfUser(userId);

        List<CreatorQnaDto> qnaList = qnaMapper.getQnaListOfUser(userId, pager);

        PageResult<CreatorQnaDto> result = PageResult.of(qnaList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "Q&A 목록 조회 성공", result));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(Long userId) {
        User user = userMapper.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException();
        }
        List<RecentViewProject> recentViewProjects = userMapper.getRecentViewProjects(userId);
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
    public ResponseEntity<ResponseDto<MyPageQnADetailDto>> getQnADetail(Long userId, Long projectId) {
        Project project = projectMapper.findById(projectId);
        User user = userMapper.getUserById(userId);
        Creator creator = creatorMapper.findById(project.getCreatorId());
        Qna qna = qnaMapper.getQnAById(userId, projectId);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "잘못된 프로젝트 페이지 입니다."));
        }

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
    public ResponseEntity<ResponseDto<String>> userNickname(Long userId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto<String>> userProfileImg(Long userId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDto<String>> userpassword(Long userId) {
        return null;
    }

}
