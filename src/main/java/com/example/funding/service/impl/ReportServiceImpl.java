package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.RpAddRequestDto;
import com.example.funding.enums.NotificationType;
import com.example.funding.handler.NotificationPublisher;
import com.example.funding.mapper.ReportMapper;
import com.example.funding.model.Report;
import com.example.funding.service.ReportService;
import com.example.funding.validator.Loaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class ReportServiceImpl implements ReportService {
    private final Loaders loaders;
    private final ReportMapper reportMapper;

    private final NotificationPublisher notificationPublisher;

    /**
     * <p>신고 내역 목록 조회(관리자 기준)</p>
     *
     * @param pager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<Report>>> reportList(Pager pager) {
        int total = reportMapper.reportTotal();
        List<Report> reportList = reportMapper.reportList(pager);

        PageResult<Report> result = PageResult.of(reportList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "신고내역 목록 조회 성공", result));
    }

    /**
     * <p>내 신고 내역 목록 조회(후원자 기준)</p>
     *
     * @param userId 사용자 ID
     * @param pager  Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<Report>>> myReportList(Long userId, Pager pager) {
        loaders.user(userId);
        int total = reportMapper.myReportTotal(userId);
        List<Report> myReportList = reportMapper.myReportList(userId, pager);
        PageResult<Report> result = PageResult.of(myReportList, pager, total);
        return ResponseEntity.ok(ResponseDto.success(200, "신고내역 목록 조회 성공", result));
    }


    /**
     * <p>신고 등록</p>
     *
     * @param userId 사용자 ID
     * @param rpDto  RpAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-09-24
     */
    @Override
    public ResponseEntity<ResponseDto<String>> addReport(Long userId, RpAddRequestDto rpDto) {
        loaders.user(userId);
        Report item = Report.builder()
                .userId(userId)
                .target(rpDto.getTarget())
                .reason(rpDto.getReason())
                .reportDate(rpDto.getReportDate())
                .reportStatus(rpDto.getReportStatus())
                .reportType(rpDto.getReportType())
                .build();

        reportMapper.addReport(item);
        notificationPublisher.publish(userId, NotificationType.REPORT_RECEIVED, null, item.getReportId());
        return ResponseEntity.ok(ResponseDto.success(200, "신고 등록 성공", "데이터 출력확인"));
    }
}



