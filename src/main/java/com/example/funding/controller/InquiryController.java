package com.example.funding.controller;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.IqrAddRequestDto;
import com.example.funding.model.Inquiry;
import com.example.funding.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cs/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;


    /**
     * <p>1:1 문의 내역 목록 조회(관리자 기준)</p>
     *
     * @param reqPager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<PageResult<Inquiry>>> inquiryList(Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        return inquiryService.inquiryList(pager);
    }

    /**
     * <p>내 문의 내역 목록 조회(후원자 기준)</p>
     *
     * @param userId 사용자 ID
     * @param reqPager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @GetMapping("/mylist/{userId}")
    public ResponseEntity<ResponseDto<PageResult<Inquiry>>> myInquiryList(@PathVariable Long userId, Pager reqPager) {
        Pager pager = Pager.ofRequest(
                reqPager != null ? reqPager.getPage() : 1,
                reqPager != null ? reqPager.getSize() : 10,
                reqPager != null ? reqPager.getPerGroup() : 5
        );

        return inquiryService.myInquiryList(userId, pager);
    }

    /**
     * <p>1:1 문의 등록</p>
     *
     * @param userId 사용자 ID
     * @param iqrDto IqrAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-09-24
     */
    @PostMapping("/{userId}/add")
    public ResponseEntity<ResponseDto<String>> addInquiry(@PathVariable Long userId, @RequestBody IqrAddRequestDto iqrDto){
        return inquiryService.addInquiry(userId, iqrDto);
    }
}
