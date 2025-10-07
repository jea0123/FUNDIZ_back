package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.dto.response.project.CursorPage;
import com.example.funding.dto.response.project.ReviewDto;
import com.example.funding.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/project/{projectId}")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    /**
     * <p>프로젝트 상세 페이지 내 커뮤니티 목록 조회</p>
     * @param projectId 프로젝트 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId 마지막 항목의 cmId
     * @param size 한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-03
     */
    @GetMapping("/community")
    public ResponseEntity<ResponseDto<CursorPage<CommunityDto>>> getCommunityList(@PathVariable("projectId") Long projectId,
                                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                                  @RequestParam(required = false) Long lastId,
                                                                                  @RequestParam(defaultValue = "10") int size) {

        return communityService.getCommunityList(projectId, "CM", lastCreatedAt, lastId, size);
    }

    /**
     * <p>프로젝트 상세 페이지 내 후기 목록 조회</p>
     * @param projectId 프로젝트 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId 마지막 항목의 cmId
     * @param size 한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-03
     */
    @GetMapping("/review")
    public ResponseEntity<ResponseDto<CursorPage<ReviewDto>>> getReviewList(@PathVariable("projectId") Long projectId,
                                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                            @RequestParam(required = false) Long lastId,
                                                                            @RequestParam(defaultValue = "10") int size) {

        return communityService.getReviewList(projectId, "RV", lastCreatedAt, lastId, size);
    }
}
