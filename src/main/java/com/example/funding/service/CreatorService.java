package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.*;
import com.example.funding.dto.request.shipping.ShippingStatusDto;
import com.example.funding.dto.response.backing.BackingCreatorProjectListDto;
import com.example.funding.dto.response.creator.*;
import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.dto.response.shipping.CreatorShippingProjectList;
import com.example.funding.exception.badrequest.AlreadyCreatorException;
import com.example.funding.exception.notfound.CreatorNotFoundException;
import com.example.funding.exception.notfound.UserNotFoundException;
import com.example.funding.model.Creator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    ResponseEntity<ResponseDto<CreatorSummaryDto>> getCreatorSummary(Long creatorId, Long userId);

    ResponseEntity<ResponseDto<PageResult<CreatorProjectDto>>> getCreatorProject(Long creatorId, String sort, Pager pager);
}
