package com.example.funding.service;

import com.example.funding.common.CursorPage;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.CreatorRegisterRequestDto;
import com.example.funding.dto.request.creator.CreatorUpdateRequestDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.request.shipping.ShippingStatusDto;
import com.example.funding.dto.response.backing.BackingCreatorProjectListDto;
import com.example.funding.dto.response.creator.*;
import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.dto.response.shipping.CreatorShippingProjectList;
import com.example.funding.exception.badrequest.AlreadyCreatorException;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.exception.notfound.UserNotFoundException;
import com.example.funding.model.Creator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Validated
public interface CreatorService {

    ResponseEntity<ResponseDto<PageResult<CreatorProjectListDto>>> getProjectList(Long creatorId, SearchCreatorProjectDto dto, Pager pager);

    ResponseEntity<ResponseDto<CreatorProjectDetailDto>> getProjectDetail(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<Long>> createProject(ProjectCreateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> updateProject(ProjectCreateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> deleteProject(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<String>> verifyProject(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<PageResult<CreatorQnaDto>>> getQnaListOfCreator(Long creatorId, Pager pager);

    ResponseEntity<ResponseDto<CreatorProjectSummaryDto>> getProjectSummary(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<CreatorProfileSummaryDto>> getCreatorProfileSummary(Long creatorId);

    ResponseEntity<ResponseDto<CreatorDashboardDto>> getCreatorDashBoard(Long creatorId);

    ResponseEntity<ResponseDto<List<BackingCreatorProjectListDto>>> getCreatorProjectList(Long creatorId);

    ResponseEntity<ResponseDto<List<CreatorShippingProjectList>>> getCreatorShippingList(Long creatorId);

    ResponseEntity<ResponseDto<List<CreatorShippingBackerList>>> getShippingBackerList(Long creatorId, Long projectId);

    /**
     * 크리에이터 등록
     * @param dto 크리에이터 등록 요청 DTO
     * @param userId 유저 ID
     * @return 크리에이터 이름
     * @throws UserNotFoundException 유저를 찾을 수 없는 경우
     * @throws AlreadyCreatorException 이미 크리에이터로 등록된 유저인 경우
     * @since 2025-10-12
     */
    ResponseEntity<ResponseDto<String>> registerCreator(CreatorRegisterRequestDto dto, Long userId) throws IOException;

    ResponseEntity<ResponseDto<String>> updateCreatorInfo(Long creatorId, CreatorUpdateRequestDto dto);

    ResponseEntity<ResponseDto<Creator>> item(Long creatorId);

    ResponseEntity<ResponseDto<String>> setShippingStatus(Long projectId, Long creatorId, ShippingStatusDto status);
    /**
     * 크리에이터 팔로워 수 조회
     * @param creatorId 크리에이터 ID
     * @return 팔로워 수
     * @throws CreatorNotFoundException 크리에이터를 찾을 수 없는 경우
     * @since 2025-10-15
     * @author 장민규
     */
    ResponseEntity<ResponseDto<Long>> getFollowerCnt(Long creatorId);

    /**
     * 크리에이터 요약 정보 조회
     * @param creatorId 크리에이터 ID
     * @param userId 유저 ID (팔로우 여부 확인용)
     * @return 크리에이터 요약 정보
     * @since 2025-10-19
     * @author 장민규
     */
    ResponseEntity<ResponseDto<CreatorSummaryDto>> getCreatorSummary(@NotNull(message = "크리에이터 ID는 필수입니다.")
                                                                     @Positive(message = "크리에이터 ID는 양수여야 합니다.")
                                                                     Long creatorId, Long userId);

    /**
     * 크리에이터의 프로젝트 목록 조회 (페이징 및 정렬)
     * @param creatorId 크리에이터 ID
     * @param sort 정렬 기준
     * @param pager 페이저 정보 (페이지 번호, 페이지 크기 등)
     * @return 페이징된 크리에이터 프로젝트 목록
     * @since 2025-10-19
     * @author 장민규
     */
    ResponseEntity<ResponseDto<PageResult<CreatorProjectDto>>> getCreatorProject(@NotNull(message = "크리에이터 ID는 필수입니다.")
                                                                                 @Positive(message = "크리에이터 ID는 양수여야 합니다.")
                                                                                 Long creatorId,
                                                                                 @NotBlank(message = "정렬 기준은 필수입니다.")
                                                                                 String sort, Pager pager);

    /**
     * 크리에이터 리뷰 목록 조회 (커서 기반 페이징)
     * @param creatorId 크리에이터 ID
     * @param lastCreatedAt 마지막으로 조회된 리뷰의 생성일시 (커서)
     * @param lastId 마지막으로 조회된 리뷰의 ID (커서)
     * @param size 조회할 리뷰 개수
     * @param projectId (선택 사항) 특정 프로젝트에 대한 리뷰만 조회
     * @param photoOnly (선택 사항) 사진이 포함된 리뷰만 조회 여부
     * @return 커서 기반 페이징된 크리에이터 리뷰 목록
     * @since 2025-10-19
     * @author 장민규
     */
    ResponseEntity<ResponseDto<CursorPage<ReviewListDto>>> getCreatorReviews(@NotNull(message = "크리에이터 ID는 필수입니다.")
                                                                             @Positive(message = "크리에이터 ID는 양수여야 합니다.")
                                                                             Long creatorId, LocalDateTime lastCreatedAt,
                                                                             @Positive(message = "마지막 ID는 양수여야 합니다.") Long lastId,
                                                                             @Positive(message = "조회 개수는 양수여야 합니다.") int size,
                                                                             @Positive(message = "프로젝트 ID는 양수여야 합니다.")
                                                                             Long projectId, Boolean photoOnly);
}
