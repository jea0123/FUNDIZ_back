package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.common.Utils;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.admin.AdminProjectUpdateDto;
import com.example.funding.dto.request.admin.RejectProjectDto;
import com.example.funding.dto.request.admin.SearchAdminProjectDto;
import com.example.funding.dto.request.settlement.SettlementPaidRequestDto;
import com.example.funding.dto.request.settlement.SettlementSearchCond;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.AdminProjectListDto;
import com.example.funding.dto.response.admin.ProjectVerifyDetailDto;
import com.example.funding.dto.response.admin.ProjectVerifyListDto;
import com.example.funding.dto.response.admin.analytic.CategorySuccess;
import com.example.funding.dto.response.admin.analytic.Kpi;
import com.example.funding.dto.response.admin.analytic.RewardSalesTop;
import com.example.funding.dto.response.settlement.SettlementItem;
import com.example.funding.dto.row.SettlementSummary;
import com.example.funding.exception.*;
import com.example.funding.model.User;
import com.example.funding.service.AdminService;
import com.example.funding.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    private final SettlementService settlementService;

    /**
     * 관리자 대시보드 분석 데이터 조회
     *
     * @param period 조회 기간 (1m, 3m, 6m, 1y, all)
     * @param metric 정렬 기준 (qty: 판매 수량, revenue: 매출)
     * @param limit  상위 N개 리워드 조회 제한
     * @param ctgrId 카테고리 ID
     * @return 관리자 대시보드 분석 데이터
     * @throws AnalyticsNotFoundException 데이터가 존재하지 않을 경우 (기간 내 데이터가 없을 때)
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
     *
     * @param ctgrId 카테고리 ID
     * @return 특정 카테고리의 성공률 분석 데이터
     * @throws CategorySuccessNotFoundException 데이터가 존재하지 않을 경우 (해당 카테고리에 속한 프로젝트가 없을 때)
     * @author 장민규
     * @since 2025-09-11
     */
    @GetMapping("/category-success")
    public ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(@RequestParam Long ctgrId) {
        return adminService.getCategorySuccessByCategory(ctgrId);
    }

    /**
     * 주요 성과 지표(KPI) 조회
     *
     * @param period 조회 기간 (1m, 3m, 6m, 1y, all)
     * @return 주요 성과 지표(KPI) 데이터
     * @throws KPINotFoundException 데이터가 존재하지 않을 경우 (기간 내 데이터가 없을 때)
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
     *
     * @param period 조회 기간 (1m, 3m, 6m, 1y, all)
     * @param metric 정렬 기준 (qty: 판매 수량, revenue: 매출)
     * @param limit  상위 N개 리워드 조회 제한
     * @return 상위 리워드 판매량/매출 데이터 리스트
     * @throws RewardSalesNotFoundException 데이터가 존재하지 않을 경우 (기간 내 데이터가 없을 때)
     * @author 장민규
     * @since 2025-09-11
     */
    @GetMapping("/reward-sales-top")
    public ResponseEntity<ResponseDto<List<RewardSalesTop>>> getRewardSalesTops(@RequestParam(defaultValue = "6m") String period, @RequestParam(defaultValue = "qty") String metric,
                                                                                @RequestParam(defaultValue = "5") int limit) {
        Utils.AnalyticsWindow w = resolveWindow(period, metric, KST);
        return adminService.getRewardSalesTops(w.getFrom(), w.getTo(), limit, metric);
    }

    /**
     * <p>프로젝트 목록 조회</p>
     *
     * @param dto      SearchProjectVerifyDto
     * @param reqPager 요청 pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-01
     */
    @GetMapping("/project")
    public ResponseEntity<ResponseDto<PageResult<AdminProjectListDto>>> getProjectList(SearchAdminProjectDto dto, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 5,
                reqPager != null ? reqPager.getPerGroup() : null
        );

        return adminService.getProjectList(dto, pager);
    }

    /**
     * <p>프로젝트 취소</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-17
     */
    @PostMapping("/project/{projectId}/cancel")
    public ResponseEntity<ResponseDto<String>> cancelProject(@PathVariable Long projectId) {
        //TODO: 관리자 체크

        return adminService.cancelProject(projectId);
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto       AdminProjectUpdateDto
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-17
     */
    @PostMapping("/project/{projectId}")
    public ResponseEntity<ResponseDto<String>> updateProject(@PathVariable Long projectId, @RequestBody AdminProjectUpdateDto dto) {
        //TODO: 관리자 체크

        dto.setProjectId(projectId);

        return adminService.updateProject(dto);
    }

    /**
     * <p>프로젝트 심사 목록 조회</p>
     *
     * @param dto      SearchProjectVerifyDto
     * @param reqPager 요청 pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-18
     */
    @GetMapping("/verify")
    public ResponseEntity<ResponseDto<PageResult<ProjectVerifyListDto>>> getProjectVerifyList(SearchAdminProjectDto dto, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 5,
                reqPager != null ? reqPager.getPerGroup() : null
        );

        return adminService.getProjectVerifyList(dto, pager);
    }

    /**
     * <p>프로젝트 심사 상세 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-19
     */
    @GetMapping("/verify/{projectId}")
    public ResponseEntity<ResponseDto<ProjectVerifyDetailDto>> getProjectVerifyDetail(@PathVariable Long projectId) {
        return adminService.getProjectVerifyDetail(projectId);
    }

    /**
     * <p>프로젝트 승인</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-19
     */
    @PostMapping("/verify/{projectId}/approve")
    public ResponseEntity<ResponseDto<String>> approveProject(@PathVariable Long projectId) {
        //TODO: 관리자 체크

        return adminService.approveProject(projectId);
    }

    /**
     * <p>프로젝트 반려</p>
     *
     * @param projectId 프로젝트 ID
     * @param dto       RejectProjectDto
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-19
     */
    @PostMapping("/verify/{projectId}/reject")
    public ResponseEntity<ResponseDto<String>> rejectProject(@PathVariable Long projectId, @RequestBody RejectProjectDto dto) {
        //TODO: 관리자 체크

        return adminService.rejectProject(projectId, dto.getRejectedReason());
    }

    /**
     * <p>회원 관리 목록 조회</p>
     *
     * @param reqPager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-06
     */
    @GetMapping("/user/list")
    public ResponseEntity<ResponseDto<PageResult<User>>> userList(Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        return adminService.userList(pager);
    }

    /**
     * <p>정산 목록 조회 (검색 + 페이징)</p>
     *
     * @param q       검색어 (프로젝트명, 크리에이터명)
     * @param status  정산 상태 (ALL, PENDING, COMPLETED)
     * @param from    시작 날짜 (yyyy-MM-dd)
     * @param to      종료 날짜 (yyyy-MM-dd)
     * @param page    페이지 번호 (1부터 시작)
     * @param size    페이지 크기
     * @param perGroup 페이지 그룹당 페이지 수
     * @return 정산 목록
     * @throws IllegalArgumentException 잘못된 요청 파라미터일 때
     * @author 장민규
     * @since 2025-10-13
     */
    @GetMapping("/settlement/list")
    public ResponseEntity<ResponseDto<PageResult<SettlementItem>>> getSettlements(
            @RequestParam(required = false) String q,
            @RequestParam(required = false, defaultValue = "ALL") String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "5") Integer perGroup
    ) {
        SettlementSearchCond cond = SettlementSearchCond.builder()
                .q(q)
                .status(status)
                .from(from)
                .to(to)
                .build();
        Pager pager = Pager.ofRequest(page, size, perGroup);
        return settlementService.getSettlements(cond, pager);
    }

    @GetMapping("/settlement/summary")
    public ResponseEntity<ResponseDto<SettlementSummary>> getSettlementSummary() {
        return settlementService.getSettlementSummary();
    }

    /**
     * <p>정산 상태 변경</p>
     *
     * @param dto 정산 요청 DTO
     * @return 정산 정보
     * @throws ProjectNotFoundException   존재하지 않는 프로젝트일 때
     * @throws AccessDeniedException      접근 권한이 없을 때
     * @throws ProjectNotSuccessException 프로젝트가 성공 상태가 아닐 때
     * @throws SettlementNotFoundException 정산 정보를 찾을 수 없을 때
     * @throws SettlementStatusAlreadyChangedException 이미 변경된 상태일 때
     * @author 장민규
     * @since 2025-10-13
     */
    @PostMapping("/settlement")
    public ResponseEntity<ResponseDto<String>> updateStatus(@RequestBody SettlementPaidRequestDto dto) {
        return settlementService.updateStatus(dto);
    }
}
