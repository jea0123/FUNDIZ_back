package com.example.funding.mapper;

import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.backing.BackingCreatorBackerList;
import com.example.funding.dto.response.backing.MyPageBackingDetailSaveDto;
import com.example.funding.dto.response.backing.MyPageBackingSaveDto;
import com.example.funding.dto.response.creator.DailyCountDto;
import com.example.funding.dto.response.creator.MonthCountDto;
import com.example.funding.model.Backing;
import com.example.funding.model.BackingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BackingMapper {
    void addBacking(Backing backing);

    void addBackingDetail(BackingDetail backingDetail);

    void updateBacking(BackingRequestUpdateDto updateDto);

    Long getBackerCnt(Long creatorId);

    List<BackingCreatorBackerList> getCBackerList(Long creatorId);

    List<DailyCountDto> dailyCount(Long creatorId);

    List<MonthCountDto> monthCount(Long creatorId);

    Backing findById(Long backingId);

    Long findBackingIdByUserId(Long userId);

    List<MyPageBackingSaveDto> getBackingLists(@Param("userId") Long userId);

    List<MyPageBackingDetailSaveDto> getBackingDetails(@Param("backingId") Long userId);

    void updateBackingStatus(Long backingId);

    void updateBackingStatusCompleted();

    void updateBackingStatusCancelled();
}
