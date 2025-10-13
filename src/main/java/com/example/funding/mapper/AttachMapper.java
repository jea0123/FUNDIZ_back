package com.example.funding.mapper;

import com.example.funding.model.Attach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttachMapper {
    void insert(Attach attach);

    void delete(@Param("attachId") Long attachId);
}
