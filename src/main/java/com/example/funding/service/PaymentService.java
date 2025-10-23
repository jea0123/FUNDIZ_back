package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.payment.AddCardDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.payment.CardListDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {
    ResponseEntity<ResponseDto<List<CardListDto>>> getCardList(Long userId);

    ResponseEntity<ResponseDto<String>> addCard(Long userId, AddCardDto requestCard);

    ResponseEntity<ResponseDto<String>> deleteCard(Long userId, Long payInfoId);
}
