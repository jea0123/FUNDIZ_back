package com.example.funding.mapper;

import com.example.funding.dto.response.user.BackingDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface BackingMapper {
    //후원한 프로젝트 리스트
    List<BackingDto> getBackingListUserId(@Param("userId") Long userId);

}
