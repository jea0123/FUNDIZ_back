package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.model.Reward;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface RewardService {
    void createReward(Long projectId, List<RewardCreateRequestDto> rewardList , LocalDateTime endDate, boolean validated);

    void replaceRewards(Long projectId, List<RewardCreateRequestDto> rewardList, LocalDateTime endDate);

    ResponseEntity<ResponseDto<String>> deleteReward(Long projectId, Long rewardId);

    ResponseEntity<ResponseDto<List<Reward>>> getRewardListManage(Long projectId, Long creatorId);

    ResponseEntity<ResponseDto<String>> addReward(Long projectId, Long creatorId, RewardCreateRequestDto dto);
}
