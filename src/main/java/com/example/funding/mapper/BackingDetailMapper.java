package com.example.funding.mapper;

import com.example.funding.dto.response.user.BackingDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BackingDetailMapper {
    //후원한 프로젝트 상세정보
    BackingDetailDto getBackingProjectAndUserId(@Param("projectId")Long projectId, @Param("userId") Long userId);
}
