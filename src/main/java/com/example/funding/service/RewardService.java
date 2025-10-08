package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.dto.request.reward.RewardUpdateRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RewardService {
    void createReward(Long projectId, List<RewardCreateRequestDto> rewardList);

    ResponseEntity<ResponseDto<String>> addReward(Long projectId, RewardCreateRequestDto dto);

    ResponseEntity<ResponseDto<String>> updateReward(Long projectId, Long rewardId, RewardUpdateRequestDto dto);

    void replaceRewards(Long projectId, List<RewardCreateRequestDto> rewardList);

    ResponseEntity<ResponseDto<String>> deleteReward(Long projectId, Long rewardId);
}
