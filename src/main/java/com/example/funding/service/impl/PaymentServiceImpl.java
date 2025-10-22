package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.payment.AddCardDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.payment.CardListDto;
import com.example.funding.mapper.BackingMapper;
import com.example.funding.mapper.PaymentMapper;
import com.example.funding.model.Payment;
import com.example.funding.service.PaymentService;
import com.example.funding.validator.Loaders;
import com.example.funding.validator.PermissionChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final Loaders loaders;
    private final PermissionChecker auth;
    private final PaymentMapper paymentMapper;
    private final BackingMapper backingMapper;

    @Override
    public ResponseEntity<ResponseDto<List<CardListDto>>> getCardList(Long userId) {
        loaders.user(userId);

        List<CardListDto> cardLists = paymentMapper.getCardList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용자 카드 리스트 불러오기 성공", cardLists));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> addCard(Long userId, AddCardDto requestCard) {
        loaders.user(userId);

        AddCardDto card = AddCardDto.builder()
                .payInfoId(requestCard.getPayInfoId())
                .userId(userId)
                .cardCompany(requestCard.getCardCompany())
                .method(requestCard.getMethod())
                .cardNum(requestCard.getCardNum())
                .build();
        paymentMapper.addCard(card);
        return ResponseEntity.ok(ResponseDto.success(200, "카드 등록 완료", null));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> deleteCard(Long userId, Long payInfoId) {
        paymentMapper.deleteCard(payInfoId);
        return ResponseEntity.ok(ResponseDto.success(200, "카드 삭제 완료", null));
    }
}
