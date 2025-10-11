package com.example.funding.mapper;

import com.example.funding.model.Reward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RewardMapper {
    List<Reward> getRewardList(@Param("projectId") Long projectId);

    List<Reward> findByProjectId(@Param("projectId") Long projectId);

    List<Reward> findProjectIdsByRewardIds(@Param("ids") List<Long> rewardIds);

    int saveReward(Reward reward);

    int deleteReward(@Param("projectId") Long projectId, @Param("rewardId") Long rewardId);

    void deleteRewards(@Param("projectId") Long projectId);

    List<Reward> getCreatorRewardList(@Param("projectId") Long projectId, @Param("creatorId") Long creatorId);

    boolean existsByProjectIdAndNameNormalized(@Param("projectId") Long projectId, @Param("key") String key);

    Long getProjectIdByRewardId(@Param("rewardId") Long rewardId);
}
