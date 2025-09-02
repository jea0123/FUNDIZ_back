package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Notice;
import com.example.funding.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

        @GetMapping
        ResponseEntity<ResponseDto<List<Notice>> list() {
            return noticeService.list();
        }

        @GetMapping("/{noticeId}")
        public ResponseEntity<ResponseDto<Notice>> getNoticeDetail(@PathVariable("noticeId") Long noticeId) {
            return noticeService.getNoticeDetail(noticeId);
        }

        @PostMapping
        public ResponseEntity<ResponseDto<Notice>> add(@RequestBody Notice item) {
            return noticeService.add(item);
        }

        @PutMapping
        public ResponseEntity<ResponseDto<Notice>> update(@RequestBody Notice item) {
            return noticeService.update(item);
        }

        @DeleteMapping("/{noticeId}")
        void delete(@PathVariable Long noticeId) {
            noticeService.delete(noticeId);
    }
}
