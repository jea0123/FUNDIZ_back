package com.example.funding.mapper;

import com.example.funding.model.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface NoticeMapper {
    List<Notice> list();

    Notice noticeDetail(@Param("noticeId") Long noticeId);

    void updateViewCnt(@Param("noticeId") Long noticeId);

    Notice add(@Param("noticeId") Notice item);

    Notice update(@Param("noticeId") Notice item);

    Notice delete(@Param("noticeId") Long noticeId);

    Notice item(@Param("noticeId") Long noticeId);


}
