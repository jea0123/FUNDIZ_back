package com.example.funding.controller;


import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.response.orders.OrdersResponseDto;
import com.example.funding.service.OrdersService;
import com.example.funding.service.ProjectService;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;
    private final ProjectService projectService;
    private final UserService userService;



    @GetMapping("/order/{projectId}")
    public ResponseEntity<ResponseDto<OrdersResponseDto>>getOrders(@PathVariable Long projectId,
                                                                   @AuthenticationPrincipal(expression = "userId") Long userId) {
        return ordersService.prepareOrder(userId, projectId);

    }
}
