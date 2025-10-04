package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.ProjectCreateRequestDto;
import com.example.funding.dto.request.project.ProjectUpdateRequestDto;
import com.example.funding.dto.response.creator.CreatorPDetailDto;
import com.example.funding.dto.response.creator.CreatorPListDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CreatorService {
    ResponseEntity<ResponseDto<List<CreatorPListDto>>> getCreatorPList(Long creatorId);

    ResponseEntity<ResponseDto<CreatorPDetailDto>> getCreatorDList(Long creatorId, Long projectId);

    ResponseEntity<ResponseDto<CreatorPDetailDto>> getCreatorDashBoard(Long creatorId);

    ResponseEntity<ResponseDto<String>> createProject(ProjectCreateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> updateProject(ProjectUpdateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> deleteByCreator(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<String>> verifyProject(Long projectId, Long creatorId);
}
