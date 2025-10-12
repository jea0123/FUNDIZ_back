package com.example.funding.controller;

import com.example.funding.mapper.PaymentMapper;
import com.example.funding.mapper.ProjectMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: 결제창 만들기
@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
}
