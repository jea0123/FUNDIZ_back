package com.example.funding.service;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.request.address.AddrDefaultSetDto;
import com.example.funding.dto.request.address.AddrUpdateRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface AddressService {
    ResponseEntity<ResponseDto<List<AddressResponseDto>>> getAddrList(@NotNull @Positive Long userId);

    ResponseEntity<ResponseDto<String>> addAddress(@NotNull @Positive Long userId, AddrAddRequestDto addrDto);

    ResponseEntity<ResponseDto<String>> updateAddr(@NotNull @Positive Long userId, @NotNull @Positive Long addrId, AddrUpdateRequestDto addrDto);

    ResponseEntity<ResponseDto<String>> deleteAddr(@NotNull @Positive Long userId, @NotNull @Positive Long addrId);

    ResponseEntity<ResponseDto<String>> defaultAddr(@NotNull @Positive Long userId, @NotNull @Positive Long addrId, AddrDefaultSetDto addrDefaultDto);
}
