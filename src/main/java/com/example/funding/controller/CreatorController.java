package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.response.creator.CreatorPDetailDto;
import com.example.funding.dto.response.creator.CreatorProjectDetailDto;
import com.example.funding.dto.response.creator.CreatorProjectListDto;
import com.example.funding.service.CreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/creator")
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService creatorService;

    /**
     * <p>창작자의 프로젝트 목록 조회</p>
     * @param creatorId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 202510-02
     * @author by: 이윤기
     */
//   @GetMapping("/{creatorId}/dashBoard") // DEV 프로필 추가: creatorId 프런트에서 헤더 주입
   @GetMapping("/dashBoard")
   public ResponseEntity<ResponseDto<CreatorPDetailDto>> getCreatorDashBoard(@PathVariable Long creatorId){
       return creatorService.getCreatorDashBoard(creatorId);
   }

//    /**
//     * <p>창작자의 프로젝트 목록 조회</p>
//     * @param creatorId
//     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
//     * @since 202510-02
//     * @author by: 이윤기
//     */
//
//    @GetMapping("/{creatorId}/list")
//    public ResponseEntity<ResponseDto<List<CreatorPListDto>>>getCreatorPList(@PathVariable Long creatorId){
//        return creatorService.getCreatorPList(creatorId);
//    }
//
//    /**
//     * <p>창작자의 프로젝트 상세</p>
//     * @param creatorId
//     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
//     * @since 2025-10-02
//     * @author by: 이윤기
//     */
//
//    @GetMapping("/{creatorId}/detail/{projectId}")
//    public ResponseEntity<ResponseDto<CreatorPDetailDto>>getCreatorDList(@PathVariable Long creatorId, @PathVariable Long projectId){
//        return creatorService.getCreatorDList(creatorId, projectId);
//    }

    /**
     * <p>프로젝트 목록 조회</p>
     *
     * @param creatorId 창작자 ID
     * @param dto SearchCreatorProjectDto
     * @param reqPager 요청 pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-05
     */
    @GetMapping("/projects")
    public ResponseEntity<ResponseDto<PageResult<CreatorProjectListDto>>> getProjectList(@RequestAttribute Long creatorId,
                                                                                         SearchCreatorProjectDto dto,
                                                                                         Pager reqPager) {
       Pager pager = Pager.ofRequest(
            reqPager != null ? reqPager.getPage() : 1,
            reqPager != null ? reqPager.getSize() : 5,
            reqPager != null ? reqPager.getPerGroup() : null
        );

        return creatorService.getProjectList(creatorId, dto, pager);
    }

    /**
     * <p>프로젝트 상세 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-05
     */
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ResponseDto<CreatorProjectDetailDto>> getProjectDetail(@PathVariable Long projectId,
                                                                                 @RequestAttribute Long creatorId) {
        return creatorService.getProjectDetail(projectId, creatorId);
    }

    /**
     * <p>프로젝트 생성</p>
     *
     * @param dto ProjectCreateRequestDto
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-09
     */
    @PostMapping("/project/new")
    public ResponseEntity<ResponseDto<String>> createProject(@RequestBody ProjectCreateRequestDto dto,
                                                             @RequestAttribute Long creatorId) {
        //TODO: userId -> creatorId

        return creatorService.createProject(dto, creatorId);
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto ProjectCreateRequestDto
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ResponseDto<String>> updateProject(@PathVariable Long projectId,
                                                             @RequestAttribute Long creatorId,
                                                             @RequestBody ProjectCreateRequestDto dto) {
        //TODO: userId -> creatorId

        if (dto.getProjectId() != null && !dto.getProjectId().equals(projectId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 프로젝트 ID 입니다.");
        }
        dto.setProjectId(projectId);

        return creatorService.updateProject(dto, creatorId);
    }

    /**
     * <p>프로젝트 삭제</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<ResponseDto<String>> deleteProject(@PathVariable Long projectId,
                                                             @RequestAttribute Long creatorId) {
        //TODO: userId -> creatorId

        return creatorService.deleteProject(projectId, creatorId);
    }

    /**
     * <p>프로젝트 심사 요청</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-18
     */
    @PostMapping("/project/{projectId}/submit")
    public ResponseEntity<ResponseDto<String>> verifyProject(@PathVariable Long projectId,
                                                             @RequestAttribute Long creatorId) {
        //TODO: userId -> creatorId

        return creatorService.verifyProject(projectId, creatorId);
    }
}
