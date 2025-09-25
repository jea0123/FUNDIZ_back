package com.example.funding.service.impl;

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
    public ResponseEntity<ResponseDto<List<Notice>>> noticeList() {
        List<Notice> noticeList =  noticeMapper.noticeList();
        if (noticeList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(409,"공지사항 목록 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 목록 조회 성공", noticeList));
    }

    //조회수 업데이트
    //250919
    @Override
    public ResponseEntity<ResponseDto<Notice>> item(Long noticeId) {
        noticeMapper.updateViewCnt(noticeId);

        Notice item =  noticeMapper.noticeDetail(noticeId);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(409,"공지사항 상세 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"공지사항 상세 조회 성공", item));
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

        if(result ==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"공지사항 추가 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200,"공지사항 추가 성공", "데이터 출력확인"));
    }


    //공지사항 수정
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> updateNotice(Long noticeId, NoticeUpdateRequestDto ntcDto) {
        ntcDto.setNoticeId(noticeId);

        int result = noticeMapper.updateNotice(ntcDto);
        if(result ==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "공지사항 수정 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200,"공지사항 수정 완료", "공지사항 수정 "));
    }


    //공지사항 삭제
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> deleteNotice(Long noticeId) {
        int deleted = noticeMapper.deleteNotice(noticeId);
        if(deleted == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"공지사항 삭제 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "공지사항 삭제 완료", "공지사항 삭제"));
    }

    //문의내역 목록
    //250923
    @Override
    public ResponseEntity<ResponseDto<List<Inquiry>>> inquiryList() {
        List<Inquiry> inquiryList =  inquiryMapper.inquiryList();
        if (inquiryList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(409,"문의내역 목록 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"문의내역 목록 조회 성공", inquiryList));
    }

    //문의 등록
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> addInquiry(Long userId, IqrAddRequestDto iqrDto) {
        Inquiry item = Inquiry.builder()
                .title(iqrDto.getTitle())
                .content(iqrDto.getContent())
                .createdAt(iqrDto.getCreatedAt())
                .isCanceled(iqrDto.getIsCanceled())
                .ctgr(iqrDto.getCtgr())
                .build();

        int result = inquiryMapper.addInquiry(item);

        if(result ==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"문의 등록 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200,"문의 등록 성공", "데이터 출력확인"));
    }

    //신고내역 목록
    //250923
    @Override
    public ResponseEntity<ResponseDto<List<Report>>> reportList() {
        List<Report> reportList =  reportMapper.reportList();
        if (reportList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(409,"신고내역 목록 조회 불가"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200,"신고내역 목록 조회 성공", reportList));
    }

    //신고 등록
    //250924
    @Override
    public ResponseEntity<ResponseDto<String>> addReport(Long userId, RpAddRequestDto rpDto) {
        Report item = Report.builder()
                .target(rpDto.getTarget())
                .reason(rpDto.getReason())
                .reportDate(rpDto.getReportDate())
                .reportStatus(rpDto.getReportStatus())
                .reportType(rpDto.getReportType())
                .build();

        int result = reportMapper.addReport(item);

        if(result ==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"신고 등록 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200,"신고 등록 성공", "데이터 출력확인"));
    }



}
