package com.example.funding.mapper;

import com.example.funding.dto.response.backing.userList_detail.MyPageBackingList_reward;
import com.example.funding.model.Reward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RewardMapper {
    List<Reward> getRewardListPublic(@Param("projectId") Long projectId);

    List<Reward> findByProjectId(@Param("projectId") Long projectId);

    void saveReward(Reward reward);

    void deleteReward(@Param("projectId") Long projectId, @Param("rewardId") Long rewardId);

    void deleteRewards(@Param("projectId") Long projectId);

    List<Reward> getRewardListManage(@Param("projectId") Long projectId, @Param("creatorId") Long creatorId);

    Reward findById(@Param("rewardId") Long rewardId);

    List<MyPageBackingList_reward>getMyPageDetailRewardList (Long rewardId);
}
