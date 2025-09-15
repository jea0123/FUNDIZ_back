package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.Backing.BackingResponseDto;
import com.example.funding.dto.response.user.BackingDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BackingService {
    ResponseEntity<ResponseDto<BackingResponseDto>> prepareBacking(Long userId, Long projectId);

    ResponseEntity<ResponseDto<String>> createBacking(Long userId, BackingRequestDto requestDto);

    ResponseEntity<ResponseDto<List<BackingDto>>> getBackingList(Long userId);

    ResponseEntity<ResponseDto<BackingDto>> getBackingDetail(Long userId, Long projectId, Long rewardId);

    ResponseEntity<ResponseDto<String>> updateBacking(BackingRequestUpdateDto requestDto, Long backingId);

    ResponseEntity<ResponseDto<String>> deleteBacking(Long backingId, Long userId);
}
