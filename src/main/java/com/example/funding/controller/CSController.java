package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Notice;
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

    @GetMapping("/notice")
    public ResponseEntity<ResponseDto<List<Notice>>> noticeList() {
        return csService.noticeList();
    }

    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<ResponseDto<Notice>> item(@PathVariable Long noticeId) {
        return csService.item(noticeId);
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Notice>> add(@RequestBody ResponseEntity<ResponseDto<Notice>> item) {
        csService.add(item);

        return item;
    }
}
