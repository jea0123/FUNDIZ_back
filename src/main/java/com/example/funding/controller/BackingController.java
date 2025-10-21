package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/backing")
@RequiredArgsConstructor
public class BackingController {
    private final BackingService backingService;

    @GetMapping("/prepare/{projectId}")
    public ResponseEntity<ResponseDto<BackingResponseDto>> prepareBacking(@PathVariable Long projectId,
                                                                          @AuthenticationPrincipal CustomUserPrincipal principal) {
        return backingService.prepareBacking(principal.userId(), projectId);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<String>> createBacking(@RequestBody BackingRequestDto requestDto,
                                                             @AuthenticationPrincipal CustomUserPrincipal principal) {
        return backingService.createBacking(principal.userId(), requestDto);
    }

    @PostMapping("/cancel/{backingId}")
    public ResponseEntity<ResponseDto<String>> cancelBacking(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                             @PathVariable Long backingId) {
        return backingService.cancelBacking(principal.userId(), backingId);
    }

    @PostMapping("/{backingId}/update")
    public ResponseEntity<ResponseDto<String>> updateBacking(@RequestBody BackingRequestUpdateDto requestDto,
                                                             @PathVariable Long backingId,
                                                             @AuthenticationPrincipal CustomUserPrincipal principal) {
        return backingService.updateBacking(requestDto, backingId, principal.userId());
    }

    @GetMapping("/myPageBackingList/{userId}")
    public ResponseEntity<ResponseDto<List<MyPageBackingListDto>>> geMyPageBackingList(@PathVariable Long userId) {
        return backingService.getMyPageBackingList(userId);
    }

    @GetMapping("/page")
    public ResponseEntity<ResponseDto<List<BackingDto>>> getBackingList(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return backingService.getBackingList(principal.userId());
    }

    @GetMapping("/page/project/{projectId}/reward/{rewardId}/backing/{backingId}")
    public ResponseEntity<ResponseDto<BackingDto>> getBackingDetail(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                                    @PathVariable Long projectId,
                                                                    @PathVariable Long rewardId,
                                                                    @PathVariable Long backingId) {
        return backingService.getBackingDetail(principal.userId(), projectId, rewardId, backingId);
    }

    @GetMapping("/myPageBackingList")
    public ResponseEntity<ResponseDto<List<MyPageBackingListDto>>> geMyPageBackingList(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return backingService.getMyPageBackingList(principal.userId());
    }

    @GetMapping("/myPageBackingDetail/{backingId}")
    public ResponseEntity<ResponseDto<MyPageBackingDetailDto>> geMyPageBackingDetail(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return backingService.getMyPageBackingDetail(principal.userId());
    }

}
