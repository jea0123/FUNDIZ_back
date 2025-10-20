package com.example.funding.service;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.admin.AdminProjectUpdateDto;
import com.example.funding.dto.request.admin.SearchAdminProjectDto;
import com.example.funding.dto.request.admin.UserAdminUpdateRequestDto;
import com.example.funding.dto.request.cs.NoticeAddRequestDto;
import com.example.funding.dto.request.cs.NoticeUpdateRequestDto;
import com.example.funding.dto.request.cs.ReportUpdateRequestDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.AdminProjectListDto;
import com.example.funding.dto.response.admin.ProjectVerifyDetailDto;
import com.example.funding.dto.response.admin.ProjectVerifyListDto;
import com.example.funding.dto.response.admin.analytic.Kpi;
import com.example.funding.dto.response.admin.analytic.RewardSalesTop;
import com.example.funding.exception.notfound.AnalyticsNotFoundException;
import com.example.funding.exception.notfound.KPINotFoundException;
import com.example.funding.exception.notfound.RewardSalesNotFoundException;
import com.example.funding.model.User;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Validated
public interface AdminService {
    /**
     * 관리자 대시보드 분석 데이터 조회
     *
     * @param from   조회 시작 날짜
     * @param to     조회 종료 날짜
     * @param limit  상위 N개 리워드 조회 제한
     * @param metric 정렬 기준 (qty: 판매 수량, revenue: 매출)
     * @param month  조회 기간 (개월 단위)
     * @param ctgrId 카테고리 ID
     * @return 관리자 대시보드 분석 데이터
     * @throws AnalyticsNotFoundException 데이터가 존재하지 않을 경우
     * @author 장민규
     * @since 2025-09-11
     */
    ResponseEntity<ResponseDto<AdminAnalyticsDto>> getAdminAnalytics(
            @NotNull(message = "시작일(from) 날짜는 필수입니다. 현재: ${validatedValue}")
            @PastOrPresent(message = "시작일(from) 날짜는 오늘 이전이거나 같아야 합니다. 현재: ${validatedValue}")
            LocalDate from,
            @NotNull(message = "종료일(to) 날짜는 필수입니다. 현재: ${validatedValue}")
            @FutureOrPresent(message = "종료일(to) 날짜는 오늘 이후이거나 같아야 합니다. 현재: ${validatedValue}")
            LocalDate to,
            @NotNull(message = "limit는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "limit는 양수여야 합니다. 현재: ${validatedValue}")
            Integer limit,
            @NotBlank(message = "metric는 필수입니다. 현재: '${validatedValue}'")
            String metric,
            @NotNull(message = "month는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "month는 양수여야 합니다. 현재: ${validatedValue}")
            @Min(value = 1, message = "month는 최소 {value} 이상이어야 합니다. 현재값: ${validatedValue}")
            @Max(value = 12, message = "month는 최대 {value} 이하이어야 합니다. 현재값: ${validatedValue}")
            Integer month,
            @NotNull(message = "ctgrId는 필수입니다. 현재: ${validatedValue}")
            @Positive(message = "ctgrId는 양수여야 합니다. 현재: ${validatedValue}")
            Long ctgrId
    );

    /**
     * KPI 조회
     *
     * @param month 조회 기간 (개월 단위)
     * @return KPI 데이터
     * @throws KPINotFoundException 데이터가 존재하지 않을 경우
     * @author 장민규
     * @since 2025-09-11
     */
    ResponseEntity<ResponseDto<Kpi>> getKpi(int month);

    /**
     * 상위 리워드 판매량/매출 조회
     *
     * @param from   조회 시작 날짜
     * @param to     조회 종료 날짜
     * @param limit  상위 N개 리워드 조회 제한
     * @param metric 정렬 기준 (qty: 판매 수량, revenue: 매출)
     * @return 상위 리워드 판매량/매출 데이터 리스트
     * @throws RewardSalesNotFoundException 데이터가 존재하지 않을 경우
     * @author 장민규
     * @since 2025-09-11
     */
    ResponseEntity<ResponseDto<List<RewardSalesTop>>> getRewardSalesTops(LocalDate from, LocalDate to, int limit, String metric);

    ResponseEntity<ResponseDto<PageResult<AdminProjectListDto>>> getProjectList(SearchAdminProjectDto dto, Pager pager);

    ResponseEntity<ResponseDto<String>> cancelProject(Long projectId);

    ResponseEntity<ResponseDto<String>> updateProject(AdminProjectUpdateDto dto);

    ResponseEntity<ResponseDto<PageResult<ProjectVerifyListDto>>> getProjectVerifyList(SearchAdminProjectDto dto, Pager pager);

    ResponseEntity<ResponseDto<ProjectVerifyDetailDto>> getProjectVerifyDetail(Long projectId);

    ResponseEntity<ResponseDto<String>> approveProject(Long projectId);

    ResponseEntity<ResponseDto<String>> rejectProject(Long projectId, String rejectedReason);

    ResponseEntity<ResponseDto<PageResult<User>>> userList(Pager pager);

    ResponseEntity<ResponseDto<String>> updateUser(Long userId, UserAdminUpdateRequestDto userDto);

    ResponseEntity<ResponseDto<User>> item(Long userId);

    ResponseEntity<ResponseDto<String>> addNotice(NoticeAddRequestDto ntcDto);

    ResponseEntity<ResponseDto<String>> updateNotice(Long noticeId, NoticeUpdateRequestDto ntcDto);

    ResponseEntity<ResponseDto<String>> deleteNotice(Long noticeId);

    ResponseEntity<ResponseDto<String>> updateReportStatus(Long reportId, ReportUpdateRequestDto dto);
}
