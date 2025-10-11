package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.creator.ProjectCreateRequestDto;
import com.example.funding.dto.request.creator.SearchCreatorProjectDto;
import com.example.funding.dto.response.backing.BackingCreatorProjectListDto;
import com.example.funding.dto.response.creator.CreatorDashboardDto;

import com.example.funding.dto.response.creator.CreatorProjectDetailDto;
import com.example.funding.dto.response.creator.CreatorProjectListDto;
import com.example.funding.dto.response.shipping.CreatorShippingBackerList;
import com.example.funding.dto.response.shipping.CreatorShippingProjectList;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CreatorService {

    ResponseEntity<ResponseDto<CreatorDashboardDto>> getCreatorDashBoard(Long creatorId);

    ResponseEntity<ResponseDto<PageResult<CreatorProjectListDto>>> getProjectList(Long creatorId, SearchCreatorProjectDto dto, Pager pager);

    ResponseEntity<ResponseDto<CreatorProjectDetailDto>> getProjectDetail(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<String>> createProject(ProjectCreateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> updateProject(ProjectCreateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> deleteProject(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<String>> verifyProject(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<List<BackingCreatorProjectListDto>>> getCreatorProjectList(Long creatorId);

    ResponseEntity<ResponseDto<List<CreatorShippingProjectList>>> getCreatorShippingList(Long creatorId);

    ResponseEntity<ResponseDto<List<CreatorShippingBackerList>>> getShippingBackerList(Long creatorId, Long projectId);
}
