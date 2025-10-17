package com.example.funding.dto.response.creator;

import com.example.funding.enums.CreatorType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreatorInfoDto {
    private Long creatorId;
    private String creatorName;
    private CreatorType creatorType;
    private String email;
    private String phone;
    private String bank;
    private String account;
    private String businessNumber;
    private String profileImg;
    private String profileImgUrl;
}
