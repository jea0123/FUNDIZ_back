package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.*;
import com.example.funding.model.Notice;
import com.example.funding.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cs/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * <p>공지사항 목록 조회</p>
     *
     * @param reqPager 요청 pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-19
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<PageResult<Notice>>> noticeList(Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        return noticeService.noticeList(pager);
    }

    /**
     * <p>공지사항 상세 페이지 조회</p>
     *
     * @param noticeId 프로젝트 ID
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-09-19
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<ResponseDto<Notice>> item(@PathVariable Long noticeId) {
        return noticeService.item(noticeId);
    }

}
