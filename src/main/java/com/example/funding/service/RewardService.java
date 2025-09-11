package com.example.funding.service;

import com.example.funding.dto.request.reward.RewardCreateRequestDto;

import java.util.List;

public interface RewardService {
    void createRewardList(Long projectId, List<RewardCreateRequestDto> rewardList);
}
