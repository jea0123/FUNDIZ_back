package com.example.funding.mapper;

import com.example.funding.dto.response.user.BackingDetailDto;
import com.example.funding.dto.response.user.BackingListDto;
import com.example.funding.dto.response.user.BackingProjectDto;
import com.example.funding.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface BackingMapper {
    BackingProjectDto getMyPageBackingProjectId(@Param("projectId")Long projectId);
    
    //후원한 프로젝트 리스트
    List<BackingDetailDto> getBackingListUserId(@Param("userId") Long userId);

}
