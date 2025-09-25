package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.request.admin.SearchProjectVerifyDto;
import com.example.funding.dto.response.admin.ProjectVerifyDetailDto;
import com.example.funding.dto.response.admin.analytic.*;
import com.example.funding.dto.response.admin.ProjectVerifyListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AdminMapper {
    Kpi getKpiByMonths(@Param("months") int months);

    List<RevenueTrend> getMonthlyTrends(@Param("months") int months);

    List<RewardSalesTop> getRewardSalesTops(@Param("from") LocalDate from,
                                            @Param("to") LocalDate to,
                                            @Param("limit") int limit,
                                            @Param("metric") String metric);

    List<PaymentMethod> getPaymentMethods(@Param("from") LocalDate from,
                                          @Param("to") LocalDate to);

    List<CategorySuccess> getCategorySuccessByCategory(@Param("ctgrId") Long ctgrId);

    int cancelProject(@Param("projectId") Long projectId);

    int countProjectVerify(@Param("dto") SearchProjectVerifyDto dto);

    List<ProjectVerifyListDto> getProjectVerifyList(@Param("dto") SearchProjectVerifyDto dto, @Param("pager") Pager pager);

    ProjectVerifyDetailDto getProjectVerifyDetail(@Param("projectId") Long projectId);

    int approveProject(@Param("projectId") Long projectId);

    int rejectProject(@Param("projectId") Long projectId, @Param("rejectedReason") String rejectedReason);
}
