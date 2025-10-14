package com.example.funding.mapper;

import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.backing.BackingCreatorBackerList;
import com.example.funding.dto.response.backing.BackingResponseDto;
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
    BackingDto getBackingProjectAndUserId( @Param("userId") Long userId,@Param("projectId")Long projectId, @Param("rewardId") Long rewardId);

    //후원하기 페이지에서 이미 저장되있는 데이터를 가져오기 위한 dto
    BackingResponseDto prepareBacking(@Param("userId") Long userId, @Param("projectId") Long projectId);

    //
    int addBacking(Backing backing);

    int addBackingDetail(BackingDetail backingDetail);

    int updateBacking(BackingRequestUpdateDto updateDto);

    int deleteBacking(@Param("backingId") Long backingId, @Param("userId") Long userId);

    long getBackerCnt(Long creatorId);

    List<BackingCreatorBackerList> getCBackerList(Long creatorId);

    List<DailyCountDto> dailyCount(Long creatorId);

    List<MonthCountDto> monthCount(Long creatorId);

}
