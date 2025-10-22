package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingDetailDto;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingListDto;
import com.example.funding.dto.response.user.BackingDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface BackingService {
    ResponseEntity<ResponseDto<BackingResponseDto>> prepareBacking(Long userId, Long projectId);

    ResponseEntity<ResponseDto<String>> createBacking(Long userId, BackingRequestDto requestDto);

    ResponseEntity<ResponseDto<List<BackingDto>>> getBackingList(Long userId);

    ResponseEntity<ResponseDto<BackingDto>> getBackingDetail(Long userId, Long projectId, Long rewardId, Long backingId);

    ResponseEntity<ResponseDto<String>> updateBacking(BackingRequestUpdateDto requestDto, Long backingId, Long userId);

    ResponseEntity<ResponseDto<List<MyPageBackingListDto>>> getMyPageBackingList(Long userId);

    ResponseEntity<ResponseDto<MyPageBackingDetailDto>> getMyPageBackingDetail(Long userId, Long backingId);

    ResponseEntity<ResponseDto<String>> cancelBacking(Long userId, Long backingId);
}
