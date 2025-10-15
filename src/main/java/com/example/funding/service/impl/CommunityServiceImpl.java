package com.example.funding.service.impl;

import com.example.funding.common.Cursor;
import com.example.funding.common.CursorPage;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.CommunityCreateRequestDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.dto.response.project.ReviewDto;
import com.example.funding.mapper.CommunityMapper;
import com.example.funding.mapper.ReplyMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.Community;
import com.example.funding.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;
    private final UserMapper userMapper;
    private final ReplyMapper replyMapper;

    /**
     * <p>프로젝트 상세 페이지 내 커뮤니티 목록 조회</p>
     *
     * @param projectId     프로젝트 ID
     * @param code          CM
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 cmId
     * @param size          한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-03
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CursorPage<CommunityDto>>> getCommunityList(Long projectId, String code, LocalDateTime lastCreatedAt, Long lastId, int size) {
        List<CommunityDto> communityList = communityMapper.getCommunityList(projectId, "CM", lastCreatedAt, lastId, size);

        Cursor next = null;
        if (!communityList.isEmpty()) {
            CommunityDto last = communityList.getLast();
            next = new Cursor(last.getCreatedAt(), last.getCmId());
        }

        return ResponseEntity.ok(ResponseDto.success(200, "커뮤니티 조회 성공", CursorPage.of(communityList, next)));
    }

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 등록</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto       CommunityCreateRequestDto
     * @param userId    사용자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-12
     */
    @Override
    public ResponseEntity<ResponseDto<String>> createCommunity(Long projectId, CommunityCreateRequestDto dto, Long userId) {
        //TODO: guard, validator

        Community community = Community.builder()
                .projectId(projectId)
                .userId(userId)
                .cmContent(dto.getCmContent())
                .build();

        int result = communityMapper.createCommunity(community);
        if (result != 1) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "커뮤니티 등록 실패");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "커뮤니티 등록 성공", null));
    }

    /**
     * <p>프로젝트 상세 페이지 내 후기 목록 조회</p>
     *
     * @param projectId     프로젝트 ID
     * @param code          RV
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 cmId
     * @param size          한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-03
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<CursorPage<ReviewDto>>> getReviewList(Long projectId, String code, LocalDateTime lastCreatedAt, Long lastId, int size) {
        List<ReviewDto> reviewList = communityMapper.getReviewList(projectId, "RV", lastCreatedAt, lastId, size);

        Cursor next = null;
        if (!reviewList.isEmpty()) {
            ReviewDto last = reviewList.getLast();
            next = new Cursor(last.getCreatedAt(), last.getCmId());
        }

        return ResponseEntity.ok(ResponseDto.success(200, "리뷰 조회 성공", CursorPage.of(reviewList, next)));
    }
}
