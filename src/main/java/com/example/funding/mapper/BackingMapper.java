package com.example.funding.mapper;

import com.example.funding.dto.response.user.BackingProjectDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface BackingMapper {
    BackingProjectDto getMyPageBackingProjectId(@Param("projectId")Long projectId);
}
