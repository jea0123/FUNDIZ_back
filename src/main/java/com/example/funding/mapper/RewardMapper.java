package com.example.funding.mapper;

import com.example.funding.dto.request.reward.RewardCreateRequestDto;
import com.example.funding.model.Reward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RewardMapper {
    List<Reward> getRewardListById(@Param("projectId") Long projectId);

    List<Reward> findProjectIdsByRewardIds(@Param("ids") List<Long> rewardIds);

    void saveReward(Reward reward);
}
