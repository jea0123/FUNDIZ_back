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
public class Community {
    private Long cmId;
    private Long userId;
    private Long projectId;
    private String content;
    private int rating;
    private Date createdAt;
    private String code;
}
