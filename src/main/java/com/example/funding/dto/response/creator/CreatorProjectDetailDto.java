package com.example.funding.dto.response.creator;

import com.example.funding.model.Reward;
import com.example.funding.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreatorProjectDetailDto {
    private Long projectId;
    private Long creatorId;
    private String title;
    private String content;
    private String thumbnail;
    private Integer goalAmount;
    private LocalDate startDate;
    private LocalDate endDate;

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
