package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.request.address.AddrDefaultSetDto;
import com.example.funding.dto.request.address.AddrUpdateRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {
    ResponseEntity<ResponseDto<List<AddressResponseDto>>> getAddrList(Long userId);

    ResponseEntity<ResponseDto<String>> addAddress(Long userId, AddrAddRequestDto addrDto);

    ResponseEntity<ResponseDto<String>> updateAddr(Long userId, Long addrId, AddrUpdateRequestDto addrDto);

    ResponseEntity<ResponseDto<String>> deleteAddr(Long userId, Long addrId);

    ResponseEntity<ResponseDto<String>> defaultAddr(Long userId, Long addrId, AddrDefaultSetDto addrDefaultDto);
}
