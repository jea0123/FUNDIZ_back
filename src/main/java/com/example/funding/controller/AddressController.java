package com.example.funding.controller;

import com.example.funding.common.CustomUserPrincipal;
import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.request.address.AddrDefaultSetDto;
import com.example.funding.dto.request.address.AddrUpdateRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/list")
    public ResponseEntity<ResponseDto<List<AddressResponseDto>>> getAddressList(@AuthenticationPrincipal CustomUserPrincipal principal){
        return addressService.getAddrList(principal.userId());
    }

    //주소지 추가
    @PostMapping("/add")
    public ResponseEntity<ResponseDto<String>> addAddress(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                          @RequestBody AddrAddRequestDto addrDto){
        return addressService.addAddress(principal.userId(), addrDto);
    }

    @PostMapping("/defaultAddr/{addrId}")
    public ResponseEntity<ResponseDto<String>> setDefaultAddr(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                              @PathVariable Long addrId,
                                                              @RequestBody AddrDefaultSetDto addrDefaultDto){
        return addressService.defaultAddr(principal.userId(), addrId, addrDefaultDto);
    }

    // 주소지 기본설정 컨트롤러 분리필요
    @PostMapping("/update/{addrId}")
    public ResponseEntity<ResponseDto<String>> updateAddress(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                             @PathVariable Long addrId,
                                                             @RequestBody AddrUpdateRequestDto addrDto){
        return addressService.updateAddr(principal.userId(), addrId,addrDto);
    }

    @DeleteMapping("/delete/{addrId}")
    public ResponseEntity<ResponseDto<String>> deleteAddress(@AuthenticationPrincipal CustomUserPrincipal principal,
                                                             @PathVariable Long addrId) {
        return addressService.deleteAddr(principal.userId(), addrId);
    }

}
