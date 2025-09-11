package com.example.funding.dto.response.admin;

import com.example.funding.dto.response.admin.analytic.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminAnalyticsDto {
    private Kpi kpi;
    private List<RevenueTrend> revenueTrends;
    private List<RewardSalesTop> rewardSalesTops;
    private List<PaymentMethod> paymentMethods;
    private List<CategorySuccess> categorySuccesses;
}
