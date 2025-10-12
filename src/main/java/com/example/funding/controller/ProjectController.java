package com.example.funding.controller;

import com.example.funding.common.CursorPage;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.CommunityCreateRequestDto;
import com.example.funding.dto.request.project.SearchProjectDto;
import com.example.funding.dto.response.project.*;
import com.example.funding.exception.FeaturedProjectNotFoundException;
import com.example.funding.exception.RecentPaidProjectNotFoundException;
import com.example.funding.service.CommunityService;
import com.example.funding.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final CommunityService communityService;

    /**
     * <p>프로젝트 상세 페이지 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 조은애
     * @since 2025-08-31
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(@PathVariable Long projectId) {
        return projectService.getProjectDetail(projectId);
    }

    /**
     * <p>최근 등록된 프로젝트 상위 10개 조회</p>
     *
     * @return 최근 등록된 프로젝트 상위 10개
     * @throws RecentPaidProjectNotFoundException 최근 24시간 내 결제된 프로젝트가 없을 경우
     * @author by: 장민규
     * @since 2025-09-03
     */
    @GetMapping("/recent-top10")
    public ResponseEntity<ResponseDto<List<RecentTop10ProjectDto>>> getRecentTop10() {
        return projectService.getRecentTop10();
    }

    /**
     * <p>추천 프로젝트 조회</p>
     *
     * @param days  최근 N일 이내 시작된 프로젝트
     * @param limit 최대 조회 개수
     * @return 추천 프로젝트 리스트
     * @throws FeaturedProjectNotFoundException 추천 프로젝트가 없을 경우
     * @author by: 장민규
     * @since 2025-09-04
     */
    @GetMapping("/featured")
    public ResponseEntity<ResponseDto<List<FeaturedProjectDto>>> getFeatured(@RequestParam(defaultValue = "30") int days,
                                                                             @RequestParam(defaultValue = "8") int limit) {
        return projectService.getFeatured(days, limit);
    }

    /**
     * <p>검색 기능 (제목, 내용, 창작자명, 태그)</p>
     *
     * @param dto      SearchProjectDto
     * @param reqPager 요청 pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<PageResult<FeaturedProjectDto>>> searchProject(SearchProjectDto dto, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 20,
                reqPager != null ? reqPager.getPerGroup() : null
        );

        return projectService.searchProject(dto, pager);
    }

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
    @GetMapping("/{projectId}/community")
    public ResponseEntity<ResponseDto<CursorPage<CommunityDto>>> getCommunityList(@PathVariable Long projectId,
                                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                                  @RequestParam(required = false) Long lastId,
                                                                                  @RequestParam(defaultValue = "10") int size) {

        return communityService.getCommunityList(projectId, "CM", lastCreatedAt, lastId, size);
    }

    /**
     *
     * @param projectId
     * @param dto
     * @return
     */
    @PostMapping("{projectId}/community/new")
    public ResponseEntity<ResponseDto<String>> createCommunity(@PathVariable Long projectId,
                                                               @RequestBody CommunityCreateRequestDto dto) {
        //TODO: 임시 id
        Long userId = 22L;

        return communityService.createCommunity(projectId, dto, userId);
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
    @GetMapping("/{projectId}/review")
    public ResponseEntity<ResponseDto<CursorPage<ReviewDto>>> getReviewList(@PathVariable Long projectId,
                                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                            @RequestParam(required = false) Long lastId,
                                                                            @RequestParam(defaultValue = "10") int size) {

        return communityService.getReviewList(projectId, "RV", lastCreatedAt, lastId, size);
    }

}
