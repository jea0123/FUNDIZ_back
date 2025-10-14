package com.example.funding.dto.request.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserProfileImgDto {
    private MultipartFile profileImg;
}
