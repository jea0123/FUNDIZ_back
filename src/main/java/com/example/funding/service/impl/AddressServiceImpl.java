package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.request.address.AddrDefaultSetDto;
import com.example.funding.dto.request.address.AddrUpdateRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.mapper.AddressMapper;
import com.example.funding.model.Address;
import com.example.funding.service.AddressService;
import com.example.funding.validator.Loaders;
import com.example.funding.validator.PermissionChecker;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class AddressServiceImpl implements AddressService {
    private final Loaders loaders;
    private final PermissionChecker auth;
    private final AddressMapper addressMapper;

    @Override
    public ResponseEntity<ResponseDto<List<AddressResponseDto>>> getAddrList(@NotBlank Long userId) {
        loaders.user(userId);

        List<AddressResponseDto> addrResponse = addressMapper.getAddressList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용자 주소 리스트 불러오기 성공", addrResponse));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> addAddress(@NotBlank Long userId, AddrAddRequestDto addrDto) {
        loaders.user(userId);
        Address addr = Address.builder()
                .userId(userId)
                .addrName(addrDto.getAddrName())
                .recipient(addrDto.getRecipient())
                .postalCode(addrDto.getPostalCode())
                .roadAddr(addrDto.getRoadAddr())
                .detailAddr(addrDto.getDetailAddr())
                .recipientPhone(addrDto.getRecipientPhone())
                .isDefault(addrDto.getIsDefault())
                .build();
        addressMapper.addAddr(addr);
        return ResponseEntity.ok(ResponseDto.success(200, "배송지 추가 성공", null));
    }


    @Override
    public ResponseEntity<ResponseDto<String>> updateAddr(@NotBlank Long userId, @NotBlank Long addrId, AddrUpdateRequestDto addrDto) {
        loaders.user(userId);
        Address existingAddr = loaders.address(addrId);
        auth.mustBeOwner(userId, existingAddr.getUserId());

        addrDto.setAddrId(addrId);
        addrDto.setUserId(userId);
        addressMapper.updateAddr(addrDto);
        return ResponseEntity.ok(ResponseDto.success(200, "주소 수정 완료", null));
    }


    @Override
    public ResponseEntity<ResponseDto<String>> deleteAddr(@NotBlank Long userId, @NotBlank Long addrId) {
        loaders.user(userId);
        Address existingAddr = loaders.address(addrId);
        auth.mustBeOwner(userId, existingAddr.getUserId());

        addressMapper.deleteAddr(userId, addrId);
        return ResponseEntity.ok(ResponseDto.success(200, "주소 삭제 완료", null));
    }

    //TODO: 컨트롤러 분리 (기본주소지설정)
    @Override
    public ResponseEntity<ResponseDto<String>> defaultAddr(@NotBlank Long userId, @NotBlank Long addrId, AddrDefaultSetDto addrDefaultDto) {
        loaders.user(userId);
        Address existingAddr = loaders.address(addrId);
        auth.mustBeOwner(userId, existingAddr.getUserId());

        addrDefaultDto.setUserId(userId);
        addrDefaultDto.setAddrId(addrId);

        //기본 값을 'N'으로 바꿈
        addressMapper.resetDefaultAddr(userId, addrId);
        return ResponseEntity.ok(ResponseDto.success(200, "배송지 기본설정 완료", null));
    }

}
