package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.orders.OrdersResponseDto;
import com.example.funding.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {



    @Override
    public ResponseEntity<ResponseDto<OrdersResponseDto>> prepareOrder(Long userId, Long projectId) {
        return null;
    }
}
