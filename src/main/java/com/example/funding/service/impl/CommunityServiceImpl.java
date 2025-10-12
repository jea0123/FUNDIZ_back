package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.*;
import com.example.funding.mapper.CommunityMapper;
import com.example.funding.mapper.ReplyMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;
    private final UserMapper userMapper;
    private final ReplyMapper replyMapper;

    /**
     * <p>프로젝트 상세 페이지 내 커뮤니티 목록 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @param code CM
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId 마지막 항목의 cmId
     * @param size 한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-03
     */
    @Override
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
     * <p>프로젝트 상세 페이지 내 후기 목록 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @param code RV
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId 마지막 항목의 cmId
     * @param size 한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-03
     */
    @Override
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
