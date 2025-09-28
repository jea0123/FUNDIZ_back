package com.example.funding.mapper;

import com.example.funding.dto.request.address.AddrUpdateRequestDto;
import com.example.funding.dto.response.address.AddressResponseDto;
import com.example.funding.model.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {
    List<AddressResponseDto> getAddressList(@Param("userId") Long userId);

    int addAddr(Address addr);

    int resetDefaultAddr(Long userId, Long addrId);

    int updateAddr(AddrUpdateRequestDto addrDto);

    int deleteAddr(@Param("userId") Long userId, @Param("addrId") Long addrId);
}
