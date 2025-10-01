package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.*;
import com.example.funding.model.Inquiry;
import com.example.funding.model.Notice;
import com.example.funding.model.Report;
import com.example.funding.service.CSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cs")
@RequiredArgsConstructor
public class CSController {

    private final CSService csService;

    //공지사항 목록
    //250919
    @GetMapping("/notice")
    public ResponseEntity<ResponseDto<List<Notice>>> noticeList() {
        return csService.noticeList();
    }

    //공지사항 상세
    //250919
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<ResponseDto<Notice>> item(@PathVariable Long noticeId) {
        return csService.item(noticeId);
    }

    //공지사항 등록
    //250924
    @PostMapping("/notice/add")
    public ResponseEntity<ResponseDto<String>> addNotice(@RequestBody NoticeAddRequestDto ntcDto){

        return csService.addNotice(ntcDto);
    }

    //공지사항 수정
    //250924
    @PostMapping("notice/update/{noticeId}")
    public ResponseEntity<ResponseDto<String>> updateNotice(@PathVariable Long noticeId, @RequestBody NoticeUpdateRequestDto ntcDto){
        return csService.updateNotice(noticeId, ntcDto);
    }

    //공지사항 삭제
    //250924
    @DeleteMapping("notice/delete/{noticeId}")
    public ResponseEntity<ResponseDto<String>> deleteNotice(@PathVariable Long noticeId) {
        return csService.deleteNotice(noticeId);
    }

    //문의내역 목록
    //250923
    @GetMapping("/inquiry/list")
    public ResponseEntity<ResponseDto<List<Inquiry>>> inquiryList() {
        return csService.inquiryList();
    }

    //내 문의내역 목록
    //250923
    @GetMapping("/inquiry/mylist/{userId}")
    public ResponseEntity<ResponseDto<List<Inquiry>>> myInquiryList(@PathVariable Long userId) {
        return csService.myInquiryList(userId);
    }

    //문의 등록
    //250924
    @PostMapping("/inquiry/{userId}/add")
    public ResponseEntity<ResponseDto<String>> addInquiry(@PathVariable Long userId, @RequestBody IqrAddRequestDto iqrDto){
        return csService.addInquiry(userId, iqrDto);
    }

    //신고내역 목록
    //250923
    @GetMapping("/report/list")
    public ResponseEntity<ResponseDto<List<Report>>> reportList() {
            return csService.reportList();
        }


    //내 신고내역 목록
    //250923
    @GetMapping("/report/mylist/{userId}")
    public ResponseEntity<ResponseDto<List<Report>>> reportList(@PathVariable Long userId) {
            return csService.myReportList(userId);
    }

    //신고 등록
    //250924
    @PostMapping("/report/{userId}/add")
    public ResponseEntity<ResponseDto<String>> addReport(@PathVariable Long userId, @RequestBody RpAddRequestDto rpDto){
        return csService.addReport(userId, rpDto);
    }

}
