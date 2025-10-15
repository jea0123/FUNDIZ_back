package com.example.funding.controller;

import com.example.funding.common.FileUploader;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.CreatorRegisterRequestDto;
import com.example.funding.dto.request.creator.NewsCreateRequestDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.dto.response.backing.BackingCreatorProjectListDto;
import com.example.funding.dto.response.creator.*;
import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.dto.response.shipping.CreatorShippingProjectList;
import com.example.funding.enums.CreatorType;
import com.example.funding.exception.badrequest.AlreadyCreatorException;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.exception.notfound.UserNotFoundException;
import com.example.funding.model.Reward;
import com.example.funding.service.CreatorService;
import com.example.funding.service.NewsService;
import com.example.funding.service.RewardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/creator")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService creatorService;
    private final RewardService rewardService;
    private final NewsService newsService;
    private final FileUploader fileUploader;

    /**
     * <p>크리에이터 등록</p>
     *
     * @param dto       크리에이터 등록 요청 DTO
     * @param principal 인증된 사용자 정보
     * @return 크리에이터 이름
     * @throws UserNotFoundException 유저를 찾을 수 없는 경우(404)
     * @throws AlreadyCreatorException 이미 크리에이터로 등록된 유저인 경우(400)
     * @author 장민규
     * @since 2025-10-12
     */
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<String>> registerCreator(@ModelAttribute CreatorRegisterRequestDto dto
//                                                               @AuthenticationPrincipal CustomUserPrincipal principal
    ) throws IOException {
        MultipartFile file = dto.getProfileImg();
        if (file != null && file.isEmpty()) {
            file = null;
        }
        CreatorType type = dto.getCreatorType() != null ? dto.getCreatorType() : CreatorType.GENERAL;
        dto.setProfileImg(file);
        dto.setCreatorType(type);
//        Long userId = principal.userId();
        Long userId = 400L; // TODO: 임시
        return creatorService.registerCreator(dto, userId);
    }

    /**
     * <p>프로젝트 목록 조회</p>
     *
     * @param creatorId 창작자 ID
     * @param dto       SearchCreatorProjectDto
     * @param reqPager  요청 pager
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
    public ResponseEntity<ResponseDto<String>> createProject(@ModelAttribute ProjectCreateRequestDto dto,
                                                             @RequestAttribute Long creatorId) throws IOException {
        //TODO: userId -> creatorId
        String thumbnailUrl = fileUploader.upload(dto.getThumbnail());
        dto.setThumbnailUrl(thumbnailUrl);
        return creatorService.createProject(dto, creatorId);
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto       ProjectCreateRequestDto
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-16
     */
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ResponseDto<String>> updateProject(@PathVariable Long projectId,
                                                             @RequestAttribute Long creatorId,
                                                             @RequestBody ProjectCreateRequestDto dto) throws IOException {
        //TODO: userId -> creatorId

        if (dto.getProjectId() != null && !dto.getProjectId().equals(projectId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 프로젝트 ID 입니다.");
        }
        dto.setProjectId(projectId);
        String thumbnailUrl = fileUploader.upload(dto.getThumbnail());
        dto.setThumbnailUrl(thumbnailUrl);

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

    /**
     * <p>프로젝트 요약</p>
     *
     * @param projectId 프로젝트 ID
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-08
     */
    @GetMapping("/projects/{projectId}/summary")
    public ResponseEntity<ResponseDto<CreatorProjectSummaryDto>> getProjectSummary(@PathVariable Long projectId,
                                                                                   @RequestAttribute Long creatorId) {
        return creatorService.getProjectSummary(projectId, creatorId);
    }

    /**
     * <p>리워드 목록 조회</p>
     *
     * @param projectId 프로젝트 Id
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-08
     */
    @GetMapping("/projects/{projectId}/reward")
    public ResponseEntity<ResponseDto<List<Reward>>> getCreatorRewardList(@PathVariable Long projectId,
                                                                          @RequestAttribute Long creatorId) {
        return rewardService.getCreatorRewardList(projectId, creatorId);
    }

    /**
     * <p>리워드 단건 추가</p>
     *
     * @param projectId 프로젝트 Id
     * @param creatorId 창작자 ID
     * @param dto       RewardCreateRequestDto
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-08
     */
    @PostMapping("/projects/{projectId}/reward")
    public ResponseEntity<ResponseDto<String>> addReward(@PathVariable Long projectId,
                                                         @RequestAttribute Long creatorId,
                                                         @RequestBody RewardCreateRequestDto dto) {
        dto.setProjectId(projectId);

        return rewardService.addReward(projectId, creatorId, dto);
    }

    /**
     * <p>창작자 프로필 요약 조회</p>
     *
     * <li>창작자명</li>
     * <li>사업자번호</li>
     * <li>이메일</li>
     * <li>전화번호</li>
     * <li>정지 여부</li>
     *
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-11
     */
    @GetMapping("/summary")
    public ResponseEntity<ResponseDto<CreatorProfileSummaryDto>> getCreatorProfileSummary(@RequestAttribute Long creatorId) {
        return creatorService.getCreatorProfileSummary(creatorId);
    }

    /**
     * <p>QnA 내역 목록 조회(창작자 기준)</p>
     *
     * @param creatorId 창작자 ID
     * @param reqPager  Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-08
     */
    @GetMapping("/qna")
    public ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfCreator(@RequestAttribute("creatorId") Long creatorId, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        return creatorService.getQnaListOfCreator(creatorId, pager);
    }

    /**
     * <p>창작자의 대시보드</p>
     *
     * @param creatorId
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author by: 이윤기
     * @since 2025-10-02
     */

    @GetMapping("/dashBoard")
    public ResponseEntity<ResponseDto<CreatorDashboardDto>> getCreatorDashBoard(@RequestAttribute Long creatorId) {
        return creatorService.getCreatorDashBoard(creatorId);
    }

    @GetMapping("/backingList")
    public ResponseEntity<ResponseDto<List<BackingCreatorProjectListDto>>> getBackingList(@RequestAttribute Long creatorId) {
        return creatorService.getCreatorProjectList(creatorId);
    }

    @GetMapping("/shippingList")
    public ResponseEntity<ResponseDto<List<CreatorShippingProjectList>>> getShippingList(@RequestAttribute Long creatorId) {
        return creatorService.getCreatorShippingList(creatorId);
    }

    @GetMapping("/shippingBackerList/{projectId}")
    public ResponseEntity<ResponseDto<List<CreatorShippingBackerList>>> getShippingBackerList(@RequestAttribute Long creatorId, @PathVariable Long projectId) {
        return creatorService.getShippingBackerList(creatorId, projectId);
    }
    
    // TODO: 이윤기 - 창작자 배송 리스트에서 배송상태 변경을 위한 @PostMapping 필요

    /**
     * <p>프로젝트 새소식 등록</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto       NewsCreateRequestDto
     * @param creatorId 창작자 ID
     * @return 성공 시 200 Ok
     * @author 조은애
     * @since 2025-10-11
     */
    @PostMapping("/projects/{projectId}/news")
    public ResponseEntity<ResponseDto<String>> createNews(@PathVariable Long projectId,
                                                          @RequestBody NewsCreateRequestDto dto,
                                                          @RequestAttribute Long creatorId) {
        return newsService.createNews(projectId, creatorId, dto);
    }

    /**
     * <p>크리에이터 팔로워 수 조회</p>
     *
     * @param creatorId 크리에이터 ID
     * @return 팔로워 수
     * @throws CreatorNotFoundException 크리에이터를 찾을 수 없는 경우(404)
     * @author 장민규
     * @since 2025-10-15
     */
    @GetMapping("/followerCnt/{creatorId}")
    public ResponseEntity<ResponseDto<Long>> getFollowerCnt(@PathVariable Long creatorId) {
        return creatorService.getFollowerCnt(creatorId);
    }
}
