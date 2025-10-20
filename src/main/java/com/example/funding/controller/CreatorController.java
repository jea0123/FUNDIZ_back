package com.example.funding.controller;

import com.example.funding.common.*;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.PagerRequest;
import com.example.funding.dto.request.creator.*;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.dto.request.shipping.ShippingStatusDto;
import com.example.funding.dto.response.backing.BackingCreatorProjectListDto;
import com.example.funding.dto.response.creator.*;
import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.dto.response.shipping.CreatorShippingProjectList;
import com.example.funding.enums.CreatorType;
import com.example.funding.exception.badrequest.AlreadyCreatorException;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.exception.notfound.UserNotFoundException;
import com.example.funding.model.Creator;
import com.example.funding.model.Reward;
import com.example.funding.service.CreatorService;
import com.example.funding.service.NewsService;
import com.example.funding.service.RewardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
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
     * @throws UserNotFoundException   유저를 찾을 수 없는 경우(404)
     * @throws AlreadyCreatorException 이미 크리에이터로 등록된 유저인 경우(400)
     * @author 장민규
     * @since 2025-10-12
     */
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<String>> registerCreator(@Valid @ModelAttribute CreatorRegisterRequestDto dto
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
     * <p>크리에이터 상세 정보 조회</p>
     *
     * @param creatorId 창작자 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-15
     */
    @GetMapping("/info")
    public ResponseEntity<ResponseDto<Creator>> item(@RequestAttribute Long creatorId) {
        creatorId = 1L;
        return creatorService.item(creatorId);
    }

    /**
     * <p>크리에이터 정보 수정</p>
     *
     * @param creatorId  창작자 ID
     * @param dto        CreatorUpdateRequestDto
     * @param profileImg 프로필 이미지
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-15
     */
    @PostMapping(value = "/update/{creatorId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto<String>> updateCreatorInfo(@RequestAttribute Long creatorId,
                                                                 @ModelAttribute CreatorUpdateRequestDto dto,
                                                                 @RequestParam(required = false) MultipartFile profileImg) throws IOException {
        creatorId = 1L;

        if (profileImg != null && !profileImg.isEmpty()) {
            // 새로운 프로필 이미지가 있다면 업로드 처리
            dto.setProfileImg(profileImg);
            String profileImgUrl = fileUploader.upload(dto.getProfileImg());

            if (profileImgUrl != null && !profileImgUrl.isEmpty()) {
                dto.setProfileImgUrl(profileImgUrl);
            } else {
                dto.setProfileImgUrl(dto.getProfileImgUrl());
            }
        } else {
            dto.setProfileImgUrl(dto.getProfileImgUrl());
        }

        return creatorService.updateCreatorInfo(creatorId, dto);
    }

    /**
     * <p>프로젝트 목록 조회</p>
     *
     * @param creatorId 창작자 ID
     * @param dto       SearchCreatorProjectDto
     * @param req       요청 pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-05
     */
    @GetMapping("/projects")
    public ResponseEntity<ResponseDto<PageResult<CreatorProjectListDto>>> getProjectList(@RequestAttribute Long creatorId,
                                                                                         SearchCreatorProjectDto dto,
                                                                                         @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
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
    public ResponseEntity<ResponseDto<Long>> createProject(ProjectCreateRequestDto dto,
                                                           @RequestAttribute Long creatorId) throws IOException {

        String thumbnailUrl = null;
        if (dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()) {
            thumbnailUrl = fileUploader.upload(dto.getThumbnail());
        }
        String businessDocUrl = null;
        if (dto.getBusinessDoc() != null && !dto.getBusinessDoc().isEmpty()) {
            businessDocUrl = fileUploader.upload(dto.getBusinessDoc());
        }
        dto.setThumbnailUrl(thumbnailUrl);
        dto.setBusinessDocUrl(businessDocUrl);

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
                                                             ProjectCreateRequestDto dto,
                                                             @RequestAttribute Long creatorId) throws IOException {
        if (dto.getProjectId() != null && !dto.getProjectId().equals(projectId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 프로젝트 ID 입니다.");
        }
        dto.setProjectId(projectId);

        String thumbnailUrl = null;
        if (dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()) {
            thumbnailUrl = fileUploader.upload(dto.getThumbnail());
        }
        String businessDocUrl = null;
        if (dto.getBusinessDoc() != null && !dto.getBusinessDoc().isEmpty()) {
            businessDocUrl = fileUploader.upload(dto.getBusinessDoc());
        }
        dto.setThumbnailUrl(thumbnailUrl);
        dto.setBusinessDocUrl(businessDocUrl);

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
    public ResponseEntity<ResponseDto<List<Reward>>> getRewardListManage(@PathVariable Long projectId,
                                                                         @RequestAttribute Long creatorId) {
        return rewardService.getRewardListManage(projectId, creatorId);
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
     *
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
     * @param req       Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-08
     */
    @GetMapping("/qna")
    public ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfCreator(@RequestAttribute("creatorId") Long creatorId, @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
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

    @PostMapping("/shippingBackerList/{projectId}")
    public ResponseEntity<ResponseDto<String>> setShippingStatus(@PathVariable Long projectId,
                                                                 @RequestAttribute Long creatorId,
                                                                 @Valid @RequestBody ShippingStatusDto status) {
        return creatorService.setShippingStatus(projectId, creatorId, status);
    }

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

    /**
     * <p>크리에이터 요약 정보 조회</p>
     *
     * @param creatorId 크리에이터 ID
     * @param principal 인증된 사용자 정보
     * @return 크리에이터 요약 정보
     * @author 장민규
     * @since 2025-10-19
     */
    @GetMapping("/summary/{creatorId}")
    public ResponseEntity<ResponseDto<CreatorSummaryDto>> getCreatorSummary(@NotNull @Positive @PathVariable Long creatorId,
                                                                            @AuthenticationPrincipal CustomUserPrincipal principal) {
        return creatorService.getCreatorSummary(creatorId, 10L);
    }

    /**
     * <p>크리에이터의 프로젝트 목록 조회 (페이징 및 정렬)</p>
     *
     * @param creatorId 크리에이터 ID
     * @param sort      정렬 기준 (recent, popular, endingSoon 등)
     * @param req       페이저 정보 (페이지 번호, 페이지 크기 등)
     * @return 페이징된 크리에이터 프로젝트 목록
     * @author 장민규
     * @since 2025-10-19
     */
    @GetMapping("/projectsList/{creatorId}")
    public ResponseEntity<ResponseDto<PageResult<CreatorProjectDto>>> getCreatorProject(@NotNull @Positive @PathVariable Long creatorId,
                                                                                        @NotNull @RequestParam(required = false, defaultValue = "recent") String sort,
                                                                                        @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), 5);
        return creatorService.getCreatorProject(creatorId, sort, pager);
    }

    /**
     * <p>크리에이터 리뷰 목록 조회 (커서 기반 페이징)</p>
     *
     * @param creatorId     크리에이터 ID
     * @param lastCreatedAt 마지막으로 조회된 리뷰의 생성일시 (커서)
     * @param lastId        마지막으로 조회된 리뷰의 ID (커서)
     * @param size          조회할 리뷰 개수
     * @param projectId     (선택 사항) 특정 프로젝트에 대한 리뷰만 조회
     * @param photoOnly     (선택 사항) 사진이 포함된 리뷰만 조회 여부
     * @return 커서 기반 페이징된 크리에이터 리뷰 목록
     * @author 장민규
     * @since 2025-10-19
     */
    @GetMapping("/reviews/{creatorId}")
    public ResponseEntity<ResponseDto<CursorPage<ReviewListDto>>> getCreatorReviews(@PathVariable Long creatorId,
                                                                                    @Positive @RequestParam(required = false) Long lastId,
                                                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                                    @Positive @RequestParam(required = false) Long projectId,
                                                                                    @RequestParam(required = false, defaultValue = "false") Boolean photoOnly,
                                                                                    @NotNull @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        return creatorService.getCreatorReviews(creatorId, lastCreatedAt, lastId, size, projectId, photoOnly);
    }

    @GetMapping("/followers/{creatorId}")
    public ResponseEntity<ResponseDto<PageResult<CreatorFollowerDto>>> getCreatorFollowers(@PathVariable Long creatorId,
                                                                                           @AuthenticationPrincipal CustomUserPrincipal principal,
                                                                                           @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
        Long loginUserId = principal != null ? principal.userId() : null;
        return creatorService.getCreatorFollowers(creatorId, loginUserId, pager);
    }
}
