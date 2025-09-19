package com.example.funding.service.impl;

import com.example.funding.dto.ResponseDto;
import com.example.funding.dto.request.address.AddrAddRequestDto;
import com.example.funding.dto.request.address.AddrUpdateRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.mapper.AddressMapper;
import com.example.funding.mapper.UserMapper;
import com.example.funding.model.Address;
import com.example.funding.model.User;
import com.example.funding.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;
    private final UserMapper userMapper;


    @Override
    public ResponseEntity<ResponseDto<List<AddressResponseDto>>> getAddrList(Long userId) {
        User user = userMapper.getUserById(userId);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "사용자를 찾을 수 없습니다."));
        }
        List<AddressResponseDto> addrResponse = addressMapper.getAddressList(userId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(200, "사용자 주소 리스트 불러오기 성공", addrResponse));
    }

    @Override
    public ResponseEntity<ResponseDto<String>> addAddress(Long userId, AddrAddRequestDto addrDto) {
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

        int result = addressMapper.addAddr(addr);
        
        if(result ==0){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"배송지 추가 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200,"배송지 추가 성공", "데이터 출력확인"));
    }


    @Override
    public ResponseEntity<ResponseDto<String>> updateAddr(Long userId, Long addrId, AddrUpdateRequestDto addrDto) {
        addrDto.setAddrId(addrId);
        addrDto.setUserId(userId);

        int result = addressMapper.updateAddr(addrDto);
        if(result ==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404, "주소 수정 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200,"주소 수정 완료", "주소 수정 "));
    }

    
    @Override
    public ResponseEntity<ResponseDto<String>> deleteAddr(Long userId, Long addrId) {
        int deleted = addressMapper.deleteAddr(userId, addrId);
        if(deleted == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.fail(404,"주소 삭제 실패"));
        }
        return ResponseEntity.ok(ResponseDto.success(200, "주소 삭제 완료", "주소 삭제"));
    }
}
