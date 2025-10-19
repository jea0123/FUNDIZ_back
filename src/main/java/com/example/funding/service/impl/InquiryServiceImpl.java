package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.IqrAddRequestDto;
import com.example.funding.mapper.InquiryMapper;
import com.example.funding.model.Inquiry;
import com.example.funding.service.InquiryService;
import com.example.funding.validator.Loaders;
import jakarta.validation.constraints.NotBlank;
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
public class InquiryServiceImpl implements InquiryService {
    private final Loaders loaders;
    private final InquiryMapper inquiryMapper;

    /**
     * <p>1:1 문의 내역 목록 조회(관리자 기준)</p>
     *
     * @param pager Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<Inquiry>>> inquiryList(Pager pager) {
        int total = inquiryMapper.inquiryTotal();

        List<Inquiry> inquiryList = inquiryMapper.inquiryList(pager);

        PageResult<Inquiry> result = PageResult.of(inquiryList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "문의내역 목록 조회 성공", result));
    }

    /**
     * <p>내 문의 내역 목록 조회(후원자 기준)</p>
     *
     * @param userId 사용자 ID
     * @param pager  Pager
     * @return 성공 시 200 OK
     * @author 이동혁
     * @since 2025-09-23
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<PageResult<Inquiry>>> myInquiryList(Long userId, Pager pager) {
        loaders.user(userId);
        int total = inquiryMapper.myInquiryTotal(userId);

        List<Inquiry> myInquiryList = inquiryMapper.myInquiryList(userId, pager);

        PageResult<Inquiry> result = PageResult.of(myInquiryList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "문의내역 목록 조회 성공", result));
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
    @Override
    public ResponseEntity<ResponseDto<String>> addInquiry(Long userId, IqrAddRequestDto iqrDto) {
        loaders.user(userId);
        Inquiry item = Inquiry.builder()
                .userId(userId)
                .title(iqrDto.getTitle())
                .content(iqrDto.getContent())
                .createdAt(iqrDto.getCreatedAt())
                .isCanceled(iqrDto.getIsCanceled())
                .ctgr(iqrDto.getCtgr())
                .build();

        inquiryMapper.addInquiry(item);
        return ResponseEntity.ok(ResponseDto.success(200, "문의 등록 성공", "데이터 출력확인"));
    }
}



