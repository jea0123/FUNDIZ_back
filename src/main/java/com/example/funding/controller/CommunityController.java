package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.QnaAddRequestDto;
import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.dto.response.project.CursorPage;
import com.example.funding.dto.response.project.ReviewDto;
import com.example.funding.model.Qna;
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
     * @author by: 조은애
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
     * @author by: 조은애
     * @since 2025-10-03
     */
    @GetMapping("/review")
    public ResponseEntity<ResponseDto<CursorPage<ReviewDto>>> getReviewList(@PathVariable("projectId") Long projectId,
                                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                            @RequestParam(required = false) Long lastId,
                                                                            @RequestParam(defaultValue = "10") int size) {

        return communityService.getReviewList(projectId, "RV", lastCreatedAt, lastId, size);
    }

    //QnA 목록 조회
    //251007
    @GetMapping("/qna")
    public ResponseEntity<ResponseDto<PageResult<Qna>>> getQnaListOfPJ(@PathVariable("projectId") Long projectId, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 10
        );

        return communityService.getQnaListOfPJ(projectId, pager);
    }

    //QnA 질문 작성
    //251008
    @PostMapping("/qna/{userId}/add")
    public ResponseEntity<ResponseDto<String>> addQuestion(@PathVariable Long userId, @RequestBody QnaAddRequestDto qnaDto){
        return communityService.addQuestion(userId, qnaDto);
    }
}
