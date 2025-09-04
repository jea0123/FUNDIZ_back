package com.example.funding.mapper;

import com.example.funding.model.BackingDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BackingDetailMapper {
    List<BackingDetail> findByBackingIds(@Param("ids") List<Long> backingIds);
}
