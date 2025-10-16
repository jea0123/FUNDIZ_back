package com.example.funding.mapper;

import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.request.reward.RewardBackingRequestDto;
import com.example.funding.dto.response.backing.BackingCreatorBackerList;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingListDto;
import com.example.funding.dto.response.backing.userList_detail.MyPageBackingList_backingDetail;
import com.example.funding.dto.response.creator.DailyCountDto;
import com.example.funding.dto.response.creator.MonthCountDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.model.Backing;
import com.example.funding.model.BackingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BackingMapper {
    //후원한 프로젝트 리스트
    List<BackingDto> getBackingListUserId(@Param("userId") Long userId);

    //후원한 목록의 상세 리스트
    BackingDto getBackingProjectAndUserId( @Param("userId") Long userId,@Param("projectId")Long projectId, @Param("rewardId") Long rewardId, @Param("backingId") Long backingId);

    void addBacking(Backing backing);

    void addBackingDetail(BackingDetail backingDetail);

    void updateBacking(BackingRequestUpdateDto updateDto);

    long getBackerCnt(Long creatorId);

    List<BackingCreatorBackerList> getCBackerList(Long creatorId);

    List<DailyCountDto> dailyCount(Long creatorId);

    List<MonthCountDto> monthCount(Long creatorId);

    Backing findById(Long backingId);

    List<MyPageBackingListDto> getBackingList(@Param("userId") Long userId);

    List<MyPageBackingList_backingDetail> getMyPageDetailBackingList(Long userId);


}
