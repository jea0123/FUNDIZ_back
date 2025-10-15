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

    void addAddr(Address addr);

    void resetDefaultAddr(@Param("userId") Long userId, @Param("addrId") Long addrId);

    void updateAddr(AddrUpdateRequestDto addrDto);

    void deleteAddr(@Param("userId") Long userId, @Param("addrId") Long addrId);

    Address getAddr(@Param("addrId") Long addrId);
}
