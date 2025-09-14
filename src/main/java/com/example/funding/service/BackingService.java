package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.response.Backing.BackingResponseDto;
import org.springframework.http.ResponseEntity;

public interface BackingService {
    ResponseEntity<ResponseDto<BackingResponseDto>> prepareBacking(Long userId, Long projectId);

    ResponseEntity<ResponseDto<String>> createBacking(Long userId, BackingRequestDto requestDto);
}
