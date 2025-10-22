package com.example.funding.mapper;

import com.example.funding.dto.request.payment.AddCardDto;
import com.example.funding.dto.response.payment.BackingPagePaymentDto;
import com.example.funding.dto.response.payment.CardListDto;
import com.example.funding.model.Backing;
import com.example.funding.model.Payment;
import com.example.funding.model.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PaymentMapper {

    List<BackingPagePaymentDto> backingPagePayment(Long userId);

    Payment findUserMethod(@Param("userId")Long userId, @Param("method")String method,@Param("cardCompany")String cardCompany);

    void addPayment(Payment payment);

    void updateBackingPayment(Payment payment);

    void savePaymentInfo(@Param("userId")Long userId, @Param("method")String method,@Param("cardCompany")String cardCompany);

    List<CardListDto> getCardList(@Param("userId")Long userId);

    void addCard(AddCardDto addCardDto);

    void deleteCard(@Param("payInfoId")Long payInfoId);

    List<PaymentInfo> getPaymentInfo(@Param("userId")Long userId);

}