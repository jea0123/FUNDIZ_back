package com.example.funding.dto.request.creator;

import com.example.funding.enums.CreatorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatorRegisterRequestDto {
    private String creatorName;
    private CreatorType creatorType;
    private String email;
    private String phone;
    private String bank;
    private String account;
    private String businessNumber;
//    private MultipartFile profileImg;
}
