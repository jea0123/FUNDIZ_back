package com.example.funding.mapper;

import com.example.funding.dto.request.backing.BackingRequestDto;
import com.example.funding.dto.request.backing.BackingRequestUpdateDto;
import com.example.funding.dto.response.Backing.BackingResponseDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.dto.response.user.BackingDto;
import com.example.funding.model.Backing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;

@Mapper
public interface BackingMapper {
    //후원한 프로젝트 리스트
    List<BackingDto> getBackingListUserId(@Param("userId") Long userId);

    BackingDto getBackingProjectAndUserId( @Param("userId") Long userId,@Param("projectId")Long projectId, @Param("rewardId") Long rewardId);

    BackingResponseDto prepareBacking(@Param("userId") Long userId, @Param("projectId") Long projectId);

    int addBacking(BackingRequestDto backingRequestDto);

    int updateBacking(BackingRequestUpdateDto updateDto);

    int deleteBacking(@Param("backingId") Long backingId, @Param("userId") Long userId);
}
