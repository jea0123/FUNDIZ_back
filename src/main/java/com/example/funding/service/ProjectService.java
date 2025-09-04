package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.project.FeaturedProjectDto;
import com.example.funding.dto.response.project.ProjectDetailDto;
import com.example.funding.dto.response.project.RecentTop10ProjectDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {

    ResponseEntity<ResponseDto<ProjectDetailDto>> getProjectDetail(Long projectId);

    ResponseEntity<ResponseDto<List<RecentTop10ProjectDto>>> getRecentTop10();

    ResponseEntity<ResponseDto<List<FeaturedProjectDto>>> getFeatured(int days, int limit);
}
