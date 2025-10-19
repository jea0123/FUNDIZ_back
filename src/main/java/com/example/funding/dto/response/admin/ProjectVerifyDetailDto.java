package com.example.funding.dto.response.admin;

import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProjectVerifyDetailDto {
    private Long projectId;
    private String title;
    private Integer goalAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String content;
    private String contentBlocks;
    private String thumbnail;
    private String businessDoc;
    private String projectStatus;
    private LocalDateTime requestedAt;

    private String subctgrName;
    private String ctgrName;

    private String creatorName;
    private String businessNum;
    private String email;
    private String phone;

    private List<Tag> tagList;
    private List<Reward> rewardList;
}
