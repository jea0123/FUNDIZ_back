package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.response.admin.SearchReviewDto;
import com.example.funding.dto.response.admin.analytic.*;
import com.example.funding.dto.row.ReviewListRow;
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

    int countReviews(@Param("dto") SearchReviewDto dto);

    List<ReviewListRow> getReviewList(@Param("dto") SearchReviewDto dto, @Param("pager") Pager pager);
}
