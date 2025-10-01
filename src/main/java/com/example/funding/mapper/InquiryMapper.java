package com.example.funding.mapper;


import com.example.funding.model.Inquiry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquiryMapper {
    List<Inquiry> inquiryList();

    List<Inquiry> myInquiryList(Long userId);

    int addInquiry(Inquiry item);

}
