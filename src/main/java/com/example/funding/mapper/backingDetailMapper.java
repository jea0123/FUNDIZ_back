package com.example.funding.mapper;

import com.example.funding.dto.response.user.BackingProjectDto;
import com.example.funding.model.BackingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BackingDetailMapper {
    List<BackingDetail> findByBackingIds(@Param("ids") List<Long> backingIds);

    //후원한 프로젝트 상세정보
    BackingProjectDto getBackingProjectAndUserId(@Param("projectId")Long projectId, @Param("userId") Long userId);

}
