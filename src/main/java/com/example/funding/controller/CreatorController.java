package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.creator.CreatorPListDto;
import com.example.funding.service.CreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/creator")
@RequiredArgsConstructor
public class CreatorController {
    private final CreatorService creatorService;

    //프로젝트 관리 리스트
    @GetMapping("/{creatorId}/list")
    public ResponseEntity<ResponseDto<List<CreatorPListDto>>>getCreatorPList(@PathVariable Long creatorId){
        return creatorService.getCreatorPList(creatorId);
    }
}
