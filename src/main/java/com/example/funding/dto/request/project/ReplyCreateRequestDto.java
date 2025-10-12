package com.example.funding.dto.request.project;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyCreateRequestDto {
    private String content;
    private Character isSecret;
}
