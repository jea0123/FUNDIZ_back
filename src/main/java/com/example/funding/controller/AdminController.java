package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.common.Utils;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.project.ProjectUpdateRequestDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.ReviewDetailDto;
import com.example.funding.dto.response.admin.SearchReviewDto;
import com.example.funding.dto.response.admin.analytic.CategorySuccess;
import com.example.funding.dto.response.admin.analytic.Kpi;
import com.example.funding.dto.response.admin.analytic.RewardSalesTop;
import com.example.funding.dto.row.ProjectRow;
import com.example.funding.dto.row.ReviewListRow;
import com.example.funding.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;

import static com.example.funding.common.Utils.monthsInt;
import static com.example.funding.common.Utils.resolveWindow;

@RestController
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final AdminService adminService;

    /**
     * 관리자 대시보드 분석 데이터 조회
     * @param period 조회 기간 (1m, 3m, 6m, 1y, all)
     * @param metric 정렬 기준 (qty: 판매 수량, revenue: 매출)
     * @param limit  상위 N개 리워드 조회 제한
     * @param ctgrId 카테고리 ID
     * @return 관리자 대시보드 분석 데이터
     * @author 장민규
     * @since 2025-09-11
     */
    @GetMapping("/analytics")
    public ResponseEntity<ResponseDto<AdminAnalyticsDto>> getAdminAnalytics(@RequestParam(defaultValue = "6m") String period, @RequestParam(defaultValue = "qty") String metric,
                                                                            @RequestParam(defaultValue = "5") int limit, @RequestParam(defaultValue = "1") Long ctgrId
    ) {
        Utils.AnalyticsWindow w = resolveWindow(period, metric, KST);
        return adminService.getAdminAnalytics(w.getFrom(), w.getTo(), limit, metric, w.getMonths(), ctgrId);
    }

    /**
     * 특정 카테고리의 성공률 분석 데이터 조회
     * @param ctgrId 카테고리 ID
     * @return 특정 카테고리의 성공률 분석 데이터
     * @author 장민규
     * @since 2025-09-11
     */
    @GetMapping("/category-success")
    public ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(@RequestParam Long ctgrId) {
        return adminService.getCategorySuccessByCategory(ctgrId);
    }

    /**
     * 주요 성과 지표(KPI) 조회
     * @param period 조회 기간 (1m, 3m, 6m, 1y, all)
     * @return 주요 성과 지표(KPI) 데이터
     * @author 장민규
     * @since 2025-09-11
     */
    @GetMapping("/kpi")
    public ResponseEntity<ResponseDto<Kpi>> getKpi(@RequestParam(defaultValue = "6m") String period) {
        int months = monthsInt(period);
        return adminService.getKpi(months);
    }

    /**
     * 상위 리워드 판매량/매출 조회
     * @param period 조회 기간 (1m, 3m, 6m, 1y, all)
     * @param metric 정렬 기준 (qty: 판매 수량, revenue: 매출)
     * @param limit  상위 N개 리워드 조회 제한
     * @return 상위 리워드 판매량/매출 데이터 리스트
     * @author 장민규
     * @since 2025-09-11
     */
    @GetMapping("/reward-sales-top")
    public ResponseEntity<ResponseDto<List<RewardSalesTop>>> getRewardSalesTops(@RequestParam (defaultValue = "6m") String period, @RequestParam(defaultValue = "qty") String metric,
                                                                                @RequestParam(defaultValue = "5") int limit) {
        Utils.AnalyticsWindow w = resolveWindow(period, metric, KST);
        return adminService.getRewardSalesTops(w.getFrom(), w.getTo(), limit, metric);
    }

    /**
     * <p>프로젝트 취소</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-17
     */
    @PostMapping("/project/{projectId}/cancel")
    public ResponseEntity<ResponseDto<String>> cancelProject(@PathVariable Long projectId) {
        //관리자 체크
        Long adId = 1L;

        return adminService.cancelProject(projectId, adId);
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto ProjectUpdateRequestDto
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-17
     */
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ResponseDto<String>> updateProject(@PathVariable Long projectId, @RequestBody ProjectUpdateRequestDto dto) {
        //관리자 체크
        Long adId = 1L;

        dto.setProjectId(projectId);

        return adminService.updateProject(dto);
    }

    /**
     * <p>프로젝트 심사 목록 조회</p>
     *
     * @param dto SearchReviewDto
     * @param reqPager 요청 pager
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-18
     */
    @GetMapping("/review")
    public ResponseEntity<ResponseDto<PageResult<ReviewListRow>>> getReviewList(SearchReviewDto dto, Pager reqPager) {
        return adminService.getReviewList(dto, reqPager);
    }

    /**
     * <p>프로젝트 심사 상세 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-19
     */
    @GetMapping("/review/{projectId}")
    public ResponseEntity<ResponseDto<ReviewDetailDto>> getReviewDetail(@PathVariable Long projectId) {
        return adminService.getReviewDetail(projectId);
    }
}
