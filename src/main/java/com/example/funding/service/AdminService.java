package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.admin.AdminAnalyticsDto;
import com.example.funding.dto.response.admin.analytic.CategorySuccess;
import com.example.funding.dto.response.admin.analytic.Kpi;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface AdminService {
    ResponseEntity<ResponseDto<AdminAnalyticsDto>> getAdminAnalytics(Date from, Date to, int limit, String metric, int month, Long ctgrId);

    ResponseEntity<ResponseDto<List<CategorySuccess>>> getCategorySuccessByCategory(Long ctgrId);

    ResponseEntity<ResponseDto<Kpi>> getKpi(int month);
}
