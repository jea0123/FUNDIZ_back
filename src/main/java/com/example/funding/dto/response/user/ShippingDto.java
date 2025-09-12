package com.example.funding.dto.response.user;


import com.example.funding.enums.BackingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShippingDto {
    private Long shippingId;
    private Long backingId;
    private Long addrId;
    private BackingStatus shippingStatus;



}
