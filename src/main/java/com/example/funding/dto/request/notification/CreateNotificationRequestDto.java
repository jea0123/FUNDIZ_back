package com.example.funding.dto.request.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNotificationRequestDto {
    private Long userId;
    private String type;
    private Long targetId;
    private String message;
}
