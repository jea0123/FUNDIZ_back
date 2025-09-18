package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/project/{projectId}")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    /**
     * <p>프로젝트 상세 페이지 내 커뮤니티 목록 조회</p>
     * @param projectId 프로젝트 ID
     * @param reqPager 요청 pager
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-03
     */
    @GetMapping("/community")
    public ResponseEntity<ResponseDto<PageResult<CommunityDto>>> getCommunity(@PathVariable("projectId") Long projectId, Pager reqPager) {
        return communityService.getCommunity(projectId, "CM", reqPager);
    }

    /**
     * <p>프로젝트 상세 페이지 내 후기 목록 조회</p>
     * @param projectId 프로젝트 ID
     * @param reqPager 요청 pager
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-03
     */
    @GetMapping("/review")
    public ResponseEntity<ResponseDto<PageResult<CommunityDto>>> getReview(@PathVariable("projectId") Long projectId, Pager reqPager) {
        return communityService.getCommunity(projectId, "RV", reqPager);
    }
}
