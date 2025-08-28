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
    private long backingId;
    private long userId;
    private long amount;
    private Date createdAt;
    private String backingStatus;
}
