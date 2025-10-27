package com.example.funding.controller;

import com.example.funding.common.CursorPage;
import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.PagerRequest;
import com.example.funding.dto.request.project.CommunityCreateRequestDto;
import com.example.funding.dto.request.project.QnaAddRequestDto;
import com.example.funding.dto.request.project.ReplyCreateRequestDto;
import com.example.funding.dto.request.project.SearchProjectDto;
import com.example.funding.dto.response.project.*;
import com.example.funding.exception.notfound.FeaturedProjectNotFoundException;
import com.example.funding.exception.notfound.ProjectNotFoundException;
import com.example.funding.exception.notfound.RecentPaidProjectNotFoundException;
import com.example.funding.service.CommunityService;
import com.example.funding.service.ProjectService;
import com.example.funding.service.QnaService;
import com.example.funding.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final CommunityService communityService;
    private final ReplyService replyService;
    private final QnaService qnaService;

    /**
     * <p>프로젝트 상세 페이지 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 조은애
     * @since 2025-08-31
     */
    @GetMapping("/{projectId:\\d+}")
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
     * @param req 요청 pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<PageResult<FeaturedProjectDto>>> searchProject(SearchProjectDto dto, @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
        return projectService.searchProject(dto, pager);
    }

    @GetMapping("/search/upcoming")
    public ResponseEntity<ResponseDto<PageResult<FeaturedProjectDto>>> searchUpcomingProjects(SearchProjectDto dto, @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
        return projectService.searchUpcomingProjects(dto, pager);
    }

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 목록 조회</p>
     *
     * @param projectId     프로젝트 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 cmId
     * @param size          한 번에 가져올 항목 수
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
     * <p>프로젝트 상세 페이지 - 후기 목록 조회</p>
     *
     * @param projectId     프로젝트 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 cmId
     * @param size          한 번에 가져올 항목 수
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

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 등록</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto       CommunityCreateRequestDto
     * @param principal 사용자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-12
     */
    @PostMapping("/{projectId}/community/new")
    public ResponseEntity<ResponseDto<String>> createCommunity(@PathVariable Long projectId,
                                                               @RequestBody CommunityCreateRequestDto dto,
                                                               @AuthenticationPrincipal CustomUserPrincipal principal) {
        return communityService.createCommunity(projectId, dto, principal.userId());
    }

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 댓글 목록 조회</p>
     *
     * @param cmId          커뮤니티 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 id
     * @param size          한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-12
     */
    @GetMapping("/community/{cmId}/reply")
    public ResponseEntity<ResponseDto<CursorPage<ReplyDto>>> getReplyList(@PathVariable Long cmId,
                                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                          @RequestParam(required = false) Long lastId,
                                                                          @RequestParam(defaultValue = "10") int size) {
        return replyService.getReplyList(cmId, lastCreatedAt, lastId, size);
    }

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티 댓글 등록</p>
     *
     * @param cmId 커뮤니티 ID
     * @param dto  ReplyCreateRequestDto
     * @param principal 사용자 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-13
     */
    @PostMapping("/community/{cmId}/reply")
    public ResponseEntity<ResponseDto<ReplyDto>> createCommunityReply(@PathVariable Long cmId,
                                                                      @RequestBody ReplyCreateRequestDto dto,
                                                                      @AuthenticationPrincipal CustomUserPrincipal principal) {
        return replyService.createCommunityReply(cmId, dto, principal.userId());
    }

    /**
     * <p>프로젝트 상세 페이지 - 좋아요 수 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @throws ProjectNotFoundException 프로젝트를 찾을 수 없을 경우
     * @author 장민규
     * @since 2025-10-15
     */
    @GetMapping("/{projectId}/likeCnt")
    public ResponseEntity<ResponseDto<Long>> getLikeCnt(@PathVariable Long projectId) {
        return projectService.getLikeCnt(projectId);
    }

    /**
     * <p>프로젝트 상세 페이지 - 커뮤니티,후기 수 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-20
     */
    @GetMapping("/{projectId}/counts")
    public ResponseEntity<ResponseDto<ProjectCountsDto>> getCounts(@PathVariable Long projectId) {
        return projectService.getCounts(projectId);
    }

    /**
     * <p>QnA 내역 목록 조회(프로젝트 상세 페이지 기준)(필요X)</p>
     *
     * @param projectId 프로젝트 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId 마지막 항목의 cmId
     * @param size 한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-07
     */
    @GetMapping("/{projectId}/qna")
    public ResponseEntity<ResponseDto<CursorPage<QnaDto>>> getQnaListOfProject(@PathVariable("projectId") Long projectId,
                                                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                               @RequestParam(required = false) Long lastId,
                                                                               @RequestParam(defaultValue = "10") int size) {
        return qnaService.getQnaListOfProject(projectId, lastCreatedAt, lastId, size);
    }

    /**
     * <p>QnA 질문 등록(프로젝트 상세 페이지 내)(필요O)</p>
     *
     * @param projectId 프로젝트 ID
     * @param principal 사용자 ID
     * @param qnaDto QnaAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-08
     */
    @PostMapping("/{projectId}/qna/add")
    public ResponseEntity<ResponseDto<String>> addQuestion(@PathVariable Long projectId, @AuthenticationPrincipal CustomUserPrincipal principal, @RequestBody QnaAddRequestDto qnaDto){
        return qnaService.addQuestion(projectId, principal.userId(), qnaDto);
    }
}
