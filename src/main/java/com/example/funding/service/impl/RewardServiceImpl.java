package com.example.funding.service.impl;

import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.mapper.RewardMapper;
import com.example.funding.model.Reward;
import com.example.funding.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardServiceImpl implements RewardService {

    private final RewardMapper rewardMapper;

    @Override
    public void createRewardList(Long projectId, List<RewardCreateRequestDto> rewardList) {
        for (RewardCreateRequestDto dto : rewardList) {
            Reward reward = Reward.builder()
                    .projectId(projectId)
                    .rewardName(dto.getRewardName())
                    .price(dto.getPrice())
                    .rewardContent(dto.getRewardContent())
                    .deliveryDate(dto.getDeliveryDate())
                    .rewardCnt(dto.getRewardCnt())
                    .isPosting(dto.getIsPosting())
                    .remain(dto.getRewardCnt()) // 초기 remain = rewardCnt
                    .build();

            rewardMapper.saveReward(reward);
        }
    }
}
