package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.common.Utils;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.admin.AdminProjectUpdateDto;
import com.example.funding.dto.request.admin.SearchAdminProjectDto;
import com.example.funding.dto.request.admin.UserAdminUpdateRequestDto;
import com.example.funding.dto.request.cs.NoticeUpdateRequestDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.AdminProjectListDto;
import com.example.funding.dto.response.admin.ProjectVerifyDetailDto;
import com.example.funding.dto.response.admin.ProjectVerifyListDto;
import com.example.funding.dto.response.admin.analytic.*;
import com.example.funding.exception.AnalyticsNotFoundException;
import com.example.funding.exception.CategorySuccessNotFoundException;
import com.example.funding.exception.KPINotFoundException;
import com.example.funding.exception.RewardSalesNotFoundException;
import com.example.funding.mapper.AdminMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.RewardMapper;
import com.example.funding.mapper.TagMapper;
import com.example.funding.model.*;
import com.example.funding.service.AdminService;
import com.example.funding.service.validator.ProjectTransitionGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    private final ProjectTransitionGuard transitionGuard;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<AdminAnalyticsDto>> getAdminAnalytics(LocalDate from, LocalDate to, int limit, String metric, int months, Long ctgrId) {
        Kpi kpi = adminMapper.getKpiByMonths(months);
        List<RevenueTrend> revenueTrends = adminMapper.getMonthlyTrends(months);
        List<RewardSalesTop> rewardSalesTops = adminMapper.getRewardSalesTops(from, to, limit, metric);
        List<PaymentMethod> paymentMethods = adminMapper.getPaymentMethods(from, to);
        List<CategorySuccess> categorySuccesses = adminMapper.getCategorySuccessByCategory(ctgrId);

        if (kpi == null || revenueTrends.isEmpty() || rewardSalesTops.isEmpty() || paymentMethods.isEmpty() || categorySuccesses.isEmpty()) {
            throw new AnalyticsNotFoundException();
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

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(Long ctgrId) {
        List<CategorySuccess> categorySuccesses = adminMapper.getCategorySuccessByCategory(ctgrId);
        if (categorySuccesses.isEmpty()) {
            throw new CategorySuccessNotFoundException();
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "카테고리별 성공률 조회 성공", categorySuccesses));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<Kpi>> getKpi(int months) {
        Kpi kpi = adminMapper.getKpiByMonths(months);
        if (kpi == null) {
            throw new KPINotFoundException();
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "KPI 조회 성공", kpi));
    }

    @Override
    public ResponseEntity<ResponseDto<List<RewardSalesTop>>> getRewardSalesTops(LocalDate from, LocalDate to, int limit, String metric) {
        List<RewardSalesTop> rewardSalesTops = adminMapper.getRewardSalesTops(from, to, limit, metric);
        if (rewardSalesTops.isEmpty()) {
            throw new RewardSalesNotFoundException();
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "리워드 판매 상위 조회 성공", rewardSalesTops));
    }

    /**
     * <p>프로젝트 목록 조회</p>
     *
     * @param dto   SearchProjectVerifyDto
     * @param pager pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-10-01
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<AdminProjectListDto>>> getProjectList(SearchAdminProjectDto dto, Pager pager) {
        dto.applyRangeType();

        int total = adminMapper.countProject(dto);
        List<AdminProjectListDto> items = Collections.emptyList();

        if (total > 0) {
            items = adminMapper.getProjectList(dto, pager);
            for (AdminProjectListDto item : items) {
                item.setPercentNow(Utils.getPercentNow(item.getCurrAmount(), item.getGoalAmount()));
            }
        }
        PageResult<AdminProjectListDto> result = PageResult.of(items, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 목록 조회 성공", result));
    }

    /**
     * <p>프로젝트 취소</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-17
     */
    @Override
    public ResponseEntity<ResponseDto<String>> cancelProject(Long projectId) {
        int result = adminMapper.cancelProject(projectId);
        if (result == 0) {
            throw new IllegalStateException("이미 취소되었거나 존재하지 않는 프로젝트입니다.");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 취소 성공", null));
    }

    /**
     * <p>프로젝트 수정</p>
     *
     * @param dto ProjectCreateRequestDto
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-17
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateProject(AdminProjectUpdateDto dto) {
        if (dto.getProjectId() == null) {
            throw new IllegalArgumentException("프로젝트 ID가 필요합니다.");
        }

        Project existing = projectMapper.findById(dto.getProjectId());
        if (existing == null) {
            throw new IllegalStateException("존재하지 않는 프로젝트입니다.");
        }

        //TODO: 목표금액, 종료일 무결성 체크

        Project project = Project.builder()
                .projectId(existing.getProjectId())
                .subctgrId(dto.getSubctgrId())
                .title(dto.getTitle())
                .thumbnail(dto.getThumbnail())
                .projectStatus(dto.getProjectStatus())
                .build();

        int result = adminMapper.updateProject(project);
        if (result == 0) {
            throw new IllegalStateException("프로젝트 수정 실패");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 수정 성공", null));
    }

    /**
     * <p>프로젝트 심사 목록 조회</p>
     *
     * @param dto   SearchProjectVerifyDto
     * @param pager pager
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-18
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<ProjectVerifyListDto>>> getProjectVerifyList(SearchAdminProjectDto dto, Pager pager) {
        dto.applyRangeType();

        int total = adminMapper.countProjectVerify(dto);

        List<ProjectVerifyListDto> items = Collections.emptyList();
        if (total > 0) {
            items = adminMapper.getProjectVerifyList(dto, pager);
        }
        PageResult<ProjectVerifyListDto> result = PageResult.of(items, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트 심사 목록 조회 성공", result));
    }

    /**
     * <p>프로젝트 심사 상세 조회</p>
     *
     * @param projectId 프로젝트 ID
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-19
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<ProjectVerifyDetailDto>> getProjectVerifyDetail(Long projectId) {
        ProjectVerifyDetailDto detail = adminMapper.getProjectVerifyDetail(projectId);
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
     * @author 조은애
     * @since 2025-09-19
     */
    @Override
    public ResponseEntity<ResponseDto<String>> approveProject(Long projectId) {
        // Guard
        transitionGuard.assertCanApprove(projectId);

        int result = adminMapper.approveProject(projectId);
        if (result != 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 처리되었거나 현재 상태에서 승인할 수 없습니다.");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트가 성공적으로 승인되었습니다.", null));
    }

    /**
     * <p>프로젝트 반려</p>
     *
     * @param projectId      프로젝트 ID
     * @param rejectedReason 반려 사유
     * @return 성공 시 200 OK
     * @author 조은애
     * @since 2025-09-19
     */
    @Override
    public ResponseEntity<ResponseDto<String>> rejectProject(Long projectId, String rejectedReason) {
        // Guard
        transitionGuard.requireVerifying(projectId);

        int result = adminMapper.rejectProject(projectId, rejectedReason);
        if (result != 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 처리되었거나 현재 상태에서 반려할 수 없습니다.");
        }

        return ResponseEntity.ok(ResponseDto.success(200, "프로젝트가 성공적으로 반려되었습니다.", null));
    }

    /**
     * <p>회원 관리 목록 조회</p>
     *
     * @param pager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-06
     */
    @Override
    public ResponseEntity<ResponseDto<PageResult<User>>> userList(Pager pager) {
        int total = adminMapper.userTotal();

        List<User> userList = adminMapper.userList(pager);

        PageResult<User> result = PageResult.of(userList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "회원 목록 조회 성공", result));
    }

    /**
     * <p>회원 정보 상세 페이지 조회</p>
     *
     * @param userId 사용자 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-13
     */
    @Override
    public ResponseEntity<ResponseDto<User>> item(Long userId) {
        User item = adminMapper.userDetail(userId);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "회원 정보 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "회원 정보 조회 성공", item));
    }

    /**
     * <p>회원 정보 수정(관리자)</p>
     *
     * @param userId 사용자 ID
     * @param userDto UserAdminUpdateRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-13
     */
    @Override
    public ResponseEntity<ResponseDto<String>> updateUser(Long userId, UserAdminUpdateRequestDto userDto) {
        userDto.setUserId(userId);

        int result = adminMapper.updateUser(userDto);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "회원정보 수정 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "회원정보 수정 완료", "회원정보 수정 "));
    }
}
