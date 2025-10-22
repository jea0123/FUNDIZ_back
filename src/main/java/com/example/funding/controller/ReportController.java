package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.PagerRequest;
import com.example.funding.dto.request.cs.RpAddRequestDto;
import com.example.funding.model.Report;
import com.example.funding.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cs/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * <p>신고 내역 목록 조회(관리자 기준)</p>
     *
     * @param req Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<PageResult<Report>>> reportList(@Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
        return reportService.reportList(pager);
    }

    /**
     * <p>내 신고 내역 목록 조회(후원자 기준)</p>
     *
     * @param principal 인증된 사용자 정보
     * @param req Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @GetMapping("/mylist")
    public ResponseEntity<ResponseDto<PageResult<Report>>> reportList(@AuthenticationPrincipal CustomUserPrincipal principal, @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
        return reportService.myReportList(principal.userId(), pager);
    }

    /**
     * <p>신고 등록(후원자)</p>
     *
     * @param principal 인증된 사용자 정보
     * @param rpDto RpAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-09-24
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto<String>> addReport(@AuthenticationPrincipal CustomUserPrincipal principal, @RequestBody RpAddRequestDto rpDto){
        return reportService.addReport(principal.userId(), rpDto);
    }

    /**
     * <p>신고내역 상세 페이지 조회(관리자)</p>
     *
     * @param reportId 프로젝트 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-10-19
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<ResponseDto<Report>> item(@PathVariable Long reportId) {
        return reportService.item(reportId);
    }

}
