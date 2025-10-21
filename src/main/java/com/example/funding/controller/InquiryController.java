package com.example.funding.controller;

import com.example.funding.common.CursorPage;
import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.PagerRequest;
import com.example.funding.dto.request.cs.IqrAddRequestDto;
import com.example.funding.dto.request.cs.IqrReplyCreateRequestDto;
import com.example.funding.dto.response.cs.InquiryReplyDto;
import com.example.funding.model.Inquiry;
import com.example.funding.service.InquiryService;
import com.example.funding.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/cs/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;
    private final ReplyService replyService;

    /**
     * <p>1:1 문의 내역 목록 조회(관리자 기준)</p>
     *
     * @param req Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @GetMapping("/list")
    public ResponseEntity<ResponseDto<PageResult<Inquiry>>> inquiryList(@Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
        return inquiryService.inquiryList(pager);
    }

    /**
     * <p>내 문의 내역 목록 조회(후원자 기준)</p>
     *
     * @param principal 인증된 사용자 정보
     * @param req       Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @GetMapping("/mylist")
    public ResponseEntity<ResponseDto<PageResult<Inquiry>>> myInquiryList(@AuthenticationPrincipal CustomUserPrincipal principal, @Valid PagerRequest req) {
        Pager pager = Pager.ofRequest(req.getPage(), req.getSize(), req.getPerGroup());
        return inquiryService.myInquiryList(principal.userId(), pager);
    }

    /**
     * <p>1:1 문의 등록</p>
     *
     * @param principal 인증된 사용자 정보
     * @param iqrDto    IqrAddRequestDto
     * @return 성공 시 200 OK, 실패 시 404 NOT FOUND
     * @author 이동혁
     * @since 2025-09-24
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDto<String>> addInquiry(@AuthenticationPrincipal CustomUserPrincipal principal, @RequestBody IqrAddRequestDto iqrDto) {
        return inquiryService.addInquiry(principal.userId(), iqrDto);
    }


    /**
     * <p>문의내역 답변 조회</p>
     *
     * @param inqId         문의내역 ID
     * @param lastCreatedAt 마지막 항목의 생성일시
     * @param lastId        마지막 항목의 id
     * @param size          한 번에 가져올 항목 수
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-13
     */
    @GetMapping("/reply/{inqId}")
    public ResponseEntity<ResponseDto<CursorPage<InquiryReplyDto>>> getInquiryReplyList(@PathVariable Long inqId,
                                                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastCreatedAt,
                                                                                        @RequestParam(required = false) Long lastId,
                                                                                        @RequestParam(defaultValue = "10") int size) {
        return replyService.getInquiryReplyList(inqId, lastCreatedAt, lastId, size);
    }

    /**
     * <p>문의내역 답변 등록</p>
     *
     * @param inqId 문의내역 ID
     * @param dto   IqrReplyCreateRequestDto
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-10-13
     */
    @PostMapping("/reply/{inqId}")
    public ResponseEntity<ResponseDto<InquiryReplyDto>> createInquiryReply(@PathVariable Long inqId,
                                                                           @RequestBody IqrReplyCreateRequestDto dto) {
        return replyService.createInquiryReply(inqId, dto);
    }

}
