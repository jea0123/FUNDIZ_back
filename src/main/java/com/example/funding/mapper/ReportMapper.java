package com.example.funding.mapper;


import com.example.funding.common.Pager;
import com.example.funding.model.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface ReportMapper {
    List<Report> reportList(@Param("pager") Pager pager);

    List<Report> myReportList(Long userId, @Param("pager") Pager pager);

    int reportTotal();

    int myReportTotal(Long userId);

    int addReport(Report item);

}
