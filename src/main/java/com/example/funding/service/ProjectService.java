package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.ProjectCreateRequestDto;
import com.example.funding.dto.request.project.ProjectUpdateRequestDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import com.example.funding.dto.response.project.SearchProjectDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {

    ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(Long projectId);

    ResponseEntity<ResponseDto<List<RecentTop10ProjectDto>>> getRecentTop10();

    ResponseEntity<ResponseDto<List<FeaturedProjectDto>>> getFeatured(int days, int limit);

    ResponseEntity<ResponseDto<String>> createProject(ProjectCreateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<PageResult<FeaturedProjectDto>>> search(SearchProjectDto dto, Pager reqPager);

    ResponseEntity<ResponseDto<String>> updateProject(ProjectUpdateRequestDto dto, Long creatorId);

    ResponseEntity<ResponseDto<String>> deleteByCreator(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<String>> requestReview(Long projectId, Long creatorId);
}
