package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.response.creator.CreatorPDetailDto;
import com.example.funding.dto.response.creator.CreatorProjectDetailDto;
import com.example.funding.dto.response.creator.CreatorProjectListDto;
import com.example.funding.dto.response.creator.CreatorProjectSummaryDto;
import org.springframework.http.ResponseEntity;

public interface CreatorService {
//    ResponseEntity<ResponseDto<List<CreatorPListDto>>> getCreatorPList(Long creatorId);
//
//    ResponseEntity<ResponseDto<CreatorPDetailDto>> getCreatorDList(Long creatorId, Long projectId);

    ResponseEntity<ResponseDto<CreatorPDetailDto>> getCreatorDashBoard(Long creatorId);

    ResponseEntity<ResponseDto<PageResult<CreatorProjectListDto>>> getProjectList(Long creatorId, SearchCreatorProjectDto dto, Pager pager);

    ResponseEntity<ResponseDto<CreatorProjectDetailDto>> getProjectDetail(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<String>> createProject(ProjectCreateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> updateProject(ProjectCreateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> deleteProject(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<String>> verifyProject(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<CreatorProjectSummaryDto>> getProjectSummary(Long projectId, Long creatorId);
}
