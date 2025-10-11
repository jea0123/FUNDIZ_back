package com.example.funding.dto.response.backing;

import lombok.*;

import java.time.LocalDate;

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
    private LocalDate createdAt;

    private Long projectId;
}
