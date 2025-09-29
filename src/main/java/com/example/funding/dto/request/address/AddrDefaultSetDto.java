package com.example.funding.dto.request.address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddrDefaultSetDto {
    private Long addrId;
    private Long userId;
    private Character isDefault;
}
