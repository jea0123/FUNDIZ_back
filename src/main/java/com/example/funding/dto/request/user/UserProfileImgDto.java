package com.example.funding.dto.request.user;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserProfileImgDto {
    @Nullable
    private MultipartFile profileImg;
}
