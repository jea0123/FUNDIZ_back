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
    private Long creatorId;
    private String title;
    private String content;
    private String thumbnail;
    private Integer goalAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String projectStatus;
    private LocalDateTime requestedAt;

    private Long ctgrId;
    private String ctgrName;
    private Long subctgrId;
    private String subctgrName;

    private String creatorName;
    private String businessNum;
    private String email;
    private String phone;

    private List<Tag> tagList;
    private List<Reward> rewardList;
}
