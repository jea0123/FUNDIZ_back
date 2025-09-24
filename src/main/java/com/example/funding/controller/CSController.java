package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
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
    @GetMapping("/notice")
    public ResponseEntity<ResponseDto<List<Notice>>> noticeList() {
        return csService.noticeList();
    }

    //공지사항 상세
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<ResponseDto<Notice>> item(@PathVariable Long noticeId) {
        return csService.item(noticeId);
    }

    //공지사항 추가
    @PostMapping("/notice/add")
    public ResponseEntity<ResponseDto<String>> addNotice(@RequestBody Notice notice){
        return csService.addNotice(notice);
    }

    //공지사항 수정
    @PostMapping("notice/update/{noticeId}")
    public ResponseEntity<ResponseDto<String>> updateNotice(@PathVariable Long noticeId, @RequestBody Notice notice){
        return csService.updateNotice(noticeId, notice);
    }

    //공지사항 삭제
    @DeleteMapping("notice/delete/{noticeId}")
    public ResponseEntity<ResponseDto<String>> deleteNotice(@PathVariable Long noticeId) {
        return csService.deleteNotice(noticeId);
    }

    //문의내역 목록
    @GetMapping("/inquiry")
    public ResponseEntity<ResponseDto<List<Inquiry>>> inquiryList() {
        return csService.inquiryList();
    }

    //신고내역 목록
    @GetMapping("/report")
    public ResponseEntity<ResponseDto<List<Report>>> reportList() {
        return csService.reportList();
    }

}
