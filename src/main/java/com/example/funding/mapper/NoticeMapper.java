package com.example.funding.mapper;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;


import java.util.List;
@Mapper
public interface NoticeMapper {
    List<Notice> list();

    Notice noticeDetail(@Param("noticeId") Long noticeId);

    void updateViewCnt(@Param("noticeId") Long noticeId);

    ResponseEntity<ResponseDto<Notice>> add(@Param("noticeId") Notice item);

    ResponseEntity<ResponseDto<Notice>> update(@Param("noticeId") Notice item);

    void delete(@Param("noticeId") Long noticeId);

    Notice item(@Param("noticeId") Long noticeId);


}
