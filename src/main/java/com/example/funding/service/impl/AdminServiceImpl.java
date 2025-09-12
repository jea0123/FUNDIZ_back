package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.analytic.*;
import com.example.funding.mapper.AdminMapper;
import com.example.funding.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    /**
     * 관리자 대시보드 분석 데이터 조회
     * @param from   조회 시작 날짜
     * @param to     조회 종료 날짜
     * @param limit  상위 N개 리워드 조회 제한
     * @param metric 정렬 기준 (qty: 판매 수량, revenue: 매출)
     * @param months 조회 기간 (개월 단위)
     * @param ctgrId 카테고리 ID
     * @return 관리자 대시보드 분석 데이터
     * @author 장민규
     * @since 2025-09-11
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<AdminAnalyticsDto>> getAdminAnalytics(LocalDate from, LocalDate to, int limit, String metric, int months, Long ctgrId) {
        Kpi kpi = adminMapper.getKpiByMonths(months);
        List<RevenueTrend> revenueTrends = adminMapper.getMonthlyTrends(months);
        List<RewardSalesTop> rewardSalesTops = adminMapper.getRewardSalesTops(from, to, limit, metric);
        List<PaymentMethod> paymentMethods = adminMapper.getPaymentMethods(from, to);
        List<CategorySuccess> categorySuccesses = adminMapper.getCategorySuccessByCategory(ctgrId);

        if(kpi == null || revenueTrends.isEmpty() || rewardSalesTops.isEmpty() || paymentMethods.isEmpty() || categorySuccesses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "해당 기간에 대한 분석 데이터가 없습니다."));
        }

        AdminAnalyticsDto analytics = AdminAnalyticsDto.builder()
                .kpi(kpi)
                .revenueTrends(revenueTrends)
                .rewardSalesTops(rewardSalesTops)
                .paymentMethods(paymentMethods)
                .categorySuccesses(categorySuccesses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "관리자 대시보드 분석 데이터 조회 성공", analytics));
    }

    /**
     * 카테고리별 성공률 조회
     * @param ctgrId 카테고리 ID
     * @return 카테고리별 성공률 데이터 리스트
     * @author 장민규
     * @since 2025-09-11
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(Long ctgrId) {
        List<CategorySuccess> categorySuccesses = adminMapper.getCategorySuccessByCategory(ctgrId);
        if(categorySuccesses.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "해당 카테고리에 대한 데이터가 없습니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "카테고리별 성공률 조회 성공", categorySuccesses));
    }

    /**
     * KPI 조회
     * @param months 조회 기간 (개월 단위)
     * @return KPI 데이터
     * @author 장민규
     * @since 2025-09-11
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<Kpi>> getKpi(int months) {
        Kpi kpi = adminMapper.getKpiByMonths(months);
        if(kpi == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "해당 기간에 대한 KPI 데이터가 없습니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "KPI 조회 성공", kpi));
    }

    /**
     * 상위 리워드 판매량/매출 조회
     * @param from   조회 시작 날짜
     * @param to     조회 종료 날짜
     * @param limit  상위 N개 리워드 조회 제한
     * @param metric 정렬 기준 (qty: 판매 수량, revenue: 매출)
     * @return 상위 리워드 판매량/매출 데이터 리스트
     * @author 장민규
     * @since 2025-09-11
     */
    @Override
    public ResponseEntity<ResponseDto<List<RewardSalesTop>>> getRewardSalesTops(LocalDate from, LocalDate to, int limit, String metric) {
        List<RewardSalesTop> rewardSalesTops = adminMapper.getRewardSalesTops(from, to, limit, metric);
        if(rewardSalesTops.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "해당 기간에 대한 리워드 판매 데이터가 없습니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "리워드 판매 상위 조회 성공", rewardSalesTops));
    }
}
