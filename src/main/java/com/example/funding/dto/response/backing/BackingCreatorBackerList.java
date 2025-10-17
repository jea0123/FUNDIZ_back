package com.example.funding.dto.response.backing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BackingCreatorBackerList {

    // 유저
    private Long userId;
    private String nickname;

    //후원
    private Long amount;
    private LocalDateTime createdAt;

    private Long projectId;
}
