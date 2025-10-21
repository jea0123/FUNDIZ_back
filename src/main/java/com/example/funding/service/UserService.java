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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
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
    ResponseEntity<ResponseDto<LoginUserDto>> getLoginUser(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId);

    ResponseEntity<ResponseDto<MyPageUserDto>> getMyPageUser(Long userId);

    ResponseEntity<ResponseDto<List<MyPageLikedDto>>> getLikedList(Long userId);

    ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfUser(Long userId, Pager pager);

    ResponseEntity<ResponseDto<?>> addRecentViewProject(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId,
            @NotNull(message = "projectId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "projectId는 양수여야 합니다. 현재: ${validatedValue}")
            Long projectId);

    /**
     * <p>최근 본 프로젝트 목록 조회</p>
     *
     * @param userId 인증된 사용자의 ID
     * @return 최근 본 프로젝트 목록
     * @throws UserNotFoundException 사용자가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-09-05
     */
    ResponseEntity<ResponseDto<List<RecentViewProject>>> getRecentViewProjects(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId, int limit);

    //서비스에서구현
    ResponseEntity<ResponseDto<MyPageQnADetailDto>> getQnADetail(Long userId, Long projectId);

    ResponseEntity<ResponseDto<String>> userNickname(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId, UserNicknameDto dto);

    ResponseEntity<ResponseDto<String>> userProfileImg(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId, UserProfileImgDto dto) throws Exception;

    ResponseEntity<ResponseDto<String>> userPassword(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId, UserPasswordDto dto);

    /**
     * <p>프로젝트 좋아요</p>
     *
     * @param userId    인증된 사용자의 ID
     * @param projectId 좋아요할 프로젝트 ID
     * @return 좋아요한 프로젝트 ID
     * @throws UserNotFoundException           사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException        프로젝트가 존재하지 않을 때
     * @throws DuplicatedLikedProjectException 이미 좋아요한 프로젝트일 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Long>> likeProject(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId,
            @NotNull(message = "projectId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "projectId는 양수여야 합니다. 현재: ${validatedValue}")
            Long projectId);

    /**
     * <p>프로젝트 좋아요 취소</p>
     *
     * @param userId    인증된 사용자의 ID
     * @param projectId 좋아요 취소할 프로젝트 ID
     * @return 좋아요 취소한 프로젝트 ID
     * @throws UserNotFoundException         사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException      프로젝트가 존재하지 않을 때
     * @throws LikedProjectNotFoundException 좋아요한 프로젝트가 아닐 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Long>> dislikeProject(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId,
            @NotNull(message = "projectId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "projectId는 양수여야 합니다. 현재: ${validatedValue}")
            Long projectId);

    /**
     * <p>프로젝트 좋아요 여부 확인</p>
     *
     * @param userId    인증된 사용자의 ID
     * @param projectId 확인할 프로젝트 ID
     * @return 프로젝트 좋아요 여부 (true/false)
     * @throws UserNotFoundException    사용자가 존재하지 않을 때
     * @throws ProjectNotFoundException 프로젝트가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Boolean>> checkLikedProject(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId,
            @NotNull(message = "projectId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "projectId는 양수여야 합니다. 현재: ${validatedValue}")
            Long projectId);

    /**
     * <p>크리에이터 팔로우 여부 확인</p>
     *
     * @param userId    인증된 사용자의 ID
     * @param creatorId 확인할 크리에이터 ID
     * @return 팔로우한 크리에이터 이름
     * @throws UserNotFoundException            사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException         크리에이터가 존재하지 않을 때
     * @throws DuplicatedFollowCreatorException 이미 팔로우한 크리에이터일 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<String>> followCreator(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId,
            @NotNull(message = "creatorId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "creatorId는 양수여야 합니다. 현재: ${validatedValue}")
            Long creatorId);

    /**
     * <p>크리에이터 언팔로우</p>
     *
     * @param userId    인증된 사용자의 ID
     * @param creatorId 언팔로우할 크리에이터 ID
     * @return 언팔로우한 크리에이터 이름
     * @throws UserNotFoundException             사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException          크리에이터가 존재하지 않을 때
     * @throws FollowingCreatorNotFoundException 팔로우한 크리에이터가 아닐 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<String>> unfollowCreator(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId,
            @NotNull(message = "creatorId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "creatorId는 양수여야 합니다. 현재: ${validatedValue}")
            Long creatorId);

    /**
     * <p>크리에이터 팔로우 여부 확인</p>
     *
     * @param userId    인증된 사용자의 ID
     * @param creatorId 확인할 크리에이터 ID
     * @return 크리에이터 팔로우 여부 (true/false)
     * @throws UserNotFoundException    사용자가 존재하지 않을 때
     * @throws CreatorNotFoundException 크리에이터가 존재하지 않을 때
     * @author by: 장민규
     * @since 2025-10-15
     */
    ResponseEntity<ResponseDto<Boolean>> isFollowingCreator(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId,
            @NotNull(message = "creatorId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "creatorId는 양수여야 합니다. 현재: ${validatedValue}")
            Long creatorId);

    /**
     * 사용자 요약 정보 조회
     *
     * @param userId 유저 ID
     * @return 유저 요약 정보
     * @author 장민규
     * @since 2025-10-21
     */
    ResponseEntity<ResponseDto<UserSummaryDto>> getUserSummary(
            @NotNull(message = "userId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "userId는 양수여야 합니다. 현재: ${validatedValue}")
            Long userId);
}