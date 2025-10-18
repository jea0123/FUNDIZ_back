package com.example.funding.dto.request.creator;

import com.example.funding.enums.CreatorType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreatorRegisterRequestDto {
    @NotBlank(message = "창작자 이름은 필수입니다.")
    @Size(min = 2, max = 10, message = "창작자 이름은 2자 이상 10자 이하여야 합니다.")
    private String creatorName;
    @NotBlank(message = "창작자 타입은 필수입니다.")
    private CreatorType creatorType;
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)")
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;
    @NotBlank(message = "은행 정보는 필수입니다.")
    private String bank;
    @NotBlank(message = "계좌 번호는 필수입니다.")
    private String account;
    @Nullable
    @Pattern(regexp = "^\\d{3}-\\d{2}-\\d{5}$", message = "사업자 등록번호 형식이 올바르지 않습니다. (예: 123-45-67890)")
    private String businessNumber;
    @Nullable
    private MultipartFile profileImg;
}
