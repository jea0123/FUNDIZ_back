package com.example.funding.mapper;


import com.example.funding.common.Pager;
import com.example.funding.model.Inquiry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InquiryMapper {
    List<Inquiry> inquiryList(@Param("pager") Pager pager);

    List<Inquiry> myInquiryList(Long userId, @Param("pager") Pager pager);

    int inquiryTotal();

    int myInquiryTotal(Long userId);

    void addInquiry(Inquiry item);

    int existsInquiryById(@Param("inqId") Long inqId);

    Inquiry findById(@Param("inqId") Long inqId);
}
