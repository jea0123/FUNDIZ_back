package com.example.funding.mapper;

import com.example.funding.dto.ResponseDto;
import com.example.funding.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Mapper
public interface ReportMapper {
    List<Report> reportList();

    //Notice noticeDetail(@Param("noticeId") Long noticeId);

    //void add(ResponseEntity<ResponseDto<Notice>> item);

    //Notice update(@Param("noticeId") Notice item);

    //Notice delete(@Param("noticeId") Long noticeId);

    //Notice item(@Param("noticeId") Long noticeId);


}
