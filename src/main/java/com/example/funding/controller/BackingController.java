package com.example.funding.controller;


import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.Backing.BackingResponseDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.service.BackingService;
import com.example.funding.service.ProjectService;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/Backing")
@RequiredArgsConstructor
public class BackingController {
    private final BackingService backingService;

    @GetMapping("/CreateBacking/{userId}/{projectId}")
    public ResponseEntity<ResponseDto<BackingResponseDto>>prepareBacking(@PathVariable Long projectId,
                                                                         @PathVariable Long userId) {
        return backingService.prepareBacking(userId, projectId);
    }

    @PostMapping("/CreateBacking/{userId}")
    public ResponseEntity<ResponseDto<String>>CreateBacking(@RequestBody BackingRequestDto requestDto,
                                                            @PathVariable Long userId) {
        return backingService.createBacking(userId, requestDto);
    }

    @PostMapping("/Backing/{backingId}/update{userId}")
    public ResponseEntity<ResponseDto<String>>UpdateBacking(@RequestBody BackingRequestUpdateDto requestDto,
                                                            @PathVariable Long backingId,
                                                            @PathVariable Long userId) {
        return backingService.updateBacking(requestDto, backingId);
    }

    @PostMapping("/Backing/{backingId}/delete{userId}")
    public ResponseEntity<ResponseDto<String>>DeleteBacking(@PathVariable Long backingId,
                                                            @PathVariable Long userId) {
        return backingService.deleteBacking(backingId, userId);
    }





    @GetMapping("/me/backingPage/{userId}")
    public ResponseEntity<ResponseDto<List<BackingDto>>>getBackingList(@PathVariable Long userId) {
        return backingService.getBackingList(userId);
    }

    @GetMapping("/me/backingPage/{userId}/project/{projectId}/reward/{rewardId}")
    public ResponseEntity<ResponseDto<BackingDto>> getBackingDetail(@PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long rewardId){
        return backingService.getBackingDetail(userId,projectId, rewardId);
    }

}
