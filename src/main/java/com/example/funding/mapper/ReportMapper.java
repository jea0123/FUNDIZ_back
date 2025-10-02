package com.example.funding.mapper;


import com.example.funding.model.Report;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface ReportMapper {
    List<Report> reportList();

    List<Report> myReportList(Long userId);

    int addReport(Report item);

}
