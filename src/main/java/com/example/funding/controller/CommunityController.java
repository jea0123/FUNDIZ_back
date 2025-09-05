package com.example.funding.controller;

import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1/project/{projectId}")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    /**
     * <p>프로젝트 상세 페이지 내 커뮤니티 목록 조회/p>
     * @param projectId 프로젝트 ID
     * @param pager 페이징 정보
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 조은애
     * @since 2025-09-02
     */
    @GetMapping("/community")
    public ResponseEntity<ResponseDto<List<CommunityDto>>> getCommunity(@PathVariable("projectId") Long projectId, Pager pager) {
        return communityService.getCommunity(projectId, "CM", pager);
    }

    /**
     * <p>프로젝트 상세 페이지 내 후기 목록 조회/p>
     * @param projectId 프로젝트 ID
     * @param pager 페이징 정보
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 조은애
     * @since 2025-09-02
     */
    @GetMapping("/review")
    public ResponseEntity<ResponseDto<List<CommunityDto>>> getReview(@PathVariable("projectId") Long projectId, Pager pager) {
        return communityService.getCommunity(projectId, "RV", pager);
    }
}
