package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.admin.AdminProjectUpdateDto;
import com.example.funding.dto.request.admin.SearchAdminProjectDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.AdminProjectListDto;
import com.example.funding.dto.response.admin.ProjectVerifyDetailDto;
import com.example.funding.dto.response.admin.analytic.CategorySuccess;
import com.example.funding.dto.response.admin.analytic.Kpi;
import com.example.funding.dto.response.admin.analytic.RewardSalesTop;
import com.example.funding.dto.response.admin.ProjectVerifyListDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    ResponseEntity<ResponseDto<AdminAnalyticsDto>> getAdminAnalytics(LocalDate from, LocalDate to, int limit, String metric, int month, Long ctgrId);

    ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(Long ctgrId);

    ResponseEntity<ResponseDto<Kpi>> getKpi(int month);

    ResponseEntity<ResponseDto<List<RewardSalesTop>>> getRewardSalesTops(LocalDate from, LocalDate to, int limit, String metric);

    ResponseEntity<ResponseDto<PageResult<AdminProjectListDto>>> getProjectList(SearchAdminProjectDto dto, Pager pager);

    ResponseEntity<ResponseDto<String>> cancelProject(Long projectId);

    ResponseEntity<ResponseDto<String>> updateProject(AdminProjectUpdateDto dto);

    ResponseEntity<ResponseDto<PageResult<ProjectVerifyListDto>>> getProjectVerifyList(SearchAdminProjectDto dto, Pager pager);

    ResponseEntity<ResponseDto<ProjectVerifyDetailDto>> getProjectVerifyDetail(Long projectId);

    ResponseEntity<ResponseDto<String>> approveProject(Long projectId);

    ResponseEntity<ResponseDto<String>> rejectProject(Long projectId, String rejectedReason);
}
