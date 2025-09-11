package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.analytic.CategorySuccess;
import com.example.funding.dto.response.admin.analytic.Kpi;
import com.example.funding.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

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
        ZoneId KST = ZoneId.of("Asia/Seoul");
        YearMonth now = YearMonth.now(KST);

        int monthsInt = monthsInt(period);

        Date from = null, to = null;
        if (monthsInt > 0) {
            YearMonth startYm = now.minusMonths(monthsInt - 1);
            from = Date.from(startYm.atDay(1).atStartOfDay(KST).toInstant());
            to = Date.from(now.plusMonths(1).atDay(1).atStartOfDay(KST).toInstant());
        }

        if (!"revenue".equals(metric)) metric = "qty";
        return adminService.getAdminAnalytics(from, to, limit, metric, monthsInt, ctgrId);
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

    private static int monthsInt(String period) {
        return switch (period) {
            case "1m" -> 1;
            case "3m" -> 3;
            case "6m" -> 6;
            case "1y" -> 12;
            case "all" -> 0;
            default -> 6;
        };
    }
}
