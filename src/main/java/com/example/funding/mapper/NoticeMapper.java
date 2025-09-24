package com.example.funding.mapper;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;
@Mapper
public interface NoticeMapper {
    List<Notice> noticeList();

    Notice noticeDetail(@Param("noticeId") Long noticeId);

    void updateViewCnt(@Param("noticeId") Long noticeId);

    void add(ResponseEntity<ResponseDto<Notice>> item);

    int addNotice(Notice item);

    int updateNotice(Notice notice);

    int deleteNotice(Long noticeId);

    //Notice update(@Param("noticeId") Notice item);

    //Notice delete(@Param("noticeId") Long noticeId);

    //Notice item(@Param("noticeId") Long noticeId);


}
