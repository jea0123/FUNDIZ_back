package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Backing {
    private Long backingId;
    private Long userId;
    private Long amount;
    private Date createdAt;
    private String backingStatus;
}
