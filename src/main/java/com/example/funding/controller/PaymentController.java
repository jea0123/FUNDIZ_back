package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.payment.AddCardDto;
import com.example.funding.dto.response.backing.BackingResponseDto;
import com.example.funding.dto.response.payment.CardListDto;
import com.example.funding.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: 결제창 만들기
@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/cardList")
    public ResponseEntity<ResponseDto<List<CardListDto>>>cardList(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return paymentService.getCardList(principal.userId());
    }

    @PostMapping("/addCardList")
    public ResponseEntity<ResponseDto<String>>addCard(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                      @RequestBody AddCardDto requestCard){
        return paymentService.addCard(principal.userId(), requestCard);
    }
    @DeleteMapping("/deleteCard/{payInfoId}")
    public ResponseEntity<ResponseDto<String>>deleteCard(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                         @PathVariable Long payInfoId){
        return paymentService.deleteCard(principal.userId(), payInfoId);
    }

}
