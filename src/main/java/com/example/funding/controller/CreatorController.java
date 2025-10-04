package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.ProjectCreateRequestDto;
import com.example.funding.dto.request.project.ProjectUpdateRequestDto;
import com.example.funding.dto.response.creator.CreatorPDetailDto;
import com.example.funding.dto.response.creator.CreatorPListDto;
import com.example.funding.service.CreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
   @GetMapping("/{creatorId}/dashBoard")
   public ResponseEntity<ResponseDto<CreatorPDetailDto>> getCreatorDashBoard(@PathVariable Long creatorId){
       return creatorService.getCreatorDashBoard(creatorId);
   }

    /**
     * <p>창작자의 프로젝트 목록 조회</p>
     * @param creatorId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 202510-02
     * @author by: 이윤기
     */

    @GetMapping("/{creatorId}/list")
    public ResponseEntity<ResponseDto<List<CreatorPListDto>>>getCreatorPList(@PathVariable Long creatorId){
        return creatorService.getCreatorPList(creatorId);
    }

    /**
     * <p>창작자의 프로젝트 상세</p>
     * @param creatorId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @since 2025-10-02
     * @author by: 이윤기
     */

    @GetMapping("/{creatorId}/detail/{projectId}")
    public ResponseEntity<ResponseDto<CreatorPDetailDto>>getCreatorDList(@PathVariable Long creatorId, @PathVariable Long projectId){
        return creatorService.getCreatorDList(creatorId, projectId);
    }

    /**
     * <p>프로젝트 생성</p>
     *
     * @param dto ProjectCreateRequestDto
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-09
     */
    @PostMapping("/project")
    public ResponseEntity<ResponseDto<String>> createProject(@RequestBody ProjectCreateRequestDto dto) {
        //TODO: userId -> creatorId
        Long creatorId = 1L;

        return creatorService.createProject(dto, creatorId);
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
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ResponseDto<String>> updateProject(@PathVariable Long projectId, @RequestBody ProjectUpdateRequestDto dto) {
        //TODO: userId -> creatorId
        Long creatorId = 1L;

        dto.setProjectId(projectId);
        return creatorService.updateProject(dto, creatorId);
    }

    /**
     * <p>프로젝트 삭제</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-16
     */
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<ResponseDto<String>> deleteByCreator(@PathVariable Long projectId) {
        //TODO: userId -> creatorId
        Long creatorId = 1L;

        return creatorService.deleteByCreator(projectId, creatorId);
    }

    /**
     * <p>프로젝트 심사 요청</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-18
     */
    @PostMapping("/project/verify/{projectId}")
    public ResponseEntity<ResponseDto<String>> verifyProject(@PathVariable Long projectId){
        //TODO: userId -> creatorId
        Long creatorId = 1L;

        return creatorService.verifyProject(projectId, creatorId);
    }
}
