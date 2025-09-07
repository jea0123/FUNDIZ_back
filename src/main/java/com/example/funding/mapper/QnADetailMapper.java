package com.example.funding.mapper;

import com.example.funding.model.Qna;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QnADetailMapper {
    Qna getQnAById(@Param("userId")Long userId, @Param("projectId")Long projectIds);
}
