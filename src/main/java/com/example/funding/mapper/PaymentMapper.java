package com.example.funding.mapper;

import com.example.funding.dto.response.payment.BackingPagePaymentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentMapper {

    List<BackingPagePaymentDto> backingPagePayment(Long userId);
}