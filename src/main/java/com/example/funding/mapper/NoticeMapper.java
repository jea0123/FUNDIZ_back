package com.example.funding.mapper;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.NoticeUpdateRequestDto;
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

    int updateNotice(NoticeUpdateRequestDto ntcDto);

    int deleteNotice(Long noticeId);

}
