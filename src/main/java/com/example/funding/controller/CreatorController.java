package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.creator.CreatorPDetailDto;
import com.example.funding.dto.response.creator.CreatorPListDto;
import com.example.funding.service.CreatorService;
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
}
