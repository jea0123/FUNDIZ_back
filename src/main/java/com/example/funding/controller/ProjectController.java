package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.ProjectCreateRequestDto;
import com.example.funding.dto.request.project.ProjectUpdateRequestDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.dto.response.project.SearchProjectDto;
import com.example.funding.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * <p>프로젝트 상세 페이지 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 조은애
     * @since 2025-08-31
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(@PathVariable Long projectId) {
        return projectService.getProjectDetail(projectId);
    }

    /**
     * <p>최근 등록된 프로젝트 상위 10개 조회</p>
     *
     * @return 성공 시 200 OK
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
     * @return 성공 시 200 OK
     * @author by: 장민규
     * @since 2025-09-04
     */
    @GetMapping("/featured")
    public ResponseEntity<ResponseDto<List<FeaturedProjectDto>>> getFeatured(@RequestParam(defaultValue = "30") int days,
                                                                             @RequestParam(defaultValue = "8") int limit) {
        return projectService.getFeatured(days, limit);
    }

    /**
     * <p>프로젝트 생성</p>
     *
     * @param dto ProjectCreateRequestDto
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-09
     */
    @PostMapping
    public ResponseEntity<ResponseDto<String>> createProject(@RequestBody ProjectCreateRequestDto dto) {
        //userId -> creatorId
        Long creatorId = 1L;

        return projectService.createProject(dto, creatorId);
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto ProjectUpdateRequestDto
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-16
     */
    @PostMapping("/{projectId}")
    public ResponseEntity<ResponseDto<String>> updateProject(@PathVariable Long projectId, @RequestBody ProjectUpdateRequestDto dto) {
        //userId -> creatorId
        Long creatorId = 1L;

        dto.setProjectId(projectId);
        return projectService.updateProject(dto, creatorId);
    }

    /**
     * <p>프로젝트 삭제(창작자)</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-16
     */
    @DeleteMapping("/{projectId}")
    public ResponseEntity<ResponseDto<String>> deleteByCreator(@PathVariable Long projectId) {
        //userId -> creatorId
        Long creatorId = 1L;

        return projectService.deleteByCreator(projectId, creatorId);
    }
//    프로젝트 취소(관리자)
//    @PostMapping("/{projectId}/cancel")
//    public ResponseEntity<ResponseDto<String>> cancelByAdmin(@PathVariable Long projectId, @RequestBody)

    /**
     * <p>검색 기능 (제목, 내용, 창작자명, 태그)</p>
     *
     * @param dto SearchProjectDto
     * @param pager 페이지네이션
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-16
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<PageResult<FeaturedProjectDto>>> search(SearchProjectDto dto, Pager pager) {
        pager.setDefault();

        return projectService.search(dto, pager);
    }

}
