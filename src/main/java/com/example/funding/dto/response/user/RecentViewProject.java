package com.example.funding.dto.response.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RecentViewProject {
    private Long projectId;
    private String creatorName;
    private String title;
    private String thumbnail;
    private int percentNow;
    private Integer currAmount;
    private Date endDate;
}
