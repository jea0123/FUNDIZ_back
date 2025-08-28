package com.example.funding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Admin {
    private long adId;
    private String adminId;
    private String adminPwd;
    private long author;
    private String role;
}
