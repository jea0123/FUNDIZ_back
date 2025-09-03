package com.example.funding.mapper;

import com.example.funding.model.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReplyMapper {
  List<Reply> getReplyListById(@Param("cmId") Long cmId, @Param("code") String code);
}
