package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.user.UserNicknameDto;
import com.example.funding.dto.request.user.UserPasswordDto;
import com.example.funding.dto.request.user.UserProfileImgDto;
import com.example.funding.dto.response.creator.CreatorQnaDto;
import com.example.funding.dto.response.user.*;
import com.example.funding.exception.conflict.DuplicatedFollowCreatorException;
import com.example.funding.exception.conflict.DuplicatedLikedProjectException;
import com.example.funding.exception.notfound.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface UserService {
    /**
     * <p>로그인 사용자 정보 조회</p>
     *
     * @param userId 인증된 사용자의 ID
     * @return 로그인 사용자 정보
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-08-28
     */
    ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(Long userId);

    ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId);

    ResponseEntity<ResponseDto<List<MyPageLikedDto>>> getLikedList(Long userId);

    ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfUser(Long userId, Pager pager);

    /**
     * <p>최근 본 프로젝트 목록 조회</p>
     *
     * @param userId 인증된 사용자의 ID
     * @return 최근 본 프로젝트 목록
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-09-05
     */
    ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(Long userId);

    //서비스에서구현
    ResponseEntity<ResponseDto<MyPageQnADetailDto>> getQnADetail(Long userId, Long projectId);

    ResponseEntity<ResponseDto<String>> userNickname(Long userId, UserNicknameDto dto);

    ResponseEntity<ResponseDto<String>> userProfileImg(Long userId, UserProfileImgDto dto) throws IOException;

    ResponseEntity<ResponseDto<String>> userPassword(Long userId, UserPasswordDto dto);

    /**
     * <p>프로젝트 좋아요</p>
     * @param userId 인증된 사용자의 ID
     * @param projectId 좋아요할 프로젝트 ID
     * @return 좋아요한 프로젝트 ID
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException 프로젝트가 존재하지 않을 때
     * @throws DuplicatedLikedProjectException 이미 좋아요한 프로젝트일 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Long>> likeProject(Long userId, Long projectId);

    /**
     * <p>프로젝트 좋아요 취소</p>
     * @param userId 인증된 사용자의 ID
     * @param projectId 좋아요 취소할 프로젝트 ID
     * @return 좋아요 취소한 프로젝트 ID
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException 프로젝트가 존재하지 않을 때
     * @throws LikedProjectNotFoundException 좋아요한 프로젝트가 아닐 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Long>> dislikeProject(Long userId, Long projectId);

    /**
     * <p>프로젝트 좋아요 여부 확인</p>
     * @param userId 인증된 사용자의 ID
     * @param projectId 확인할 프로젝트 ID
     * @return 프로젝트 좋아요 여부 (true/false)
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException 프로젝트가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Boolean>> checkLikedProject(Long userId, Long projectId);

    /**
     * <p>크리에이터 팔로우 여부 확인</p>
     * @param userId 인증된 사용자의 ID
     * @param creatorId 확인할 크리에이터 ID
     * @return 팔로우한 크리에이터 이름
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException 크리에이터가 존재하지 않을 때
     * @throws DuplicatedFollowCreatorException 이미 팔로우한 크리에이터일 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<String>> followCreator(Long userId, Long creatorId);

    /**
     * <p>크리에이터 언팔로우</p>
     * @param userId 인증된 사용자의 ID
     * @param creatorId 언팔로우할 크리에이터 ID
     * @return 언팔로우한 크리에이터 이름
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException 크리에이터가 존재하지 않을 때
     * @throws FollowingCreatorNotFoundException 팔로우한 크리에이터가 아닐 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<String>> unfollowCreator(Long userId, Long creatorId);

    /**
     * <p>크리에이터 팔로우 여부 확인</p>
     * @param userId 인증된 사용자의 ID
     * @param creatorId 확인할 크리에이터 ID
     * @return 크리에이터 팔로우 여부 (true/false)
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException 크리에이터가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Boolean>> isFollowingCreator(Long userId, Long creatorId);
}