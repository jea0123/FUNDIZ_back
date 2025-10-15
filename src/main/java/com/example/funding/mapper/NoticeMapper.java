package com.example.funding.mapper;

import com.example.funding.common.Pager;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.cs.NoticeUpdateRequestDto;
import com.example.funding.model.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<Notice> noticeList(@Param("pager") Pager pager);

    Notice noticeDetail(@Param("noticeId") Long noticeId);

    int noticeTotal();

    void updateViewCnt(@Param("noticeId") Long noticeId);

    void add(ResponseEntity<ResponseDto<Notice>> item);

    void addNotice(Notice item);

    void updateNotice(NoticeUpdateRequestDto ntcDto);

    void deleteNotice(Long noticeId);
}
