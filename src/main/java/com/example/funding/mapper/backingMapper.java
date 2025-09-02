package com.example.funding.mapper;

import com.example.funding.dto.response.user.MyPageBackingProjectDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface backingMapper {
    MyPageBackingProjectDto getMyPageBackingProjectId(@Param("projectId")Long projectId);
}
