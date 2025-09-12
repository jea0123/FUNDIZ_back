package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.orders.OrdersResponseDto;
import org.springframework.http.ResponseEntity;

public interface OrdersService {
    ResponseEntity<ResponseDto<OrdersResponseDto>> prepareOrder(Long userId, Long projectId);
}
