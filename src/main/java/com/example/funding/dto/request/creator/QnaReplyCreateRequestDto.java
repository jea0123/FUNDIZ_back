package com.example.funding.dto.request.creator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaReplyCreateRequestDto {
    private String content;
    private Long creatorId;
}
