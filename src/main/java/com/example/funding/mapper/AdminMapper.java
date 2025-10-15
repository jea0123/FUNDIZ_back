package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.request.admin.RegisterAdminRequestDto;
import com.example.funding.dto.request.admin.SearchAdminProjectDto;
import com.example.funding.dto.request.admin.UserAdminUpdateRequestDto;
import com.example.funding.dto.response.admin.AdminProjectListDto;
import com.example.funding.dto.response.admin.ProjectVerifyDetailDto;
import com.example.funding.dto.response.admin.ProjectVerifyListDto;
import com.example.funding.dto.response.admin.analytic.*;
import com.example.funding.model.Admin;
import com.example.funding.model.Project;
import com.example.funding.model.User;
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

    int countProject(@Param("dto") SearchAdminProjectDto dto);

    List<AdminProjectListDto> getProjectList(@Param("dto") SearchAdminProjectDto dto, @Param("pager") Pager pager);

    void cancelProject(@Param("projectId") Long projectId);

    void updateProject(Project project);

    int countProjectVerify(@Param("dto") SearchAdminProjectDto dto);

    List<ProjectVerifyListDto> getProjectVerifyList(@Param("dto") SearchAdminProjectDto dto, @Param("pager") Pager pager);

    ProjectVerifyDetailDto getProjectVerifyDetail(@Param("projectId") Long projectId);

    int isApprovable(@Param("projectId") Long projectId);

    void approveProject(@Param("projectId") Long projectId);

    int isRejectable(@Param("projectId") Long projectId);

    void rejectProject(@Param("projectId") Long projectId, @Param("rejectedReason") String rejectedReason);

    int userTotal();

    List<User> userList(@Param("pager") Pager pager);

    void updateUser(UserAdminUpdateRequestDto userDto);

    void registerAdmin(RegisterAdminRequestDto dto);

    Admin getAdminByAdminId(String adminId);
}
