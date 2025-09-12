package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class shippingController {
    private final UserService userService;
//
//    @GetMapping("/list/{userId")
//    public ResponseEntity<ResponseDto<>>
}
