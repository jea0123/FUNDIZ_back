package com.example.funding.mapper;

import com.example.funding.dto.request.reward.RewardUpdateRequestDto;
import com.example.funding.model.Reward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RewardMapper {
    List<Reward> getRewardList(@Param("projectId") Long projectId);

    List<Reward> findProjectIdsByRewardIds(@Param("ids") List<Long> rewardIds);

    int saveReward(Reward reward);

    int updateReward(RewardUpdateRequestDto dto);

    int deleteReward(@Param("projectId") Long projectId, @Param("rewardId") Long rewardId);
}
