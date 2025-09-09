package com.example.funding.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Backing {
    private Long backingId;
    private Long userId;
    private Long amount;
    private Date createdAt;
    private String backingStatus;
}
