package com.example.funding.mapper;

import com.example.funding.dto.response.payment.BackingPagePaymentDto;
import com.example.funding.model.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentMapper {

    List<BackingPagePaymentDto> backingPagePayment(Long userId);

    Payment findUserMethod(@Param("userId")Long userId, @Param("method")String method,@Param("cardCompany")String cardCompany);

    void addPayment(Payment payment);

    void updateBackingPayment(Payment payment);
}