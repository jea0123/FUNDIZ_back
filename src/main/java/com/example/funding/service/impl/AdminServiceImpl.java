package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.admin.RejectRequestDto;
import com.example.funding.dto.request.project.ProjectUpdateRequestDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.ReviewDetailDto;
import com.example.funding.dto.response.admin.SearchReviewDto;
import com.example.funding.dto.response.admin.analytic.*;
import com.example.funding.dto.row.ReviewListRow;
import com.example.funding.mapper.AdminMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.RewardMapper;
import com.example.funding.mapper.TagMapper;
import com.example.funding.model.Project;
import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import com.example.funding.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    private final ProjectMapper projectMapper;
    private final TagMapper tagMapper;
    private final RewardMapper rewardMapper;

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

    /**
     * <p>프로젝트 취소</p>
     *
     * @param projectId 프로젝트 ID
     * @param adId 관리자번호
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-17
     */
    @Override
    public ResponseEntity<ResponseDto<String>> cancelProject(Long projectId, Long adId) {
        int result = adminMapper.cancelProject(projectId);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "이미 취소되었거나 존재하지 않는 프로젝트입니다."));
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 취소 성공", null));
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param dto ProjectUpdateRequestDto
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-17
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateProject(ProjectUpdateRequestDto dto) {
        if (dto.getProjectId() == null) {
            throw new IllegalArgumentException("프로젝트 ID가 필요합니다.");
        }

        Project existing = projectMapper.findById(dto.getProjectId());
        if (existing == null) {
            throw new IllegalStateException("존재하지 않는 프로젝트입니다.");
        }

        //목표금액, 종료일 무결성 체크

        Project project = Project.builder()
                .projectId(existing.getProjectId())
                .subctgrId(dto.getSubctgrId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .thumbnail(dto.getThumbnail())
                .goalAmount(dto.getGoalAmount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        int result = projectMapper.updateProject(project);
        if (result == 0) {
            throw new IllegalStateException("프로젝트 수정 실패");
        }

        //태그 전체 삭제 후 저장
        tagMapper.deleteTags(dto.getProjectId());
        if (dto.getTagList() != null) {
            for (Tag tag : dto.getTagList()) {
                int saved = tagMapper.saveTag(dto.getProjectId(), tag.getTagName());
                if (saved == 0) {
                    throw new IllegalStateException("태그 저장 실패");
                }
            }
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 수정 성공", null));
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
    @Override
    public ResponseEntity<ResponseDto<PageResult<ReviewListRow>>> getReviewList(SearchReviewDto dto, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 20,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        int total = adminMapper.countReviews(dto);

        List<ReviewListRow> items = (total == 0) ? Collections.emptyList() : adminMapper.getReviewList(dto, pager);
        PageResult<ReviewListRow> result = PageResult.of(items, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 심사 목록 조회 성공", result));
    }

    /**
     * <p>프로젝트 심사 상세 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-19
     */
    @Override
    public ResponseEntity<ResponseDto<ReviewDetailDto>> getReviewDetail(Long projectId) {
        ReviewDetailDto detail = adminMapper.getReviewDetail(projectId);
        if (detail == null) {
            throw new IllegalStateException("존재하지 않는 프로젝트입니다.");
        }
        List<Tag> tagList = tagMapper.getTagList(projectId);
        detail.setTagList(tagList);

        List<Reward> rewardList = rewardMapper.findByProjectId(projectId);
        detail.setRewardList(rewardList);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 심사 상세 조회 성공", detail));
    }

    /**
     * <p>프로젝트 승인</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-19
     */
    @Override
    public ResponseEntity<ResponseDto<String>> approve(Long projectId) {
        int result = adminMapper.approve(projectId);
        if (result == 0) {
            throw new IllegalStateException("승인 처리가 실패되었습니다.");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트가 성공적으로 승인되었습니다.", null));
    }

    /**
     * <p>프로젝트 반려</p>
     *
     * @param projectId 프로젝트 ID
     * @param rejectedReason 반려 사유
     * @return 성공 시 200 OK
     * @author by: 조은애
     * @since 2025-09-19
     */
    @Override
    public ResponseEntity<ResponseDto<String>> reject(Long projectId, String rejectedReason) {
        int result = adminMapper.reject(projectId, rejectedReason);
        if (result == 0) {
            throw new IllegalStateException("반려 처리가 실패되었습니다.");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트가 성공적으로 반려되었습니다.", null));
    }
}
