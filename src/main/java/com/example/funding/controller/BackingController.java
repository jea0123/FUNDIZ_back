package com.example.funding.controller;


import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingDetailDto;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingListDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.service.BackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/backing")
@RequiredArgsConstructor
public class BackingController {
    private final BackingService backingService;

    @GetMapping("/{userId}/create/{projectId}")
    public ResponseEntity<ResponseDto<BackingResponseDto>>prepareBacking(@PathVariable Long projectId,
                                                                         @PathVariable Long userId) {
        return backingService.prepareBacking(userId, projectId);
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<ResponseDto<String>> createBacking(@RequestBody BackingRequestDto requestDto,
                                                             @PathVariable Long userId) {
        return backingService.createBacking(userId, requestDto);
    }

    @PostMapping("/{backingId}/update/{userId}")
    public ResponseEntity<ResponseDto<String>> updateBacking(@RequestBody BackingRequestUpdateDto requestDto,
                                                             @PathVariable Long backingId,
                                                             @PathVariable Long userId) {
        return backingService.updateBacking(requestDto, backingId, userId);
    }

    @GetMapping("/page/{userId}")
    public ResponseEntity<ResponseDto<List<BackingDto>>>getBackingList(@PathVariable Long userId) {
        return backingService.getBackingList(userId);
    }

    @GetMapping("/page/{userId}/project/{projectId}/reward/{rewardId}/backing/{backingId}")
    public ResponseEntity<ResponseDto<BackingDto>> getBackingDetail(@PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long rewardId, @PathVariable Long backingId){
        return backingService.getBackingDetail(userId,projectId, rewardId, backingId);
    }

    @GetMapping("/myPageBackingList/{userId}")
    public ResponseEntity<ResponseDto<List<MyPageBackingListDto>>>geMyPageBackingList(@PathVariable Long userId) {
        return backingService.getMyPageBackingList(userId);
    }

    @GetMapping("/myPageBackingDetail/{userId}")
    public ResponseEntity<ResponseDto<List<MyPageBackingDetailDto>>>geMyPageBackingDetail(@PathVariable Long userId) {
        return backingService.getMyPageBackingDetail(userId);
    }
}
