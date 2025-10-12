package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.RpAddRequestDto;
import com.example.funding.model.Report;
import com.example.funding.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cs/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * <p>신고 내역 목록 조회(관리자 기준)</p>
     *
     * @param reqPager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<PageResult<Report>>> reportList(Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        return reportService.reportList(pager);
    }


    /**
     * <p>내 신고 내역 목록 조회(후원자 기준)</p>
     *
     * @param userId 사용자 ID
     * @param reqPager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @GetMapping("/mylist/{userId}")
    public ResponseEntity<ResponseDto<PageResult<Report>>> reportList(@PathVariable Long userId, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        return reportService.myReportList(userId, pager);
    }

    /**
     * <p>신고 등록</p>
     *
     * @param userId 사용자 ID
     * @param rpDto RpAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-09-24
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<ResponseDto<String>> addReport(@PathVariable Long userId, @RequestBody RpAddRequestDto rpDto){
        return reportService.addReport(userId, rpDto);
    }

}
