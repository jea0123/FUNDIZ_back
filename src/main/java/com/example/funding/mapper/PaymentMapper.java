package com.example.funding.mapper;

import com.example.funding.model.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface PaymentMapper {
    List<Payment> findRecentSuccessful(@Param("since") Date since);
}