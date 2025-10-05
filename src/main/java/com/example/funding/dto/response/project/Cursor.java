package com.example.funding.dto.response.project;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Cursor {
    private final LocalDateTime lastCreatedAt;
    private final Long lastId;
}
