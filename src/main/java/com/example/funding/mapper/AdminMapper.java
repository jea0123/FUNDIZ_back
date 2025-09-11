package com.example.funding.mapper;

import com.example.funding.dto.response.admin.analytic.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface AdminMapper {
    Kpi getKpiByMonths(@Param("months") int months);

    List<RevenueTrend> getMonthlyTrends(@Param("months") int months);

    List<RewardSalesTop> getRewardSalesTops(@Param("from") Date from,
                                            @Param("to") Date to,
                                            @Param("limit") int limit,
                                            @Param("metric") String metric);

    List<PaymentMethod> getPaymentMethods(@Param("from") Date from,
                                          @Param("to") Date to);

    List<CategorySuccess> getCategorySuccessByCategory(@Param("ctgrId") Long ctgrId);
}
