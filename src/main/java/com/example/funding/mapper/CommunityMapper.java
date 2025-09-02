package com.example.funding.mapper;

import com.example.funding.dto.response.project.CommunityDto;
import com.example.funding.model.Community;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {
    List<Community> getCommunityListById(@Param("projectId") Long projectId, @Param("code") String code);
}
