package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.ProjectUpdateRequestDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.SearchReviewDto;
import com.example.funding.dto.response.admin.analytic.CategorySuccess;
import com.example.funding.dto.response.admin.analytic.Kpi;
import com.example.funding.dto.response.admin.analytic.RewardSalesTop;
import com.example.funding.dto.row.ReviewListRow;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    ResponseEntity<ResponseDto<AdminAnalyticsDto>> getAdminAnalytics(LocalDate from, LocalDate to, int limit, String metric, int month, Long ctgrId);

    ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(Long ctgrId);

    ResponseEntity<ResponseDto<Kpi>> getKpi(int month);

    ResponseEntity<ResponseDto<List<RewardSalesTop>>> getRewardSalesTops(LocalDate from, LocalDate to, int limit, String metric);

    ResponseEntity<ResponseDto<String>> cancelProject(Long projectId, Long adId);

    ResponseEntity<ResponseDto<String>> updateProject(ProjectUpdateRequestDto dto);

    ResponseEntity<ResponseDto<PageResult<ReviewListRow>>> getReviewList(SearchReviewDto dto, Pager reqPager);
}
