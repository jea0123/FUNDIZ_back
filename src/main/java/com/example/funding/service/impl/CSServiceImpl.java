package com.example.funding.service.impl;

import com.example.funding.common.PageResult;
import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.*;
import com.example.funding.mapper.InquiryMapper;
import com.example.funding.mapper.NoticeMapper;
import com.example.funding.mapper.ReportMapper;
import com.example.funding.model.Inquiry;
import com.example.funding.model.Notice;
import com.example.funding.model.Report;
import com.example.funding.service.CSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CSServiceImpl implements CSService {

    private final NoticeMapper noticeMapper;
    private final InquiryMapper inquiryMapper;
    private final ReportMapper reportMapper;

    //공지사항 목록
    //250919
    @Override
    public ResponseEntity<ResponseDto<PageResult<Notice>>> noticeList(Pager pager) {
        int total = noticeMapper.noticeTotal();

        List<Notice> noticeList = noticeMapper.noticeList(pager);

        PageResult<Notice> result = PageResult.of(noticeList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "공지사항 목록 조회 성공", result));
    }

    //조회수 업데이트
    //250919
    @Override
    public ResponseEntity<ResponseDto<Notice>> item(Long noticeId) {
        noticeMapper.updateViewCnt(noticeId);

        Notice item = noticeMapper.noticeDetail(noticeId);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(409, "공지사항 상세 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "공지사항 상세 조회 성공", item));
    }

    //공지사항 등록
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> addNotice(NoticeAddRequestDto ntcDto) {
        Notice item = Notice.builder()
                .title(ntcDto.getTitle())
                .content(ntcDto.getContent())
                .viewCnt(ntcDto.getViewCnt())
                .createdAt(ntcDto.getCreatedAt())
                .build();

        int result = noticeMapper.addNotice(item);

        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "공지사항 추가 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "공지사항 추가 성공", "데이터 출력확인"));
    }


    //공지사항 수정
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> updateNotice(Long noticeId, NoticeUpdateRequestDto ntcDto) {
        ntcDto.setNoticeId(noticeId);

        int result = noticeMapper.updateNotice(ntcDto);
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "공지사항 수정 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "공지사항 수정 완료", "공지사항 수정 "));
    }


    //공지사항 삭제
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> deleteNotice(Long noticeId) {
        int deleted = noticeMapper.deleteNotice(noticeId);
        if (deleted == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "공지사항 삭제 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "공지사항 삭제 완료", "공지사항 삭제"));
    }

    //문의내역 목록
    //250923
    @Override
    public ResponseEntity<ResponseDto<PageResult<Inquiry>>> inquiryList(Pager pager) {
        int total = inquiryMapper.inquiryTotal();

        List<Inquiry> inquiryList = inquiryMapper.inquiryList(pager);

        PageResult<Inquiry> result = PageResult.of(inquiryList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "문의내역 목록 조회 성공", result));
    }

    //내 문의내역 목록
    //250923
    @Override
    public ResponseEntity<ResponseDto<PageResult<Inquiry>>> myInquiryList(Long userId, Pager pager) {
        int total = inquiryMapper.myInquiryTotal(userId);

        List<Inquiry> myInquiryList = inquiryMapper.myInquiryList(userId, pager);

        PageResult<Inquiry> result = PageResult.of(myInquiryList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "문의내역 목록 조회 성공", result));
    }

    //문의 등록
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> addInquiry(Long userId, IqrAddRequestDto iqrDto) {
        Inquiry item = Inquiry.builder()
                .userId(userId)
                .title(iqrDto.getTitle())
                .content(iqrDto.getContent())
                .createdAt(iqrDto.getCreatedAt())
                .isCanceled(iqrDto.getIsCanceled())
                .ctgr(iqrDto.getCtgr())
                .build();

        int result = inquiryMapper.addInquiry(item);

        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "문의 등록 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "문의 등록 성공", "데이터 출력확인"));
    }

    //신고내역 목록
    //250923
    @Override
    public ResponseEntity<ResponseDto<PageResult<Report>>> reportList(Pager pager) {
        int total = reportMapper.reportTotal();
        List<Report> reportList = reportMapper.reportList(pager);

        PageResult<Report> result = PageResult.of(reportList, pager, total);

        return ResponseEntity.ok(ResponseDto.success(200, "신고내역 목록 조회 성공", result));
    }

    //내 신고내역 목록
    //250923
    @Override
    public ResponseEntity<ResponseDto<PageResult<Report>>> myReportList(Long userId, Pager pager) {
        int total = reportMapper.myReportTotal(userId);
        List<Report> myReportList = reportMapper.myReportList(userId, pager);
        PageResult<Report> result = PageResult.of(myReportList, pager, total);
        return ResponseEntity.ok(ResponseDto.success(200, "신고내역 목록 조회 성공", result));
    }


    //신고 등록
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> addReport(Long userId, RpAddRequestDto rpDto) {
        Report item = Report.builder()
                .userId(userId)
                .target(rpDto.getTarget())
                .reason(rpDto.getReason())
                .reportDate(rpDto.getReportDate())
                .reportStatus(rpDto.getReportStatus())
                .reportType(rpDto.getReportType())
                .build();

        int result = reportMapper.addReport(item);

        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "신고 등록 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "신고 등록 성공", "데이터 출력확인"));
    }
}



