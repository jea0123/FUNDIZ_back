package com.example.funding.controller;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.request.address.AddrDefaultSetDto;
import com.example.funding.dto.request.address.AddrUpdateRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/shipping")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/{userId}/list")
    public ResponseEntity<ResponseDto<List<AddressResponseDto>>> getAddressList(@PathVariable Long userId){
        return addressService.getAddrList(userId);
    }

    //주소지 추가
    @PostMapping("/{userId}/add")
    public ResponseEntity<ResponseDto<String>> addAddress(@PathVariable Long userId,
                                                          @RequestBody AddrAddRequestDto addrDto){
        return addressService.addAddress(userId, addrDto);
    }

    @PostMapping("{userId}/defaultAddr/{addrId}")
    public ResponseEntity<ResponseDto<String>> setDefaultAddr(@PathVariable Long userId, @PathVariable Long addrId, @RequestBody AddrDefaultSetDto addrDefaultDto){
        return addressService.defaultAddr(userId, addrId, addrDefaultDto);
    }

    // 주소지 기본설정 컨트롤러 분리필요
    @PostMapping("{userId}/update/{addrId}")
    public ResponseEntity<ResponseDto<String>> updateAddress(@PathVariable Long userId,
                                                             @PathVariable Long addrId,
                                                             @RequestBody AddrUpdateRequestDto addrDto){
        return addressService.updateAddr(userId, addrId,addrDto);
    }

    @DeleteMapping("{userId}/delete/{addrId}")
    public ResponseEntity<ResponseDto<String>> deleteAddress(@PathVariable Long userId,
                                                             @PathVariable Long addrId) {
        return addressService.deleteAddr(userId,addrId);
    }


}
