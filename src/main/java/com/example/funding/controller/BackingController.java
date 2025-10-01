package com.example.funding.controller;


import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.service.BackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/Backing")
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

    //TODO: 결제 취소로 바꾸기
    @DeleteMapping("/{backingId}/delete/{userId}")
    public ResponseEntity<ResponseDto<String>> deleteBacking(@PathVariable Long backingId,
                                                             @PathVariable Long userId) {
        return backingService.deleteBacking(backingId, userId);
    }

    @GetMapping("/page/{userId}")
    public ResponseEntity<ResponseDto<List<BackingDto>>>getBackingList(@PathVariable Long userId) {
        return backingService.getBackingList(userId);
    }

    @GetMapping("/page/{userId}/project/{projectId}/reward/{rewardId}")
    public ResponseEntity<ResponseDto<BackingDto>> getBackingDetail(@PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long rewardId){
        return backingService.getBackingDetail(userId,projectId, rewardId);
    }

}
