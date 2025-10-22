package com.example.funding.dto.response.creator;

import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreatorProjectDetailDto {
    private Long projectId;
    private Long creatorId;
    private String title;
    private Integer goalAmount;
    private Integer currAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String projectStatus;
    private String content;
    private String contentBlocks;
    private String thumbnail;
    private String businessDoc;

    private Long subctgrId;
    private String subctgrName;
    private Long ctgrId;
    private String ctgrName;

    private String creatorName;
    private String businessNum;
    private String email;
    private String phone;

    private List<Tag> tagList;
    private List<Reward> rewardList;
}
